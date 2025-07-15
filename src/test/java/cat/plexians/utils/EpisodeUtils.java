package cat.plexians.utils;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EpisodeUtils {

    public static List<Episode> parseFile(String filename) throws IOException {
        List<Episode> episodes = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            if (line.trim().isEmpty()) {
                if (sb.length() > 0) {
                    episodes.add(parse(sb.toString()));
                    sb.setLength(0);
                }
            } else {
                sb.append(line).append("\n");
            }
        }
        if (sb.length() > 0) {
            episodes.add(parse(sb.toString()));
        }
        reader.close();
        return episodes;
    }

    public static Episode parse(String input) {
        //String title = extract(input, "^(.*) \\d+ \\(.*\\)$");
        String title = extract(input, "El detectiu Conan (\\d+) \\((\\d{4}-\\d{2}-\\d{2})\\)");
        String releaseDate = extract(input, "\\((.*?)\\)");
        String infoUrl = extract(input, "Info: (.*)");
        String episodeTitle = extract(input, "Titol: (.*)");
        String mediumQualityUrl = extract(input, "MQ: (.*)");
        String highQualityUrl = extract(input, "HQ: (.*)");
        String subtitlesUrl1 = extract(input, "Subs1: (.*)");
        String subtitlesUrl2 = extract(input, "Subs2: (.*)");

        return new Episode(title, releaseDate, infoUrl, episodeTitle, mediumQualityUrl, highQualityUrl, subtitlesUrl1, subtitlesUrl2);
    }

    public static String extract(String input, String regex) {
        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(input);
        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }

    public void downloadEpisodeFromTextFile(String nomDeLaSerie, String pathForDownloads, Episode e, ParsingUtils parsingUtils) throws IOException {
        //Descarrega en la carpeta corresponent
        URL website = new URL(e.getHighQualityUrl().toString());
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());

        //Preparar el nom del fitxer
        String fileNameDownloaded = parsingUtils.prepareParsingTitle(nomDeLaSerie, e);

        System.out.println(pathForDownloads + fileNameDownloaded);

        //Copiar el nom de la descarrega
        FileOutputStream fos = new FileOutputStream(pathForDownloads + fileNameDownloaded);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }

    public ArrayList<String> captureURLFromEpisodes(WebDriver driver) throws InterruptedException {

        //Scroll down per assegurar-nos que tenim tots els fitxers disponibles
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        Thread.sleep(3000);
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        Thread.sleep(3000);
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        Thread.sleep(3000);
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        Thread.sleep(3000);
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");
        Thread.sleep(3000);
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0, document.body.scrollHeight)");


        //Captura les urls dels episodis després de l'scroll
        List<WebElement> list = driver.findElements(By.xpath("//*[@href]"));
        ArrayList<String> episodeList = new ArrayList<>();
        for (WebElement e : list) {
            String link = e.getAttribute("href");
            if (link.contains("3cat") && link.contains("video") && !link.contains("js")) {
                if (!episodeList.contains(link)) {
                    // Inserta els episodis de manera única
                    episodeList.add(link);
                }
            }
        }
        return episodeList;
    }

    public void downloadEpisodeFrom3Cat(WebDriver driver, String nomDeLaSerie, String e, String pathForDownloads, ParsingUtils parsingUtils) throws IOException {
        //Prepara les urls per descarregar
        String[] arrOfStr = e.split("/");
        String idVideo = arrOfStr[arrOfStr.length - 1];
        String videoTitle = arrOfStr[arrOfStr.length - 3];
        String urlDinamics = "https://dinamics.ccma.cat/pvideo/media.jsp?media=video&version=0s&profile=tv&idint=" + idVideo;
        driver.get(urlDinamics);

        //Aconsegueix el mp4 high quality
        String jsonString = driver.getPageSource();
        String[] stringUrlHigh = jsonString.split("mp4-down-high-es.3catvideos.cat");
        String splitSecondSplit = stringUrlHigh[stringUrlHigh.length - 1];
        String[] urlDownload = splitSecondSplit.split("\"");
        String urlDescarrega = "https://mp4-down-high-es.3catvideos.cat" + urlDownload[0];
        System.out.println("Títol: " + videoTitle + "\n Id del video: " + idVideo + "\n Dinamics url: " + urlDinamics + "\n URL descarrega High: " + urlDescarrega + "\n");

        //Descarrega i guarda
        URL website = new URL(urlDescarrega);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        String fileDownloaded = parsingUtils.cleanStringFromSpecialCharactersMp4(nomDeLaSerie + "_" + videoTitle + "_" + idVideo);
        System.out.println("Fitxer final: " + pathForDownloads + fileDownloaded);
        FileOutputStream fos = new FileOutputStream(pathForDownloads + fileDownloaded);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }

    public static String extractJsonFromHtml(String htmlString) {
        int preStart = htmlString.indexOf("<pre>");
        int preEnd = htmlString.indexOf("</pre>");
        if (preStart == -1 || preEnd == -1) throw new IllegalArgumentException("No s'ha trobat el <pre> al HTML!");
        return htmlString.substring(preStart + 5, preEnd);
    }


    public void downloadEpisodeFromJson(String jsonString, String nomDeLaSerie, String pathForDownloads, ParsingUtils parsingUtils) throws IOException {
        JSONObject obj = new JSONObject(jsonString);

        String videoTitle = obj.getJSONObject("informacio").getString("slug");
        String idVideo = String.valueOf(obj.getJSONObject("informacio").getInt("id"));

        // Troba la qualitat més alta
        JSONArray urls = obj.getJSONObject("media").getJSONArray("url");
        String urlDescarrega = null;
        int maxQuality = 0;
        for (int i = 0; i < urls.length(); i++) {
            JSONObject urlObj = urls.getJSONObject(i);
            String label = urlObj.getString("label"); // Ex: "720p"
            int quality = Integer.parseInt(label.replace("p", ""));
            if (quality > maxQuality) {
                maxQuality = quality;
                urlDescarrega = urlObj.getString("file");
            }
        }

        System.out.println("Títol: " + videoTitle + "\n Id del video: " + idVideo + "\n URL descarrega: " + urlDescarrega + "\nQualitat: " + maxQuality + "p");

        URL website = new URL(urlDescarrega);
        ReadableByteChannel rbc = Channels.newChannel(website.openStream());
        String fileDownloaded = parsingUtils.cleanStringFromSpecialCharactersMp4(nomDeLaSerie + "_" + videoTitle + "_" + idVideo + ".mp4");
        System.out.println("Fitxer final: " + pathForDownloads + fileDownloaded);
        FileOutputStream fos = new FileOutputStream(pathForDownloads + fileDownloaded);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }


    public VideoInfo getUrlFromEpisodeFrom3Cat(WebDriver driver, String e) throws IOException {
        // Prepara les urls per descarregar
        String[] arrOfStr = e.split("/");
        String idVideo = arrOfStr[arrOfStr.length - 1];
        String videoTitle = arrOfStr[arrOfStr.length - 3];
        String urlDinamics = "https://dinamics.ccma.cat/pvideo/media.jsp?media=video&version=0s&profile=tv&idint=" + idVideo;
        driver.get(urlDinamics);

        // Aconsegueix el mp4 high quality
        String jsonString = driver.getPageSource();
        String[] stringUrlHigh = jsonString.split("mp4-down-high-es.3catvideos.cat");
        String splitSecondSplit = stringUrlHigh[stringUrlHigh.length - 1];
        String[] urlDownload = splitSecondSplit.split("\"");
        String urlDescarrega = "https://mp4-down-high-es.3catvideos.cat" + urlDownload[0];

        // Print the details
        System.out.println("Títol: " + videoTitle + "\nId del video: " + idVideo + "\nDinamics url: " + urlDinamics + "\nURL descarrega High: " + urlDescarrega + "\n");

        // Return a VideoInfo object
        return new VideoInfo(videoTitle, idVideo, urlDinamics, urlDescarrega);
    }


    public void downloadEpisodeFromJsonFile(String nomDeLaSerie, String pathForDownloads, Episode e, ParsingUtils parsingUtils) throws IOException, InterruptedException {
        //Descarrega en la carpeta corresponent
        URL website = new URL(e.getHighQualityUrl().toString());
        ReadableByteChannel rbc = null;
        int code;
        HttpURLConnection connection = (HttpURLConnection) website.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        code = connection.getResponseCode();
        System.out.println("Code " + code + " from URL: " + e.getHighQualityUrl().toString());
        connection.disconnect();

        if (code == 403) {
            Thread.sleep(5000);
            connection.connect();
            code = connection.getResponseCode();
            System.out.println("2nd Attempt - Code " + code + " from URL: " + e.getHighQualityUrl().toString());
        }

        try {
            rbc = Channels.newChannel(website.openStream());
        } catch (IOException io) {
            Thread.sleep(3000);
            rbc = Channels.newChannel(website.openStream());
        }

        //Preparar el nom del fitxer
        String fileNameDownloaded = parsingUtils.prepareParsingFromJsonTitle(e);

        System.out.println(pathForDownloads + fileNameDownloaded);

        //Copiar el nom de la descarrega
        FileOutputStream fos = new FileOutputStream(pathForDownloads + fileNameDownloaded);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }

    public void extractedEpisode(Video video, Url url, List<Episode> episodes) {
        System.out.println("Titol: " + video.getInformacio().getTitol());
        System.out.println("Data Emissió: " + video.getInformacio().getDataEmissio());
        System.out.println("Slug: " + video.getInformacio().getSlug());
        System.out.println("Titol Complet: " + video.getInformacio().getTitolComplet());
        System.out.println("URL: " + url.getFile());
        episodes.add(new Episode(video.getInformacio().getTitol(), video.getInformacio().getDataEmissio(), "", video.getInformacio().getTitolComplet(), url.getFile(), url.getFile(), "", ""));
    }

    public void downloadMangaEpisodePageFromURL(String url, String pathForDownloads, MangaEpisode e) throws IOException, InterruptedException {
        //Descarrega en la carpeta corresponent
        // Extract the file name from the URL
        String fileName = url.substring(url.lastIndexOf('/') + 1);

        int dotIndex = fileName.lastIndexOf('.'); // Encuentra la posición del punto
        if (dotIndex != -1) {
            String nameWithoutExtension = fileName.substring(0, dotIndex); // Nombre sin extensión
            nameWithoutExtension = nameWithoutExtension.replaceAll(",", "_");
            String extension = fileName.substring(dotIndex); // Extensión del archivo

            // Nuevo nombre de archivo con el fansub añadido
            fileName = nameWithoutExtension + "_" + e.getFansub() + extension;


        } else {
            System.out.println("El archivo no tiene extensión.");
        }

        URL website = new URL(url);
        ReadableByteChannel rbc = null;
        int code;
        HttpURLConnection connection = (HttpURLConnection) website.openConnection();
        connection.setRequestMethod("GET");
        connection.connect();
        code = connection.getResponseCode();
        System.out.println("Code " + code + " from URL: " + url);
        connection.disconnect();

        if (code == 403) {
            Thread.sleep(5000);
            connection.connect();
            code = connection.getResponseCode();
            System.out.println("2nd Attempt - Code " + code + " from URL: " + url);
        }

        try {
            rbc = Channels.newChannel(website.openStream());
        } catch (IOException io) {
            Thread.sleep(3000);
            rbc = Channels.newChannel(website.openStream());
        }

        //Preparar el nom del fitxer
        String fileNameDownloaded = e.getName() + "_volum_" + (e.getVolumeNumber() + 1) + "_episodi_" + (e.getEpisodeNumber() + 1) + "_" + fileName;

        fileNameDownloaded = fileNameDownloaded.replace("-", "_").replace("-", "_").replaceAll("(\\d+)\\.(\\d+)", "$1_$2");

        System.out.println(pathForDownloads + fileNameDownloaded);

        //Copiar el nom de la descarrega
        FileOutputStream fos = new FileOutputStream(pathForDownloads + fileNameDownloaded);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
    }
}
