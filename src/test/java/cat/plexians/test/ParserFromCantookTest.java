package cat.plexians.test;

import cat.plexians.main.BaseWebDriver;
import cat.plexians.utils.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class ParserFromCantookTest extends BaseWebDriver {

    @Test
    public void initialTest() throws InterruptedException, URISyntaxException {
        String loginUrl = "https://canarias.ebiblio.es/ebiblio_auth_sign_in";
        String activityUrl = "https://canarias.ebiblio.es/resources/67482daed7f5786f0e82a128";
        String isbn = "9788491530862";
        String nomDeLaSerie = "dr_slump";
        String volumDelLibre = "14";

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

        Thread.sleep(2000);

        driver.get(activityUrl);
        System.out.println("Accessing URL..." + activityUrl);
        Thread.sleep(2000);
        NavigationActions.hoverAndClick(driver, By.xpath("/html/body/div[2]/div/div/div/div/div[3]/div/div/div[1]/aside/div/button"));
        Thread.sleep(2000);
        NavigationActions.hoverAndClick(driver, By.xpath("/html/body/div[2]/div/div/div/div/div[3]/div/div/div[1]/aside/div/div/ul/li[1]/a"));
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
        Set<Cookie> cookies = driver.manage().getCookies();

        System.out.println(driver.getCurrentUrl());

        String imageSrc = HtmlUtils.parseImageTagUrl(iframeSource, driver.getCurrentUrl());

        System.out.println(imageSrc);


        Thread.sleep(3000);

        String nav = UrlUtils.urlTransformer(imageSrc);

        driver.get(nav);

        Thread.sleep(3000);

        String navPageSource = driver.getPageSource();

        System.out.println(navPageSource);

        int numberOfPages = HtmlUtils.countLiTags(navPageSource);

        System.out.println(numberOfPages);

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