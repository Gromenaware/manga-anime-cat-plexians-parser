package cat.plexians.test;

import cat.plexians.main.BaseWebDriver;
import cat.plexians.utils.FileDownloader;
import cat.plexians.utils.HtmlUtils;
import cat.plexians.utils.NavigationActions;
import cat.plexians.utils.ZipCreator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserFromOdiloTkEpubTest extends BaseWebDriver {

    //Val per epub
    @Test
    public void initialTest() throws InterruptedException {
        String baseBookUrl = "https://biblioteca.ebiblio.cat/info/bola-de-drac-super-n-21-00748865";
        String isbn = "9788411617581";
        String titolDelLlibre = "Bola de Drac Super n 21";
        String autorDelLlibre = "21";

        String pathForDownloads = File.separator + "Volumes" + File.separator + "TeraSingle" + File.separator + titolDelLlibre + File.separator;

        // Prepare directories
        parsingUtils.directoryCreation(pathForDownloads);

        try {
            driver.get(baseBookUrl);
            System.out.println("Accessing URL..." + baseBookUrl);
            Thread.sleep(3000);

            // Handle cookies dialog if present
            By cookiesDialog = By.xpath("//app-cookies-dialog");
            if (NavigationActions.elementExists(driver, cookiesDialog)) {
                System.out.println("Accepting cookies...");
                NavigationActions.hoverAndClick(driver, By.xpath("//app-cookies-dialog[1]/div[1]/div[2]/div[1]/button[2]"));
            }
            Thread.sleep(3000);

            // Click Preview
            NavigationActions.hoverAndClick(driver, By.xpath("//app-preview-button/button/span[1]"));
            Thread.sleep(3000);

            // Switch to second tab
            Set<String> windowHandles = driver.getWindowHandles();
            ArrayList<String> tabs = new ArrayList<>(windowHandles);
            if (tabs.size() < 2) {
                throw new IllegalStateException("Preview tab did not open.");
            }
            driver.switchTo().window(tabs.get(1));
            Thread.sleep(2000);

            // Find iframes and switch to the first one
            List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
            if (iframes.isEmpty()) {
                throw new IllegalStateException("No iframe found in the preview tab.");
            }
            driver.switchTo().frame(iframes.get(0));

            // Get iframe source and cookies
            String iframeSource = driver.getPageSource();

            System.out.println(iframeSource);

            String bookid_1 = "";
            String bookid_2 = "";
            String root_folder = "";

            try {
                // Parse the HTML using JSoup
                Document doc = Jsoup.parse(iframeSource);

                // Find the <link> element with href attribute
                Element linkElement = doc.select("link[href]").first();

                if (linkElement != null) {
                    // Extract the href attribute value
                    String hrefValue = linkElement.attr("href");

                    // Define regex to extract bookid_1, bookid_2, and root_folder
                    Pattern pattern = Pattern.compile(".*/file/([^/]+)/([^/]+)/([^/]+)/.*");
                    Matcher matcher = pattern.matcher(hrefValue);

                    if (matcher.matches()) {
                        bookid_1 = matcher.group(1);
                        bookid_2 = matcher.group(2);
                        root_folder = matcher.group(3);

                        // Print extracted values
                        System.out.println("bookid_1: " + bookid_1);
                        System.out.println("bookid_2: " + bookid_2);
                        System.out.println("root_folder: " + root_folder);
                    } else {
                        System.out.println("Pattern did not match.");
                    }
                } else {
                    System.out.println("No link element found.");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            Set<Cookie> cookies = driver.manage().getCookies();

            // Output the results
            String downloadingBaseUrl = "https://nubereader-epub.odilotk.es/nubereader/file" + File.separator + bookid_1 + File.separator + bookid_2 + File.separator;


            // Create epub
            // Define the location of the mimetype file
            String mimeTypeFilePath = pathForDownloads + File.separator + "epub" + File.separator + "mimetype";
            // Content for the mimetype file
            String content = "application/epub+zip";
            FileDownloader.writeStringToFile(content, mimeTypeFilePath);


            // https://nubereader-epub.odilotk.es/nubereader/file/E0360/00556878/META-INF/container.xml
            String metaInfDir = pathForDownloads + File.separator + "epub" + File.separator + "META-INF" + File.separator; // Directory to save downloaded files
            parsingUtils.directoryCreation(metaInfDir);
            String xmlContainerUrl = downloadingBaseUrl + "META-INF/container.xml";
            String containerXmlPath = metaInfDir + "container.xml";
            FileDownloader.downloadFileWithCookies(xmlContainerUrl, containerXmlPath, cookies);

            String epubPath = pathForDownloads + "epub" + File.separator; // Directory to save downloaded files

            String epubOEBPS = epubPath + root_folder + File.separator; // Directory to save downloaded files
            parsingUtils.directoryCreation(epubOEBPS);
            String fullPathValue = HtmlUtils.parseContainerXml(containerXmlPath);
            if (fullPathValue.isEmpty()) {
                throw new IllegalStateException("Failed to extract full-path attribute from container.xml.");
            }

            String opfUrl = downloadingBaseUrl + fullPathValue;
            System.out.println(opfUrl);
            String opfFilename = "";

            try {
                // Create URL object
                URL url = new URL(opfUrl);

                // Get the path from the URL
                String path = url.getPath();

                // Extract the filename
                opfFilename = path.substring(path.lastIndexOf("/") + 1);

                // Print extracted filename
                System.out.println("Extracted Filename: " + opfFilename);
            } catch (Exception e) {
                e.printStackTrace();
            }


            String opfPath = epubOEBPS + opfFilename;
            System.out.println(opfPath);
            FileDownloader.downloadFileWithCookies(opfUrl, opfPath, cookies);


            // Parse the OPF file and extract its structure
            Map<String, Object> epubStructure = FileDownloader.parseOPFFolders(opfPath);

            // Print extracted structure
            System.out.println("Folders: " + epubStructure.get("folders"));

            // Extract folder list
            List<String> folders = (List<String>) epubStructure.get("folders");

            // Create folders
            FileDownloader.createFolders(epubOEBPS, folders);

            System.out.println("Files: " + epubStructure.get("files"));


            // Extract file list
            List<Map<String, String>> epubFiles = (List<Map<String, String>>) epubStructure.get("files");

            // Process the file list
            for (Map<String, String> file : epubFiles) {
                String id = file.get("id");
                String href = file.get("href");
                String mediaType = file.get("media-type");

                System.out.println("File ID: " + id);
                System.out.println("File Path (href): " + href);
                System.out.println("File Media Type: " + mediaType);
                System.out.println("Download URL: " + downloadingBaseUrl + root_folder + File.separator + href);
                System.out.println("File Full Folder: " + epubOEBPS + href);

                FileDownloader.downloadFileWithCookies(downloadingBaseUrl + root_folder + File.separator + href, epubOEBPS + href, cookies);

            }


            // Create ZIP file
            String zipFileName = epubPath + titolDelLlibre + "_" + autorDelLlibre + "_" + isbn + ".epub";
            ZipCreator.createZip(epubPath, zipFileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}