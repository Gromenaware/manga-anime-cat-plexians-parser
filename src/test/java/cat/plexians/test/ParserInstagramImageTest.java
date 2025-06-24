package cat.plexians.test;

import cat.plexians.main.BaseWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ParserInstagramImageTest extends BaseWebDriver {

    @Test
    public void initialTesT() throws IOException {

        // Replace with the Instagram post URL
        String instagramUrl = "https://www.instagram.com/p/DAtWRkzIkvD/";

        // Directory to save images
        // Generate a unique file name based on the current timestamp
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String saveDir = File.separator + "Volumes" + File.separator + "TeraTwo" + File.separator + "instagram_images_" + timestamp;
        new java.io.File(saveDir).mkdirs();


        try {
            System.out.println("Opening Instagram post...");
            driver.get(instagramUrl);

            // Allow time for JavaScript to load
            Thread.sleep(5000);

            // Find all image elements
            List<WebElement> images = driver.findElements(By.tagName("img"));

            System.out.println("Downloading images...");
            int count = 1;
            for (WebElement img : images) {
                String imgUrl = img.getAttribute("src");
                if (imgUrl != null && !imgUrl.isEmpty()) {
                    String fileName = saveDir + "/image_" + count + ".jpg";
                    downloadImage(imgUrl, fileName);
                    System.out.println("Downloaded: " + fileName);
                    count++;
                }
            }

            System.out.println("All images downloaded to " + saveDir + ".");

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private static void downloadImage(String imgUrl, String savePath) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(imgUrl).openStream()); FileOutputStream fileOutputStream = new FileOutputStream(savePath)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            System.err.println("Error downloading image: " + e.getMessage());
        }
    }
}
