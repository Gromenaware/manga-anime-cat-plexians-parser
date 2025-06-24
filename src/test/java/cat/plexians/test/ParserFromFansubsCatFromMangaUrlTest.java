package cat.plexians.test;

import cat.plexians.main.BaseWebDriver;
import cat.plexians.utils.FansubsMangaResult;
import cat.plexians.utils.MangaEpisode;
import cat.plexians.utils.ParsingUtils;
import cat.plexians.utils.ZipCreator;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParserFromFansubsCatFromMangaUrlTest extends BaseWebDriver {

    @Test
    public void initialTesT() throws IOException, InterruptedException {

        String nomDelFansub = "fansubs-salou";
        String nomDeLaSerie = "el-noi-en-que-estava-interessada-no-era-en-cap-cas-un-noi";

        nomDeLaSerie = FansubsMangaResult.cleanTitle(nomDeLaSerie);

        String urlInicial = "https://manga.fansubs.cat/" + nomDeLaSerie.toLowerCase() + "/" + nomDelFansub.toLowerCase().replaceAll(" ", "-");

        System.out.println("URL del manga: " + urlInicial);

        String pathForDownloads = File.separator + "Volumes" + File.separator + "02_2TB" + File.separator + nomDeLaSerie.replace("-", "_") + File.separator;

        //Preparem els directoris
        parsingUtils.directoryCreation(pathForDownloads);

        driver.get(urlInicial);

        String totsElsVolums = "//div[@class='episode-table episode-table-manga']";

        List<WebElement> volums = driver.findElements(By.xpath(totsElsVolums));

        System.out.println("Hi ha " + volums.size() + " volums");

        List<MangaEpisode> mangaEpisodes = new ArrayList<>();

        for (int i = 0; i < volums.size(); i++) {
            WebElement volum = volums.get(i);

            // Locate all episodes within the current volume
            List<WebElement> numeroEpisodis = volum.findElements(By.xpath("//body/div[1]/div[2]/div[2]/div[3]/div[2]/div[1]/div[1]/div[" + (i + 1) + "]/div[2]/div[1]/div"));

            System.out.println("Hi ha " + numeroEpisodis.size() + " episodis en el volum " + (i + 1));

            for (int j = 0; j < numeroEpisodis.size(); j++) {
                WebElement episod = numeroEpisodis.get(j);
                // Get the data-file-id attribute of the current episode
                String dataFileId = episod.getAttribute("data-file-id");
                System.out.println("El data-file-id del episodi " + (j + 1) + " és " + dataFileId);
                if (dataFileId != null) {
                    mangaEpisodes.add(new MangaEpisode(nomDeLaSerie.replace(" ", "_").replace("_", "-"), nomDelFansub.replace(" ", "_").replace("_", "-"), i, j, dataFileId));
                }
            }
        }

        // Print the episodes
        for (MangaEpisode episode : mangaEpisodes) {
            // Open the URL associated with the current episode
            String episodeUrl = "https://escudellaicarndolla.xyz/Manga/" + episode.getFileId();
            System.out.println("Obrint Url: " + episodeUrl);
            driver.get(episodeUrl);

            // Locate all <a> elements that have href ending with .jpg
            List<WebElement> imageLinks = driver.findElements(By.xpath("//pre//a[contains(@href, '.jpg') or contains(@href, '.png')]"));

            System.out.println("Volum " + (episode.getVolumeNumber() + 1) + " hi ha " + imageLinks.size() + " pàgines en aquest episodi " + (episode.getEpisodeNumber() + 1));

            String pathForDownloadsForEpisode = pathForDownloads + "volum_" + Integer.toString((episode.getVolumeNumber() + 1)) + File.separator + "episodi_" + Integer.toString((episode.getEpisodeNumber() + 1)) + File.separator;

            ParsingUtils.directoryCreation(pathForDownloadsForEpisode);

            // Loop through the elements and print the URLs
            for (WebElement link : imageLinks) {
                String imageUrl = link.getAttribute("href");
                System.out.println("Obrint pàgina episodi: " + imageUrl);
                assert imageUrl != null;
                episodeUtils.downloadMangaEpisodePageFromURL(imageUrl, pathForDownloadsForEpisode, episode);
            }
            // Create ZIP file
            String zipFileName = pathForDownloadsForEpisode + nomDeLaSerie + "_volum_" + Integer.toString((episode.getVolumeNumber() + 1)) + "_episodi_" + Integer.toString((episode.getEpisodeNumber() + 1)) + "_[" + nomDelFansub + "].cbr";
            zipFileName = zipFileName.replaceAll("-", "_");
            ZipCreator.createZip(pathForDownloadsForEpisode, zipFileName.replace(" ", "_"));
        }
    }
}




