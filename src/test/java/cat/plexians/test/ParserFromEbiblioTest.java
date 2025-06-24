package cat.plexians.test;

import cat.plexians.main.BaseWebDriver;
import cat.plexians.utils.FileDownloader;
import cat.plexians.utils.NavigationActions;
import org.openqa.selenium.*;
import org.testng.annotations.Test;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ParserFromEbiblioTest extends BaseWebDriver {

    @Test
    public void initialTesT() throws IOException, InterruptedException {

        String nomDeLaSerie = "detectiu_conan";
        String volumDelLibre = "06";
        int numeroDePagines = 188;
        String page = "40";

        String pathForDownloads = File.separator + "Volumes" + File.separator + "TeraTwo" + File.separator + nomDeLaSerie + File.separator + volumDelLibre + File.separator;


        System.out.println("Login for eBiblio: " + EBIBLIO_LOGIN);
        System.out.println("Passwd for eBiblio: " + EBIBLIO_PASSWD);


        //Preparem els directoris
        parsingUtils.directoryCreation(pathForDownloads);

        String urlInicial = "https://biblioteca.ebiblio.cat/";

        driver.get(urlInicial);

        Thread.sleep(2000);

        //Acceptem les cookies
        NavigationActions.hoverAndClick(driver, By.xpath("/html/body/div/div[2]/div/mat-dialog-container/app-cookies-dialog/div/div[2]/div/button[2]/span[3]"));

        Thread.sleep(2000);

        //Click a login
        NavigationActions.hoverAndClick(driver, By.xpath("/html/body/app-root/div[2]/app-header/header/div[1]/opac-button[1]/button/span[2]"));

        Thread.sleep(2000);

        WebElement login = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/mat-dialog-container/app-login/div/mat-tab-group/div/mat-tab-body/div/div[1]/div[1]/form/formly-form/formly-field/formly-group/formly-field[1]/formly-group/formly-field[2]/formly-wrapper-mat-form-field/mat-form-field/div/div[1]/div/formly-field-mat-input/input"));
        WebElement passwd = driver.findElement(By.xpath("/html/body/div[1]/div[2]/div/mat-dialog-container/app-login/div/mat-tab-group/div/mat-tab-body/div/div[1]/div[1]/form/formly-form/formly-field/formly-group/formly-field[2]/app-password-input/mat-form-field/div/div[1]/div/input"));

        NavigationActions.hoverAndClick(driver, login);
        login.sendKeys(EBIBLIO_LOGIN);

        NavigationActions.hoverAndClick(driver, passwd);
        passwd.sendKeys(EBIBLIO_PASSWD);

        NavigationActions.hoverAndClick(driver, By.xpath("/html/body/div[1]/div[2]/div/mat-dialog-container/app-login/div/mat-tab-group/div/mat-tab-body/div/div[1]/div[2]/button/span[1]"));

        Thread.sleep(4000);

        driver.get("https://biblioteca.ebiblio.cat/user/checkouts");

        Thread.sleep(4000);


        NavigationActions.hoverAndClick(driver, By.xpath("/html/body/app-root/main/app-user/div/section/app-user-checkouts/div/app-record-card-grid/div/app-user-checkout-item/opac-card-item/article/div[1]/app-record-buttons/div/app-download-button/button/span[1]/span"));

        Thread.sleep(2000);

        System.out.println("Current URL: " + driver.getCurrentUrl());

        Thread.sleep(2000);


        // Get all window handles (tabs)
        Set<String> windowHandles = driver.getWindowHandles();
        ArrayList<String> tabs = new ArrayList<>(windowHandles);

        // Switch to the second tab
        driver.switchTo().window(tabs.get(1));
        System.out.println("Second Tab Title: " + driver.getTitle());
        System.out.println("Second Tab Title: " + driver.getCurrentUrl());

        /*
        //2 capes - Image i text
        for (int i = 0; i <= numeroDePagines; i++) {
            // Locate the PageContainer element
            WebElement pageContainer;
            pageContainer = driver.findElement(By.className("fixed-page-frame-center"));
            // Take a screenshot of the PageContainer element
            File screenshot = pageContainer.getScreenshotAs(OutputType.FILE);
            // Save the screenshot to a file
            File destination = new File(pathForDownloads + nomDeLaSerie + "_volum_" + volumDelLibre + "_pagina_" + i + ".jpg");
            FileUtils.copyFile(screenshot, destination);
            System.out.println("Pàgina guardada aquí: " + destination.getAbsolutePath());
            // Simulate pressing the left arrow key
            driver.findElement(By.tagName("body")).sendKeys(Keys.ARROW_LEFT);
            System.out.println("Canvi de pàgina correcte");
            Thread.sleep(1500);
        } */

        /* Old Reader */
        // Locate all iframes on the page
        List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
        System.out.println("Total iframes: " + iframes.size());

        Thread.sleep(1000);

        WebElement iframe = iframes.get(1);

        // Switch to the iframe
        driver.switchTo().frame(iframe);

        // Get the page source of the iframe
        String iframeSource = driver.getPageSource();
        System.out.println("Iframe Page Source: \n" + iframeSource);

        Thread.sleep(1000);
        // Locate the <img> element using its class name
        WebElement imgElement = driver.findElement(By.className("bgimg_page" + page + "_backgroundImage" + page));


        // Get the 'src' attribute of the <img> element
        String imgSrc = imgElement.getAttribute("src");

        // Print the 'src' value
        System.out.println("Image src: " + imgSrc);

        driver.get(imgSrc);

        Thread.sleep(2000);
        // Extract cookies from Selenium session
        Set<Cookie> cookies = driver.manage().getCookies();
        for (int i = 0; i <= numeroDePagines; i++) {
            // Construct the URL for the current page by replacing %d with the page number
            // Replace the numbers after "page" and "backgroundImage" with %d
            String transformedUrl = imgSrc.replaceAll("page\\d+", "page%d").replaceAll("backgroundImage\\d+", "backgroundImage%d");

            String imageUrl = String.format(transformedUrl, i, i);

            // Log the original and transformed URLs
            System.out.println("Generated URL: " + imageUrl);

            // Download the image with cookies
            FileDownloader.downloadFileWithCookies(imageUrl, pathForDownloads + nomDeLaSerie + "_volum_" + volumDelLibre + "_pagina_" + i + ".jpg", cookies);
            Thread.sleep(1500);
        }
    }
}


