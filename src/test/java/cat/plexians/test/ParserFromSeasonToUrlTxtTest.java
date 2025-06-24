package cat.plexians.test;

import cat.plexians.main.BaseWebDriver;
import cat.plexians.utils.VideoInfo;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class ParserFromSeasonToUrlTxtTest extends BaseWebDriver {

    @Test
    public void initialTesT() throws InterruptedException, IOException {

        String nomDeLaSerie = "detectiu-conan"; // Read from config
        int totalSeasons = 21; // Define the total number of seasons (or read from config)

        for (int seasonNumber = 17; seasonNumber <= totalSeasons; seasonNumber++) {
            String season = "season_" + seasonNumber;
            String pathForDownloads = File.separator + "Volumes" + File.separator + "02_2TB" + File.separator + "manganime" + File.separator + nomDeLaSerie + File.separator + season + File.separator;
            String urlDeDescarrega = "https://www.3cat.cat/3cat/" + nomDeLaSerie + "/capitols/temporada/" + seasonNumber + "/";

            System.out.println("Download Path: " + pathForDownloads);
            System.out.println("Download URL: " + urlDeDescarrega);

            // Write the season URL to a text file
            writeUrlToFile(pathForDownloads, nomDeLaSerie, season, urlDeDescarrega);

            driver.get(urlDeDescarrega);

            // Create the directory where content will be downloaded
            parsingUtils.directoryCreation(pathForDownloads);

            ArrayList<String> episodeList = episodeUtils.captureURLFromEpisodes(driver);

            for (String e : episodeList) {
                VideoInfo infoEpisodi = episodeUtils.getUrlFromEpisodeFrom3Cat(driver, e);
                // Write the episode URL to a text file
                // Write VideoInfo to file
                writeVideoInfoToFile(pathForDownloads, nomDeLaSerie, season, infoEpisodi);
            }
        }
    }

    private void writeUrlToFile(String pathForDownloads, String nomDeLaSerie, String season, String urlDelEpisodi) {
        // Define the file path for the URL text file
        String filePath = pathForDownloads + nomDeLaSerie + "_" + season + "_download_url.txt";

        try {
            // Ensure the parent directory exists
            File file = new File(filePath);
            file.getParentFile().mkdirs();

            // Write the URL to the file
            FileWriter writer = new FileWriter(file, true); // `true` to append to the file
            writer.write(urlDelEpisodi + System.lineSeparator()); // Write each URL on a new line
            writer.close();

            System.out.println("URL written to file: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing URL to file: " + e.getMessage());
        }
    }

    private void writeVideoInfoToFile(String pathForDownloads, String nomDeLaSerie, String season, VideoInfo videoInfo) {
        // Define the file path for the text file
        String filePath = pathForDownloads + nomDeLaSerie + "_" + season + "_download_info.txt";

        try {
            // Ensure the parent directory exists
            File file = new File(filePath);
            file.getParentFile().mkdirs();

            // Write the VideoInfo details to the file
            FileWriter writer = new FileWriter(file, true); // `true` to append to the file
            writer.write("Títol: " + videoInfo.getVideoTitle() + System.lineSeparator());
            writer.write("Id del video: " + videoInfo.getIdVideo() + System.lineSeparator());
            writer.write("Dinamics url: " + videoInfo.getUrlDinamics() + System.lineSeparator());
            writer.write("URL descarrega High: " + videoInfo.getUrlDescarrega() + System.lineSeparator());
            writer.write(System.lineSeparator()); // Add a blank line between entries
            writer.close();

            System.out.println("VideoInfo written to file: " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing VideoInfo to file: " + e.getMessage());
        }
    }

}