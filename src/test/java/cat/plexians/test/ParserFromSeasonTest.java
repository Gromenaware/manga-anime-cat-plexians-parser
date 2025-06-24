package cat.plexians.test;

import cat.plexians.main.BaseWebDriver;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class ParserFromSeasonTest extends BaseWebDriver {

    @Test
    public void initialTesT() throws InterruptedException, IOException {

        String seasonNumber = "2"; //Llegir del config
        String nomDeLaSerie = "bola-de-drac-super"; //Llegir del config
        String season = "season_" + seasonNumber;
        String pathForDownloads = File.separator + "Volumes" + File.separator + "02_2TB" + File.separator + "manganime" + File.separator + nomDeLaSerie + File.separator + season + File.separator;
        String urlDeDescarrega = "https://www.3cat.cat/3cat/" + nomDeLaSerie + "/capitols/temporada/" + seasonNumber + "/";

        System.out.println("Download Path: " + pathForDownloads);
        System.out.println("Download URL: " + urlDeDescarrega);

        driver.get(urlDeDescarrega);

        //Crear el lloc on es descarrega el contingut
        //Preparem els directoris
        parsingUtils.directoryCreation(pathForDownloads);

        ArrayList<String> episodeList = episodeUtils.captureURLFromEpisodes(driver);

        for (String e : episodeList) {
            episodeUtils.downloadEpisodeFrom3Cat(driver, parsingUtils.cleanStringFromSpecialCharactersMp4(nomDeLaSerie), e, pathForDownloads, parsingUtils);
        }
    }
}
