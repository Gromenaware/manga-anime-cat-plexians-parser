package cat.plexians.test;

import cat.plexians.main.BaseWebDriver;
import cat.plexians.utils.NavigationActions;
import cat.plexians.utils.ZipCreator;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.*;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ParserFromOdiloTkMangaScreenshotsTest extends BaseWebDriver {

    @Test
    public void initialTesT() throws IOException, InterruptedException {

        String baseBookUrl = "https://biblioteca.ebiblio.cat/info/bola-de-drac-super-n-21-00748865";
        String isbn = "9788411617581";
        String nomDeLaSerie = "Bola_de_Drac_Super_21";
        String volumDelLibre = "21";


        String pathForDownloads = File.separator + "Volumes" + File.separator + "TeraTwo" + File.separator + nomDeLaSerie + File.separator + volumDelLibre + File.separator;


        System.out.println("Login for eBiblio: " + EBIBLIO_LOGIN);
        System.out.println("Passwd for eBiblio: " + EBIBLIO_PASSWD);


        //Preparem els directoris
        parsingUtils.directoryCreation(pathForDownloads);

        driver.get(baseBookUrl);

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

        // Locate all iframes on the page
        List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
        System.out.println("Total iframes: " + iframes.size());

        Thread.sleep(1000);

        WebElement iframe = iframes.get(0);

        // Switch to the iframe
        driver.switchTo().frame(iframe);

        // Get the page source of the iframe
        String iframeSource = driver.getPageSource();
        System.out.println("Iframe Page Source: \n" + iframeSource);

        WebElement numeroDePaginesXpath = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div/div[1]/div[1]/span[2]"));

        System.out.println(numeroDePaginesXpath.getText());

        Thread.sleep(2000);
        // Use regular expression to extract the number
        String numberOnly = numeroDePaginesXpath.getText().replaceAll("[^0-9]", "");

        // Convert the string number to an integer
        int numeroDePagines = Integer.parseInt(numberOnly);

        // Print the result
        System.out.println("Extracted number: " + numeroDePagines);

        WebElement pages = driver.findElement(By.xpath("/html/body/div[2]/div[2]/div[3]/div/div[1]/div[1]/input[2]"));
        NavigationActions.hoverAndClick(driver, pages);
        pages.clear();
        //pages.sendKeys("1");

        NavigationActions.hoverAndClick(driver, By.xpath("/html/body/div[2]/div[2]/div[3]/div/div[1]/div[2]/div/div/button[1]"));
        Thread.sleep(2000);


        for (int i = 0; i <= numeroDePagines; i++) {
            // Locate the PageContainer element
            WebElement pageContainer;
            pageContainer = driver.findElement(By.xpath("/html/body/div[2]/div[3]/div[2]/div"));
            // Take a screenshot of the PageContainer element
            File screenshot = pageContainer.getScreenshotAs(OutputType.FILE);
            // Save the screenshot to a file
            File destination = new File(pathForDownloads + nomDeLaSerie + "_volum_" + volumDelLibre + "_pagina_" + i + ".jpg");
            FileUtils.copyFile(screenshot, destination);
            System.out.println("Pàgina guardada aquí: " + destination.getAbsolutePath());
            // Switch to the defaultContent
            driver.switchTo().defaultContent();
            Thread.sleep(1500);
            // Simulate pressing the left arrow key
            WebElement arrowLeft = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div[2]"));
            NavigationActions.hoverAndClick(driver, arrowLeft);
            System.out.println("Canvi de pàgina correcte");
            Thread.sleep(1500);
            // Switch to the iframe
            driver.switchTo().frame(iframe);
        }

        // Create ZIP file
        String zipFileName = pathForDownloads + nomDeLaSerie + "_volum_" + volumDelLibre + "_" + isbn + ".cbr";
        ZipCreator.createZip(pathForDownloads, zipFileName);
    }
}


