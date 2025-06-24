package cat.plexians.test;

import cat.plexians.main.BaseWebDriver;
import cat.plexians.utils.Episode;
import cat.plexians.utils.Root;
import cat.plexians.utils.Url;
import cat.plexians.utils.Video;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ParserFromJsonTest extends BaseWebDriver {

    @Test
    public void parserFromJsonTest() throws IOException, InterruptedException {
        String pathTxtSerieParse = "src" + File.separator + "test" + File.separator + "resources" + File.separator + "series" + File.separator;
        String nomDeLaSerie = "shin-chan";
        String pathForDownloads = File.separator + "Volumes" + File.separator + "TeraTwo" + File.separator + nomDeLaSerie + File.separator;
        List<Episode> episodes = new ArrayList<>();

        try {
            // Create ObjectMapper instance
            ObjectMapper mapper = new ObjectMapper();

            // Read JSON file and map/convert to Java object
            // Assuming the JSON file is named "data.json" and located in the project's root directory
            Root root = mapper.readValue(new File(pathTxtSerieParse + "shin-chan-20240831.json"), Root.class);

            // Navigate through the deserialized object to find the specific URL
            if (root.getVideos() != null) {
                for (Video video : root.getVideos()) {
                    if (video.getMedia() != null && video.getMedia().getUrl() != null) {
                        for (Url url : video.getMedia().getUrl()) {
                            if (url.getFile().contains("down-high")) {
                                episodeUtils.extractedEpisode(video, url, episodes);
                            } else if (url.getFile().contains("down-medium")) {
                                episodeUtils.extractedEpisode(video, url, episodes);
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Neteja el nom de la serie
        nomDeLaSerie = parsingUtils.cleanStringFromSpecialCharactersMp4(nomDeLaSerie);

        //Preparem els directoris
        parsingUtils.directoryCreation(pathForDownloads);

        //Llistem tots els episodis que hem fet parsing previ
        for (Episode e : episodes) {
            //Descarrega de l'episodi corresponent
            episodeUtils.downloadEpisodeFromJsonFile(nomDeLaSerie, pathForDownloads, e, parsingUtils);
        }
    }


}
