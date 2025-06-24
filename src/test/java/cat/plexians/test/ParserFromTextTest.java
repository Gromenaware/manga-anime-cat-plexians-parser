package cat.plexians.test;

import cat.plexians.main.BaseWebDriver;
import cat.plexians.utils.Episode;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ParserFromTextTest extends BaseWebDriver {

    @Test
    public void initialTesT() throws IOException {

        String nomDeLaSerie = "Naruto";
        String pathTxtSerieParse = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "series" + File.separator;
        String pathForDownloads = File.separator + "Volumes" + File.separator + "TeraTwo" + File.separator + nomDeLaSerie + File.separator;

        //Neteja el nom de la serie
        nomDeLaSerie = parsingUtils.cleanStringFromSpecialCharactersMp4(nomDeLaSerie);

        //Parse del txt de torn
        List<Episode> episodes = parsingUtils.parseTxt(pathTxtSerieParse, nomDeLaSerie);

        //Preparem els directoris
        parsingUtils.directoryCreation(pathForDownloads);

        //Llistem tots els episodis que hem fet parsing previ
        for (Episode e : episodes) {
            //Descarrega de l'episodi corresponent
            episodeUtils.downloadEpisodeFromTextFile(nomDeLaSerie, pathForDownloads, e, parsingUtils);
        }
    }
}
