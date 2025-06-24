package cat.plexians.utils;

import org.openqa.selenium.Cookie;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class FileDownloader {

    /**
     * Downloads an image from the given URL and saves it to the specified file path.
     *
     * @param imageUrl The URL of the image to download.
     * @param savePath The local file path to save the image.
     */
    public static void downloadFile(String imageUrl, String savePath) {
        try (BufferedInputStream in = new BufferedInputStream(new URL(imageUrl).openStream()); FileOutputStream out = new FileOutputStream(savePath)) {

            byte[] dataBuffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                out.write(dataBuffer, 0, bytesRead);
            }
            System.out.println("File downloaded to: " + savePath);

        } catch (IOException e) {
            System.err.println("Failed to download the image: " + e.getMessage());
        }
    }

    /**
     * Downloads an image from the given URL, including cookies for authentication, and saves it to the specified file path.
     *
     * @param imageUrl The URL of the image to download.
     * @param savePath The local file path to save the image.
     * @param cookies  The cookies to include in the request.
     */
    public static void downloadFileWithCookies(String imageUrl, String savePath, Set<Cookie> cookies) {
        int maxRetries = 3; // Number of retries in case of 500 errors
        int attempt = 0;
        boolean success = false;

        while (attempt < maxRetries && !success) {
            attempt++;
            try {
                URL url = new URL(imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                // Set timeouts
                connection.setConnectTimeout(5000); // 5 seconds
                connection.setReadTimeout(5000);    // 5 seconds

                // Add headers
                StringBuilder cookieHeader = new StringBuilder();
                for (Cookie cookie : cookies) {
                    cookieHeader.append(cookie.getName()).append("=").append(cookie.getValue()).append("; ");
                }
                // Remove the trailing "; " if present
                if (cookieHeader.length() > 0) {
                    cookieHeader.setLength(cookieHeader.length() - 2);
                }

                connection.setRequestProperty("Cookie", cookieHeader.toString());
                connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
                connection.setRequestProperty("Accept", "*/*");

                // Log the request headers for debugging
                System.out.println("Attempt " + attempt + ": Sending request to " + imageUrl);
                System.out.println("Cookies: " + cookieHeader);

                // Connect and check response code
                connection.connect();
                int responseCode = connection.getResponseCode();

                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Download the file
                    try (BufferedInputStream in = new BufferedInputStream(connection.getInputStream()); FileOutputStream out = new FileOutputStream(savePath)) {

                        byte[] dataBuffer = new byte[1024];
                        int bytesRead;
                        while ((bytesRead = in.read(dataBuffer, 0, 1024)) != -1) {
                            out.write(dataBuffer, 0, bytesRead);
                        }
                        System.out.println("File downloaded to: " + savePath);
                        success = true;
                    }
                } else if (responseCode >= 500 && responseCode < 600) {
                    System.err.println("Server error (HTTP " + responseCode + ") on attempt " + attempt + ". Retrying...");
                } else {
                    throw new IOException("Server returned HTTP response code: " + responseCode + " for URL: " + imageUrl);
                }

            } catch (IOException e) {
                System.err.println("Attempt " + attempt + " failed: " + e.getMessage());
                if (attempt >= maxRetries) {
                    System.err.println("Max retries reached. Failed to download the file.");
                }
            }
        }

        if (!success) {
            System.err.println("Failed to download the file after " + maxRetries + " attempts.");
        }
    }

    public static void simpleDownloadFileWithCookies(String fileUrl, String outputFile, Set<Cookie> cookies) throws IOException {
        // Open a connection to the URL
        URL url = new URL(fileUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();

        // Set request method and headers
        connection.setRequestMethod("GET");
        // Set timeouts
        connection.setConnectTimeout(5000); // 5 seconds
        connection.setReadTimeout(5000);   // 5 seconds
        //connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/91.0.4472.124 Safari/537.36");
        //connection.setRequestProperty("Accept", "*/*");
        connection.setRequestProperty("Cookie", cookies.toString());

        // Check response code
        int responseCode = connection.getResponseCode();
        if (responseCode != HttpURLConnection.HTTP_OK) {
            throw new IOException("Failed to download file: HTTP " + responseCode);
        }

        // Read the input stream and write to a file
        try (InputStream inputStream = connection.getInputStream(); FileOutputStream outputStream = new FileOutputStream(outputFile)) {

            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        // Close the connection
        connection.disconnect();
    }

    /**
     * Writes a given string into an XML file.
     *
     * @param content The XML content as a string.
     * @param filePath   The path to the output XML file.
     * @throws IOException If an I/O error occurs.
     */

    public static void writeStringToFile(String content, String filePath) throws IOException {

        // Create a File object
        File file = new File(filePath);

        // Create parent directories if they don't exist
        File parentDir = file.getParentFile();
        if (parentDir != null && !parentDir.exists()) {
            parentDir.mkdirs();
        }

        // Write the string content to the file
        try (FileWriter writer = new FileWriter(file)) {
            writer.write(content);
        }
    }

    // Parse the OPF file and extract the list of resources (href values)
    public static Map<String, String> parseOPF(String opfFilePath) throws Exception {
        Map<String, String> resources = new HashMap<>();

        // Parse the OPF XML
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(opfFilePath));

        // Find all <item> elements in the <manifest>
        NodeList itemList = doc.getElementsByTagName("item");
        for (int i = 0; i < itemList.getLength(); i++) {
            Element item = (Element) itemList.item(i);
            String id = item.getAttribute("id");
            String href = item.getAttribute("href");
            System.out.println(href);
            resources.put(id, href);
        }

        return resources;
    }

    // Parse the .pof file into a map structure
    public static Map<String, Object> parseOPFFolders(String filePath) throws IOException, ParserConfigurationException, SAXException {
        Map<String, Object> structure = new HashMap<>();
        List<String> folders = new ArrayList<>();
        List<Map<String, String>> files = new ArrayList<>();

        // Validate the file path
        if (filePath == null || filePath.trim().isEmpty()) {
            throw new IllegalArgumentException("The file path cannot be null or empty.");
        }

        File opfFile = new File(filePath);
        if (!opfFile.exists()) {
            throw new IllegalArgumentException("The file does not exist: " + filePath);
        }

        // Parse the XML file
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(opfFile);

        doc.getDocumentElement().normalize();

        // Extract manifest items
        NodeList manifestItems = doc.getElementsByTagName("item");
        for (int i = 0; i < manifestItems.getLength(); i++) {
            Element item = (Element) manifestItems.item(i);

            String id = item.getAttribute("id");
            String href = item.getAttribute("href");
            String mediaType = item.getAttribute("media-type");

            // Extract folder from href
            String folder = href.contains("/") ? href.substring(0, href.lastIndexOf('/')) : "";
            if (!folder.isEmpty() && !folders.contains(folder)) {
                folders.add(folder);
            }

            // Add file details
            Map<String, String> fileDetails = new HashMap<>();
            fileDetails.put("id", id);
            fileDetails.put("href", href);
            fileDetails.put("media-type", mediaType);
            files.add(fileDetails);
        }

        structure.put("folders", folders);
        structure.put("files", files);
        return structure;
    }

    public static void createFolders(String basePath, List<String> folders) {
        try {
            for (String folder : folders) {
                // Resolve the full path
                Path fullPath = Paths.get(basePath, folder);

                // Create the directory
                Files.createDirectories(fullPath);
                System.out.println("Created directory: " + fullPath);
            }
        } catch (IOException e) {
            System.err.println("Failed to create directories: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
