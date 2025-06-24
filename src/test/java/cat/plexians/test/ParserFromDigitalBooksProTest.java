package cat.plexians.test;

import cat.plexians.main.BaseWebDriver;
import cat.plexians.utils.FileDownloader;
import cat.plexians.utils.HtmlUtils;
import cat.plexians.utils.NavigationActions;
import cat.plexians.utils.ZipCreator;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ParserFromDigitalBooksProTest extends BaseWebDriver {

    @Test
    public void initialTest() {
        String baseBookUrl = "https://biblioteca.ebiblio.cat/info/adolf-tankobon-n-03-05-00602574";
        String isbn = "9788413422084";
        String nomDeLaSerie = "adolf";
        String volumDelLibre = "03";

        String pathForDownloads = File.separator + "Volumes" + File.separator + "TeraTwo" + File.separator + nomDeLaSerie + File.separator + volumDelLibre + File.separator;

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
            Set<Cookie> cookies = driver.manage().getCookies();

            // Extract working base URL from links
            String workingBaseUrl = HtmlUtils.extractHref(iframeSource, "fontface.css");
            if (workingBaseUrl.isEmpty()) {
                throw new IllegalStateException("Failed to determine the working base URL.");
            }

            String[] downloadBaseUrl = HtmlUtils.splitWorkingUrl(workingBaseUrl, "OEBPS");

            // Output the results
            String downloadingBaseUrl = "https://nubereader-epub.odilotk.es" + downloadBaseUrl[0];
            System.out.println("downloadingBaseUrl " + downloadingBaseUrl);


            // Download OPF
            // https://nubereader-epub.odilotk.es/nubereader/file/E0360/00556878/META-INF/container.xml
            String xmlContainerUrl = downloadingBaseUrl + "META-INF/container.xml";
            System.out.println("xmlContainerUrl " + xmlContainerUrl);
            String containerXmlPath = pathForDownloads + "container.xml";
            FileDownloader.downloadFileWithCookies(xmlContainerUrl, containerXmlPath, cookies);

            String fullPathValue = HtmlUtils.parseContainerXml(containerXmlPath);
            if (fullPathValue.isEmpty()) {
                throw new IllegalStateException("Failed to extract full-path attribute from container.xml.");
            }

            String opfUrl = downloadingBaseUrl + fullPathValue;
            System.out.println(opfUrl);
            String opfPath = pathForDownloads + "book.opf";
            FileDownloader.downloadFileWithCookies(opfUrl, opfPath, cookies);

            // Parse OPF file and extract page links
            List<String> pageLinks = HtmlUtils.extractPageLinks(opfPath, downloadingBaseUrl);

            // Download images
            for (int i = 0; i < pageLinks.size(); i++) {
                String imagePath = pathForDownloads + nomDeLaSerie + "_volum_" + volumDelLibre + "_page_" + i + ".jpg";
                System.out.println(pageLinks.get(i));
                driver.get(pageLinks.get(i));
                FileDownloader.downloadFileWithCookies(pageLinks.get(i), imagePath, cookies);
                Thread.sleep(1500); // Rate limiting
            }

            // Create ZIP file
            String zipFileName = pathForDownloads + nomDeLaSerie + "_volum_" + volumDelLibre + "_" + isbn + ".cbr";
            ZipCreator.createZip(pathForDownloads, zipFileName);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}