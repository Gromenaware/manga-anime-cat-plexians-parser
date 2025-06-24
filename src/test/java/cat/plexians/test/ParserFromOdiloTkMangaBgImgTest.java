package cat.plexians.test;

import cat.plexians.main.BaseWebDriver;
import cat.plexians.utils.FileDownloader;
import cat.plexians.utils.HtmlUtils;
import cat.plexians.utils.NavigationActions;
import cat.plexians.utils.ZipCreator;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.openqa.selenium.*;
import org.testng.annotations.Test;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserFromOdiloTkMangaBgImgTest extends BaseWebDriver {

    //Val per manga
    @Test
    public void initialTest() {
        String baseBookUrl = "https://biblioteca.ebiblio.cat/info/bola-de-drac-color-bu-n-03-06-00748850";
        String isbn = "9788491463344";
        String nomDeLaSerie = "Bola_de_Drac_Color_Saga_Monstre_Bu_03_06";
        String volumDelLibre = "03";

        String pathForDownloads = File.separator + "Volumes" + File.separator + "TeraSingle" + File.separator + nomDeLaSerie + File.separator;

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


            // Download OPF
            // https://nubereader-epub.odilotk.es/nubereader/file/E0360/00556878/META-INF/container.xml
            parsingUtils.directoryCreation(pathForDownloads);
            String xmlContainerUrl = downloadingBaseUrl + "META-INF/container.xml";
            String containerXmlPath = pathForDownloads + "container.xml";
            FileDownloader.downloadFileWithCookies(xmlContainerUrl, containerXmlPath, cookies);

            String epubPath = pathForDownloads + "epub" + File.separator; // Directory to save downloaded files

            String epubOEBPS = epubPath + root_folder + File.separator; // Directory to save downloaded files
            parsingUtils.directoryCreation(epubOEBPS);
            String fullPathValue = HtmlUtils.parseContainerXml(containerXmlPath);
            if (fullPathValue.isEmpty()) {
                throw new IllegalStateException("Failed to extract full-path attribute from container.xml.");
            }

            String opfUrl = downloadingBaseUrl + fullPathValue;
            System.out.println("opfUrl file: " + opfUrl);
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
            System.out.println("opfPath: " + opfPath);

            FileDownloader.downloadFileWithCookies(opfUrl, opfPath, cookies);

            // Parse OPF file and extract page links
            List<String> pageLinks = HtmlUtils.extractPageBgImgLinks(opfPath, downloadingBaseUrl + root_folder + File.separator);

            // Download images
            String downloadImagesFolders = pathForDownloads + "images" + File.separator;
            parsingUtils.directoryCreation(downloadImagesFolders);
            for (int i = 0; i < pageLinks.size(); i++) {
                String imagePath = downloadImagesFolders + nomDeLaSerie + "_volum_" + volumDelLibre + "_page_" + i + ".jpg";
                System.out.println(pageLinks.get(i));
                driver.get(pageLinks.get(i));
                FileDownloader.downloadFileWithCookies(pageLinks.get(i), imagePath, cookies);
                Thread.sleep(1500); // Rate limiting
            }

            // Create ZIP file
            String zipFileName = pathForDownloads + nomDeLaSerie + "_volum_" + volumDelLibre + "_" + isbn + ".cbr";
            ZipCreator.createZip(downloadImagesFolders, zipFileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}