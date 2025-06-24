package cat.plexians.utils;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HtmlUtils {
    public static String[] splitWorkingUrl(String input, String splitPoint) {
        int index = input.indexOf(splitPoint); // Find the index of the split point
        if (index == -1) {
            // If splitPoint is not found, return the input as part 1 and an empty string as part 2
            return new String[]{input, ""};
        }

        // Split the string into two parts
        String part1 = input.substring(0, index); // Everything before "OEBPS"
        String part2 = input.substring(index);   // Everything starting from "OEBPS"

        return new String[]{part1, part2};
    }

    public static String extractHref(String htmlContent, String partialMatch) {
        try {
            // Parse the HTML content
            Document doc = Jsoup.parse(htmlContent);

            // Select all elements with href attributes
            Elements hrefElements = doc.select("[href]");

            // Iterate through the elements to find the one matching the condition
            for (Element element : hrefElements) {
                String href = element.attr("href");
                if (href.contains(partialMatch)) { // Check if href contains the partialMatch
                    return href;
                }
            }

            // If no match is found, return an empty string
            return "";
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    public static String parseContainerXml(String containerXmlPath) {
        try (Scanner scanner = new Scanner(new File(containerXmlPath))) {
            StringBuilder xmlContent = new StringBuilder();
            while (scanner.hasNextLine()) {
                xmlContent.append(scanner.nextLine()).append(System.lineSeparator());
            }

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            org.w3c.dom.Document document = builder.parse(new ByteArrayInputStream(xmlContent.toString().getBytes()));

            NodeList rootfileNodes = document.getElementsByTagNameNS("urn:oasis:names:tc:opendocument:xmlns:container", "rootfile");
            if (rootfileNodes.getLength() > 0) {
                org.w3c.dom.Element rootfileElement = (org.w3c.dom.Element) rootfileNodes.item(0);
                return rootfileElement.getAttribute("full-path");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static List<String> extractPageLinks(String opfPath, String baseUrl) {
        List<String> pageLinks = new ArrayList<>();
        try {
            File opfFile = new File(opfPath);
            Document doc = Jsoup.parse(opfFile, "UTF-8");
            Elements links = doc.select("[href]");
            for (Element link : links) {
                if (link.attr("href").contains("backgroundImage")) {
                    pageLinks.add(baseUrl + "OEBPS" + File.separator + link.attr("href"));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pageLinks;
    }


    public static List<String> extractPageXHTMLLinks(String opfPath, String downloadUrl) throws IOException {
// Load and parse the OPF file
        File xmlFile = new File(opfPath); // Change to your file path
        Document doc = Jsoup.parse(xmlFile, "UTF-8");

        // Select all <item> elements inside <manifest>
        Elements items = doc.select("item");

        // List to store extracted href values
        List<String> hrefList = new ArrayList<>();

        // Iterate through each <item> element
        for (Element item : items) {
            String mediaType = item.attr("media-type");
            if ("application/xhtml+xml".equals(mediaType)) {
                hrefList.add(downloadUrl + item.attr("href")); // Extract the href attribute
            }
        }

        // Print extracted href values
        System.out.println("Extracted href values:");
        for (String href : hrefList) {
            System.out.println(href);
        }
        return hrefList;
    }

    public static List<String> extractPageBgImgLinks(String opfPath, String downloadUrl) throws IOException {

        // Load and parse the OPF file
        File xmlFile = new File(opfPath); // Update the file path
        Document doc = Jsoup.parse(xmlFile, "UTF-8");

        // Select all <item> elements inside <manifest>
        Elements items = doc.select("item");

        // List to store extracted href values
        List<String> hrefList = new ArrayList<>();

        // Iterate through each <item> element
        for (Element item : items) {
            String mediaType = item.attr("media-type");
            if ("image/jpeg".equals(mediaType)) {
                hrefList.add(downloadUrl + item.attr("href")); // Extract the href attribute
            }
        }

        // Print extracted href values
        System.out.println("Extracted href values:");
        for (String href : hrefList) {
            System.out.println(href);
        }

        return hrefList;
    }

    public static String parseImageTagUrl(String html, String baseUrl) {      // Parse the HTML
        Document document = Jsoup.parse(html, baseUrl);

        // Find the img tag
        Element imgTag = document.select("img").first();
        String fullUrl = "";

        // Extract and resolve the full URL
        if (imgTag != null) {
            fullUrl = imgTag.absUrl("src"); // Resolve the full URL
            System.out.println("Full Image URL: " + fullUrl);
        } else {
            System.out.println("No img tag found!");
        }
        return fullUrl;
    }

    public static String parseImageTag(String html) {
        // Parse the HTML
        Document document = Jsoup.parse(html);
        String src = "";
        // Find the img tag
        Element imgTag = document.select("img").first();

        // Extract attributes
        if (imgTag != null) {
            String alt = imgTag.attr("alt");
            src = imgTag.attr("src");
            String imgClass = imgTag.attr("class");
            String draggable = imgTag.attr("draggable");
            String fetchPriority = imgTag.attr("fetchpriority");

            // Print the attributes
            System.out.println("Image Attributes:");
            System.out.println("Alt: " + alt);
            System.out.println("Src: " + src);
            System.out.println("Class: " + imgClass);
            System.out.println("Draggable: " + draggable);
            System.out.println("Fetch Priority: " + fetchPriority);
        } else {
            System.out.println("No img tag found!");
        }
        return src;
    }

    public static int navPageCounter(String html) {
        // Parse the HTML
        Document document = Jsoup.parse(html);
        int pageCount = 0;

        // Select the page list <nav> section with epub:type="page-list"
        Element pageListNav = document.selectFirst("nav[epub|type=page-list]");

        // Count the <a> elements inside the page list
        if (pageListNav != null) {
            Elements pageLinks = pageListNav.select("a");
            pageCount = pageLinks.size(); // Count all <a> tags
        } else {
            System.out.println("Page list not found in the document.");
        }

        return pageCount;
    }

    public static List<String> createPageLinksFromNav(String baseImageUrl, int numberOfPages) {
        List<String> pageLinks = new ArrayList<>();

        for (int i = 0; i < numberOfPages; i++) {
            // Replace "0" with the current page number in the URL
            String updatedUrl = baseImageUrl.replace("page0", "page" + i).replace("backgroundImage0", "backgroundImage" + i);
            pageLinks.add(updatedUrl);
        }
        return pageLinks;
    }

    public static List<String> createPageXhtmlLinksFromNav(String baseImageUrl, int numberOfPages) {
        List<String> pageLinks = new ArrayList<>();

        for (int i = 0; i < numberOfPages; i++) {
            // Replace "0" with the current page number in the URL
            String updatedUrl = baseImageUrl.replace("page0", "page" + i).replace("backgroundImage0", "backgroundImage" + i);

            // Print the updated URL
            System.out.println(updatedUrl);
            pageLinks.add(updatedUrl);
        }
        return pageLinks;
    }


    public static int countLiTags(String html) {
        // Parse the HTML
        Document document = Jsoup.parse(html);

        // Select all <li> tags
        Elements liTags = document.select("li");

        // Return the count of <li> tags
        return liTags.size();
    }

    public static int pageListCounter(String html) {
        // Parse the HTML
        Document document = Jsoup.parse(html);

        // Select the <nav> element with epub:type="page-list"
        Element pageListNav = document.selectFirst("nav[epub\\:type=page-list]");

        // Count the <li> elements within it
        if (pageListNav != null) {
            Elements listItems = pageListNav.select("ol > li");
            System.out.println("Number of pages in page-list: " + listItems.size());
        } else {
            System.out.println("page-list not found in the document.");
        }
        return 0;
    }
}
