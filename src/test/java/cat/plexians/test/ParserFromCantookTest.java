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
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserFromCantookTest extends BaseWebDriver {

    @Test
    public void initialTest() throws InterruptedException, URISyntaxException {
        String loginUrl = "https://canarias.ebiblio.es/ebiblio_auth_sign_in";
        String activityUrl = "https://canarias.ebiblio.es/resources/6769151f5a0008c1d561497b";
        String isbn = "9788410305120";
        String titolDelLlibre = "mi-amor-por-yamada-esta-al-nv-999";
        String autorDelLlibre = "01_10";
        String volumDelLibre = "01_10";

        System.out.println("Login for eBiblio: " + EBIBLIO_LOGIN);
        System.out.println("Passwd for eBiblio: " + EBIBLIO_PASSWD);

        String pathForDownloads = File.separator + "Volumes" + File.separator + "02_2TB" + File.separator + "manganime" + File.separator + titolDelLlibre + File.separator + autorDelLlibre + File.separator + volumDelLibre + File.separator;


        // Prepare directories
        parsingUtils.directoryCreation(pathForDownloads);

        driver.get(loginUrl);

        WebElement login = driver.findElement(By.xpath("//*[@id='login']"));
        WebElement passwd = driver.findElement(By.xpath("//*[@id='password']"));

        NavigationActions.hoverAndClick(driver, login);
        login.sendKeys(EBIBLIO_LOGIN);

        NavigationActions.hoverAndClick(driver, passwd);
        passwd.sendKeys(EBIBLIO_PASSWD);

        Thread.sleep(45000);

        driver.get(activityUrl);
        System.out.println("Accessing URL..." + activityUrl);
        Thread.sleep(2000);
        NavigationActions.hoverAndClick(driver, By.xpath("//*[@id='drop-menu-1-controller']"));
        Thread.sleep(2000);
        WebElement readOnline = driver.findElement(By.xpath("//*[@id='drop-menu-1']/ul/li[1]/a"));

        // Captura l'atribut href
        String hrefValue = readOnline.getAttribute("href");

        // Imprimeix el valor de href
        System.out.println(hrefValue);

        driver.get(hrefValue);

        Thread.sleep(10000);

        // Find iframes and switch to the first one
        List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
        if (iframes.isEmpty()) {
            throw new IllegalStateException("No iframe found in the preview tab.");
        }
        driver.switchTo().frame(iframes.get(0));

        // Get iframe source and cookies
        String iframeSource = driver.getPageSource();

        System.out.println(iframeSource);

        // Patró per buscar l'atribut href
        String regex = "href=\"(https://r\\.cantook\\.com/_proxy/[^/]+/OEBPS/cover\\.xhtml)\"";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(iframeSource);

        if (matcher.find()) {
            String fullUrl = matcher.group(1);
            // Obtenir la part abans de OEBPS/cover.xhtml
            String baseUrl = fullUrl.substring(0, fullUrl.lastIndexOf("/OEBPS/")) + "/";
            System.out.println(baseUrl);
        } else {
            System.out.println("No s'ha trobat la URL.");
        }

        //OEBPS/nav.xhtml
    }

}