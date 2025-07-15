package cat.plexians.test;

import cat.plexians.main.BaseWebDriver;
import cat.plexians.utils.NavigationActions;
import cat.plexians.utils.ParsingUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ParserFromSeasonTest extends BaseWebDriver {

    @Test
    public void initialTesT() throws InterruptedException, IOException {

        String seasonNumber = "3"; //Llegir del config
        String nomDeLaSerie = "bola-de-drac-super"; //Llegir del config
        String season = "season_" + seasonNumber;
        String pathForDownloads = File.separator + "Volumes" + File.separator + "02_2TB" + File.separator + "manganime" + File.separator + nomDeLaSerie + File.separator + season + File.separator;
        String urlDeDescarrega = "https://www.3cat.cat/3cat/" + nomDeLaSerie + "/capitols/temporada/" + seasonNumber + "/";

        System.out.println("Download Path: " + pathForDownloads);
        System.out.println("Download URL: " + urlDeDescarrega);

        driver.get(urlDeDescarrega);

        NavigationActions.hoverAndClick(driver, By.id("didomi-notice-agree-button"));

        Thread.sleep(4000);

        NavigationActions.hoverAndClick(driver, By.xpath("/html/body/div[2]/div[3]/div/div/div[1]/div/button[3]/span"));

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
