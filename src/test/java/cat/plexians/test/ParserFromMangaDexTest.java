package cat.plexians.test;

import cat.plexians.utils.*;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.Test;
import org.openqa.selenium.interactions.Actions;

import java.io.File;
import java.util.*;

public class ParserFromMangaDexTest {

    @Test
    public void initialTest() {
        String baseBookUrl = "https://mangadex.org/chapter/16ca2875-0798-4488-b88a-8286c3b49d68";
        String nomDeLaSerie = "Mazinger_Otome";
        int numberOfTotalChapters = 14;
        String pathForDownloads = File.separator + "Volumes" + File.separator + "TeraTwo" + File.separator + nomDeLaSerie + File.separator;


        // Set up ChromeDriver path
        System.setProperty("webdriver.chrome.driver", "src" + File.separator + "test" + File.separator + "resources" + File.separator + "webdriver" + File.separator + "chromedriver-macos");

        // Set desired download folder
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("download.default_directory", pathForDownloads); // Configure download folder
        prefs.put("download.prompt_for_download", false);      // Disable download prompts
        prefs.put("download.directory_upgrade", true);         // Overwrite existing files
        prefs.put("safebrowsing.enabled", true);               // Enable safe browsing
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--disable-gpu", "--window-size=1920,1200", "--ignore-certificate-errors", "--disable-extensions", "--no-sandbox", "--disable-dev-shm-usage");

        // Initialize WebDriver with options
        WebDriver driver = new ChromeDriver(options);
        JavascriptExecutor js = (JavascriptExecutor) driver;


        // Prepare directories
        ParsingUtils.directoryCreation(pathForDownloads);

        try {
            driver.get(baseBookUrl);
            System.out.println("Accessing URL..." + baseBookUrl);
            Thread.sleep(3000);

            for (int i = 1; i <= numberOfTotalChapters; i++) {

                String volumeAndChapter = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div[3]/div/div[1]/div[1]/div[2]/div[1]")).getText();

                // Split the string by ","
                // Split the string by comma
                String[] parts = volumeAndChapter.split(",");

                // Extract the numbers using further splitting
                String volPart = parts[0].trim(); // "Vol. 1"
                String chPart = parts[1].trim();  // "Ch. 1"

                // Get the numbers
                int volNumber = Integer.parseInt(volPart.split("\\.")[1].trim());
                int chNumber = Integer.parseInt(chPart.split("\\.")[1].trim());
                //int chNumber = 0; //For oneshots

                // Print the results
                System.out.println("Navigating Volume: " + volNumber);
                System.out.println("Navigating Chapter: " + chNumber);

                String numberOfPagesForChapter = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div[3]/div/div[1]/div[1]/div[2]/div[2]")).getText();

                // Split the string by "/" and trim the result
                String[] parts2 = numberOfPagesForChapter.split("/");
                String numberStr = parts2[1].trim(); // Get the second part and remove any spaces

                // Convert the string to an integer
                int totalPageNumbers = Integer.parseInt(numberStr);

                // Print the result
                System.out.println("Pages per Chapter: " + totalPageNumbers);

                //Each page management

                for (int k = 1; k <= totalPageNumbers; k++) {
                    // Find the WebElement (replace with your element's locator)
                    WebElement element = driver.findElement(By.xpath("/html/body/div[1]/div[1]/div[2]/div[3]/div/div[1]/div[2]/div[1]/div"));
                    String innerHTML = element.getAttribute("innerHTML");

                    // Parse the HTML string
                    Document document = Jsoup.parse(innerHTML);

                    // Select all <img> elements
                    Elements imgTags = document.select("img");

                    // Iterate through the <img> elements
                    for (Element img : imgTags) {
                        String style = img.attr("style"); // Get the style attribute
                        if (!style.contains("display: none;")) {
                            String src = img.attr("src"); // Get the src attribute
                            // JavaScript to fetch the blob and download it
                            js.executeScript("fetch('" + src + "')" + ".then(res => res.blob())" + ".then(blob => {" + "   const url = URL.createObjectURL(blob);" + "   const a = document.createElement('a');" + "   a.href = url;" + "   a.download = '" + nomDeLaSerie + "_vol_" + volNumber + "_ch_" + chNumber + "_page_" + k + ".jpg" + "';" + // Set the file name
                                    "   document.body.appendChild(a);" + "   a.click();" + "   a.remove();" + "   URL.revokeObjectURL(url);" + "});");
                            System.out.println("Downloaded: " + nomDeLaSerie + "_vol_" + volNumber + "_ch_" + chNumber + "_page_" + k + ".jpg");
                        }
                    }

                    // Use Actions class to send ARROW RIGHT key
                    Actions actions = new Actions(driver);
                    actions.sendKeys(Keys.ARROW_RIGHT).perform();
                    Thread.sleep(2000);
                }
            }
            // Create ZIP file
            String zipFileName = pathForDownloads + nomDeLaSerie + ".cbr";
            ZipCreator.createZip(pathForDownloads, zipFileName);

        } catch (Exception e) {
            e.printStackTrace();
        }

        driver.quit();
    }
}