package cat.plexians.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class UrlUtils {

    public static boolean isUrlReachable(String url) {
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
            connection.setRequestMethod("HEAD");
            connection.setConnectTimeout(5000); // 5 seconds timeout
            connection.setReadTimeout(5000);
            int responseCode = connection.getResponseCode();
            return responseCode >= 200 && responseCode < 400; // Check for success codes
        } catch (Exception e) {
            return false; // URL is not reachable
        }
    }

    public static boolean checkForRedirection(String url) throws IOException {
        URL urlObj = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) urlObj.openConnection();
        connection.setInstanceFollowRedirects(false); // Disable automatic redirection following
        connection.setRequestMethod("GET");
        connection.connect();

        int responseCode = connection.getResponseCode();
        System.out.println("Response Code: " + responseCode);

        if (responseCode >= 300 && responseCode < 400) {
            // URL has a redirection
            String location = connection.getHeaderField("Location");
            System.out.println("URL is redirected to: " + location);
            return true;
        } else if (responseCode == HttpURLConnection.HTTP_OK) {
            // URL is not redirected
            System.out.println("URL is not redirected. Final URL: " + url);
            return false;
        } else {
            // Other HTTP status
            System.out.println("Received response code: " + responseCode);
        }
        connection.disconnect();
        return false;
    }

    public static String urlTransformer(String originalUrl) throws URISyntaxException {
        // Parse the original URL
        URI uri = new URI(originalUrl);

        // Extract the path
        String path = uri.getPath();

        // Replace the last segment of the path with "nav.xhtml"
        String newPath = path.replace("images/bgimg-page0-backgroundImage0.jpg", "nav.xhtml");

        // Construct the new URI
        URI newUri = new URI(uri.getScheme(), uri.getAuthority(), newPath, uri.getQuery(), uri.getFragment());

        // Output the new URL
        System.out.println("Transformed URL: " + newUri.toString());

        return newUri.toString();
    }

    public static String extractPublicationLink(String jsonString) {
        String publicationHref = "";
        try {
            // Parse the JSON
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode root = objectMapper.readTree(jsonString);

            // Navigate to the "links" array
            JsonNode links = root.get("links");

            // Find the object with "rel": "publication"
            if (links.isArray()) {
                for (JsonNode link : links) {
                    if ("publication".equals(link.get("rel").asText())) {
                        publicationHref = link.get("href").asText();
                        System.out.println("Publication URL: " + publicationHref);
                        break;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return publicationHref;
    }
}
