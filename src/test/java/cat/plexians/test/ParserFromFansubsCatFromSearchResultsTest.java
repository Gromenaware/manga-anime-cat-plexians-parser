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

public class ParserFromFansubsCatFromSearchResultsTest extends BaseWebDriver {

    @Test
    public void initialTesT() throws IOException, InterruptedException {

        String nomDelFansub = "lluna-plena-no-fansub+el-detectiu-conan-en-catala";

        // Navegar a la URL
        String url = "https://manga.fansubs.cat/cerca?min_duration=1&max_duration=100&min_rating=0&max_rating=4&min_score=0&max_score=100&min_year=1950&max_year=2025&fansub=" + nomDelFansub.toLowerCase().replaceAll(" ", "-") + "&full_catalogue=1&status[]=1&status[]=3&status[]=2&demographics[]=35&demographics[]=27&demographics[]=12&demographics[]=16&demographics[]=1&demographics[]=-1&origins[]=manga&origins[]=manhua&origins[]=manhwa&origins[]=novel";

        driver.get(url);

        // Esperar que la página cargue completamente
        Thread.sleep(5000); // Ajusta el tiempo según sea necesario o usa WebDriverWait

        // Buscar todos los resultados de manga
        List<WebElement> results = driver.findElements(By.xpath("//div[contains(@class,'section-content catalogue')]/div"));

        // Lista para almacenar los resultados
        List<FansubsMangaResult> mangaResults = new ArrayList<>();

        // Iterar sobre los resultados y obtener los títulos y enlaces
        for (int i = 1; i <= results.size(); i++) {
            // Obtener el título
            WebElement titleElement = driver.findElement(By.xpath("//div[contains(@class,'section-content catalogue')]/div[" + i + "]/div/div/div[contains(@class,'ellipsized-title')]"));
            String title = titleElement.getText();

            // Obtener el enlace
            WebElement linkElement = driver.findElement(By.xpath("//div[contains(@class,'section-content catalogue')]/div[" + i + "]/div/div/a"));
            String link = linkElement.getAttribute("href");

            // Crear un objeto MangaResult y agregarlo a la lista
            mangaResults.add(new FansubsMangaResult(title, link));
        }

        // Parsing  dels resultats
        for (FansubsMangaResult result : mangaResults) {
            System.out.println(result);
            System.out.println("-----------");

            String nomDeLaSerie = result.getTitle();
            String urlInicial = result.getLink();

            System.out.println("URL del manga: " + urlInicial);

            String pathForDownloads = File.separator + "Volumes" + File.separator + "TeraTwo" + File.separator + nomDeLaSerie.replace("-", "_") + File.separator;

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
                String episodeUrl = "https://dacsafregida.xyz/Manga/" + episode.getFileId();
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
                System.out.println("Comprimint la carpeta " + pathForDownloadsForEpisode);
                System.out.println("Creant fitxer per comprimir " + zipFileName);
                ZipCreator.createZip(pathForDownloadsForEpisode, zipFileName.replace(" ", "_").replace("-", "_"));

            }
        }
    }
}



