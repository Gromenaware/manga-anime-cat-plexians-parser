package cat.plexians.test;

import cat.plexians.main.BaseWebDriver;
import cat.plexians.utils.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ParserFromCantookThoriumReaderTest extends BaseWebDriver {

    @Test
    public void initialTest() throws InterruptedException, URISyntaxException, IOException {
        String loginUrl = "https://larioja.ebiblio.es/ebiblio_auth_sign_in";
        String activityUrl = "https://larioja.ebiblio.es/my_profile/activity";
        String isbn = "9788491733010";
        String nomDeLaSerie = "Bola_de_Drac_Color_Origen_i_Cinta_Vermella_03_08";
        String volumDelLibre = "03";

        System.out.println("Login for eBiblio: " + EBIBLIO_LOGIN);
        System.out.println("Passwd for eBiblio: " + EBIBLIO_PASSWD);

        String pathForDownloads = File.separator + "Volumes" + File.separator + "TeraTwo" + File.separator + nomDeLaSerie + File.separator + volumDelLibre + File.separator;

        // Prepare directories
        parsingUtils.directoryCreation(pathForDownloads);

        driver.get(loginUrl);

        WebElement login = driver.findElement(By.xpath("//*[@id='login']"));
        WebElement passwd = driver.findElement(By.xpath("//*[@id='password']"));

        NavigationActions.hoverAndClick(driver, login);
        login.sendKeys(EBIBLIO_LOGIN);

        NavigationActions.hoverAndClick(driver, passwd);
        passwd.sendKeys(EBIBLIO_PASSWD);

        NavigationActions.hoverAndClick(driver, By.xpath("/html/body/div/div[2]/div/div[2]/form/p[2]/button"));

        Thread.sleep(4000);

        driver.get(activityUrl);
        System.out.println("Accessing Activity URL..." + activityUrl);
        Thread.sleep(2000);

        Set<Cookie> cookies = driver.manage().getCookies();

        WebElement thoriumReader = driver.findElement(By.xpath("/html/body/div[2]/div/div/div/div/div[2]/article[1]/section/ul/li/article/div[2]/div/div[3]/div/ul/li[2]/a"));

        String urlEpubZip = thoriumReader.getAttribute("href");

        System.out.println("URL to download lcpl " + urlEpubZip);
        FileDownloader.downloadFileWithCookies(urlEpubZip, pathForDownloads + nomDeLaSerie + "_volum_" + volumDelLibre + "_" + isbn + ".lcpl", cookies);

        // Read the content of the file into a String
        String jsonString = Files.readString(Path.of(pathForDownloads + nomDeLaSerie + "_volum_" + volumDelLibre + "_" + isbn + ".lcpl"));
        FileDownloader.downloadFileWithCookies(UrlUtils.extractPublicationLink(jsonString), pathForDownloads + nomDeLaSerie + "_volum_" + volumDelLibre + "_" + isbn + ".epub", cookies);

        NavigationActions.hoverAndClick(driver, By.xpath("/html/body/div[2]/div/div/div/div/div[2]/article[1]/section/ul/li/article/div[2]/div/div[3]/div/ul/li[1]/a"));
        Thread.sleep(2000);

        // Switch to second tab
        Set<String> windowHandles = driver.getWindowHandles();
        ArrayList<String> tabs = new ArrayList<>(windowHandles);
        if (tabs.size() < 2) {
            throw new IllegalStateException("Preview tab did not open.");
        }
        driver.switchTo().window(tabs.get(1));
        Thread.sleep(5000);

        // Find iframes and switch to the first one
        List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
        if (iframes.isEmpty()) {
            throw new IllegalStateException("No iframe found in the preview tab.");
        }
        driver.switchTo().frame(iframes.get(0));

        // Get iframe source and cookies
        String iframeSource = driver.getPageSource();

        System.out.println(driver.getCurrentUrl());

        String imageSrc = HtmlUtils.parseImageTagUrl(iframeSource, driver.getCurrentUrl());

        Thread.sleep(3000);

        String nav = UrlUtils.urlTransformer(imageSrc);

        driver.get(nav);
        Thread.sleep(1500);


        String navSource = driver.getPageSource();

        // Path to save the HTML file
        String filePath = pathForDownloads + nomDeLaSerie + "_volum_" + volumDelLibre + "_" + isbn + "_nav.html";

        // Save the HTML content to the file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write(navSource);
            System.out.println("HTML content saved to " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to file: " + e.getMessage());
        }

        // Load the HTML or URL
        driver.get("file:///" + pathForDownloads + nomDeLaSerie + "_volum_" + volumDelLibre + "_" + isbn + "_nav.html");

        // Use XPath to select the <li> elements in /html/body/nav[3]/ol/li
        List<WebElement> liElements = driver.findElements(By.xpath("/html/body/nav[3]/ol/li"));

        // Count the number of <li> elements
        int numberOfPages = liElements.size();

        // Print the count
        System.out.println("Number of <li> elements: " + numberOfPages);

        List<String> pageLinks = HtmlUtils.createPageLinksFromNav(imageSrc, numberOfPages);

        // Download images
        for (int i = 0; i < pageLinks.size(); i++) {
            String imagePath = pathForDownloads + nomDeLaSerie + "_volum_" + volumDelLibre + "_page_" + i + ".jpg";
            System.out.println(pageLinks.get(i));
            driver.get(pageLinks.get(i));
            cookies = driver.manage().getCookies();
            Thread.sleep(1500); // Rate limiting
            FileDownloader.downloadFileWithCookies(pageLinks.get(i), imagePath, cookies);
            Thread.sleep(1500); // Rate limiting
        }
        // Create ZIP file
        String zipFileName = pathForDownloads + nomDeLaSerie + "_volum_" + volumDelLibre + "_" + isbn + ".cbr";
        ZipCreator.createZip(pathForDownloads, zipFileName);
    }
}