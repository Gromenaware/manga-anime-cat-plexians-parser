package cat.plexians.test;

import cat.plexians.main.BaseWebDriver;
import cat.plexians.utils.NavigationActions;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ParserFromSeasonTest extends BaseWebDriver {

    @Test
    public void initialTesT() throws InterruptedException, IOException {

        String seasonNumber = "1"; //Llegir del config
        String nomDeLaSerie = "prodigiosa_les_aventures_de_ladybug_i_gat_noir"; //Llegir del config
        String season = "season_" + seasonNumber;
        //String pathForDownloads = File.separator + "Volumes" + File.separator + "02_2TB" + File.separator + "manganime" + File.separator + nomDeLaSerie + File.separator + season + File.separator;
        String pathForDownloads = File.separator + "Volumes" + File.separator + "02_2TB" + File.separator + "series" + File.separator + nomDeLaSerie + File.separator + season + File.separator;
        String urlDeDescarrega = "https://www.3cat.cat/3cat/" + nomDeLaSerie + "/capitols/temporada/" + seasonNumber + "/";

        System.out.println("Download Path: " + pathForDownloads);
        System.out.println("Download URL: " + urlDeDescarrega);

        driver.get(urlDeDescarrega);

        NavigationActions.hoverAndClick(driver, By.id("didomi-notice-agree-button"));

        Thread.sleep(4000);

        // Reviu
        NavigationActions.hoverAndClick(driver, By.xpath("/html/body/div[2]/div[3]/div/div/div[1]/button"));

        Thread.sleep(4000);

        //Mirar si hi ha scroll de temporada
        JavascriptExecutor js = (JavascriptExecutor) driver;

        for (int i = 0; i < 4; i++) {
            // Get current scroll height before scrolling
            Long lastHeight = (Long) js.executeScript("return document.body.scrollHeight");

            // Scroll down
            js.executeScript("window.scrollBy(0, window.innerHeight);");
            Thread.sleep(1000); // Wait for load

            // Get new scroll height
            Long newHeight = (Long) js.executeScript("return document.body.scrollHeight");

            // Calculate current position (visual top + window height)
            Long currentPos = (Long) js.executeScript("return window.pageYOffset + window.innerHeight");

            // If we are at the bottom of the page, break the loop
            if (currentPos >= newHeight) {
                System.out.println("Reached end of page early.");
                break;
            }
        }


        //Crear el lloc on es descarrega el contingut
        //Preparem els directoris
        parsingUtils.directoryCreation(pathForDownloads);

        ArrayList<String> episodeList = episodeUtils.captureURLFromEpisodes(driver);


        for (String e : episodeList) {
            //episodeUtils.downloadEpisodeFrom3Cat(driver, parsingUtils.cleanStringFromSpecialCharactersMp4(nomDeLaSerie), e, pathForDownloads, parsingUtils);

            //Prepara les urls per descarregar
            String[] arrOfStr = e.split("/");
            String idVideo = arrOfStr[arrOfStr.length - 1];
            String urlDinamics = "https://dinamics.ccma.cat/pvideo/media.jsp?media=video&version=0s&profile=tv&idint=" + idVideo;
            driver.get(urlDinamics);

            //Aconsegueix el mp4 high quality
            String jsonString = episodeUtils.extractJsonFromHtml(driver.getPageSource());

            System.out.println(driver.getCurrentUrl());
            System.out.println(jsonString);

            episodeUtils.downloadEpisodeFromJson(jsonString, parsingUtils.cleanStringFromSpecialCharactersMp4(nomDeLaSerie), pathForDownloads, parsingUtils);
        }
    }
}
