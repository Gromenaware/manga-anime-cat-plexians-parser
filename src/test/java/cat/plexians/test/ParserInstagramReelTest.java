package cat.plexians.test;

import cat.plexians.main.BaseWebDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParserInstagramReelTest extends BaseWebDriver {

    @Test
    public void initialTest() throws IOException {
        // Replace with the Instagram reel URL
        String instagramReelUrl = "https://www.instagram.com/elnacionalcat/reel/DFFGdvZO-SB/";

        // Generate a unique file name based on the current timestamp
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String savePath = File.separator + "Volumes" + File.separator + "TeraTwo" + File.separator + "reels" + File.separator + "instagram_reel_" + timestamp + ".mp4";

        try {
            System.out.println("Opening Instagram reel...");
            driver.get(instagramReelUrl);

            // Wait for the page to load
            Thread.sleep(5000);

            // Attempt to extract the video URL
            String videoUrl = extractVideoUrl();
            if (videoUrl != null && !videoUrl.isEmpty()) {
                System.out.println("Downloading video from: " + videoUrl);
                downloadVideo(videoUrl, savePath);
                System.out.println("Video downloaded as " + savePath);
            } else {
                System.out.println("Failed to find the actual video URL.");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            driver.quit();
        }
    }

    private String extractVideoUrl() {
        try {
            // Attempt to get the video URL using the <video> tag
            WebElement videoElement = driver.findElement(By.tagName("video"));
            JavascriptExecutor js = (JavascriptExecutor) driver;
            String videoUrl = (String) js.executeScript("return arguments[0].currentSrc;", videoElement);
            System.out.println(videoUrl);

            if (videoUrl != null && !videoUrl.startsWith("blob:")) {
                System.out.println("Found video URL using currentSrc: " + videoUrl);
                return videoUrl;
            } else {
                System.out.println("Blob URL detected. Attempting fallback...");
            }

            // Fallback: Search the page source for video_url
            String pageSource = driver.getPageSource();
            int startIndex = pageSource.indexOf("\"video_url\":\"");
            if (startIndex != -1) {
                int start = startIndex + 12; // Skip "video_url":" part
                int end = pageSource.indexOf("\"", start);
                videoUrl = pageSource.substring(start, end).replace("\\u0026", "&");
                System.out.println("Found video URL in page source: " + videoUrl);
                return videoUrl;
            } else {
                System.out.println("No video_url found in page source.");
            }
        } catch (Exception e) {
            System.err.println("Error extracting video URL: " + e.getMessage());
        }
        return null;
    }

    private void downloadVideo(String videoUrl, String savePath) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(videoUrl).openStream());
             FileOutputStream fileOutputStream = new FileOutputStream(savePath)) {
            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                fileOutputStream.write(dataBuffer, 0, bytesRead);
            }
        } catch (IOException e) {
            System.err.println("Error downloading video: " + e.getMessage());
        }
    }
}