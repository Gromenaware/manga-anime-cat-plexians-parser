package cat.plexians.utils;

import java.io.File;
import java.io.IOException;
import java.text.Normalizer;
import java.util.List;

public class ParsingUtils {

    // Maximum file name length
    private static final int MAX_FILE_NAME_LENGTH = 196;

    public ParsingUtils() {
    }

    public String prepareParsingTitle(String nomDeLaSerie, Episode e) {
        String fileNameDownloaded = nomDeLaSerie + "_" + e.getTitle().replaceAll(" ", "_").toLowerCase() + "_" + e.getEpisodeTitle().replaceAll(" ", "_").toLowerCase() + "_" + e.getReleaseDate().replaceAll(" ", "_").toLowerCase() + ".mp4";
        fileNameDownloaded = cleanStringFromSpecialCharactersMp4(fileNameDownloaded);
        return fileNameDownloaded;
    }

    public String prepareParsingFromJsonTitle(Episode e) {
        String fileNameDownloaded = e.getEpisodeTitle() + "_" + e.getTitle();
        fileNameDownloaded = cleanStringFromSpecialCharactersMp4(fileNameDownloaded);
        return fileNameDownloaded;
    }

    public List<Episode> parseTxt(String pathTxtSerieParse, String nomDeLaSerie) {
        List<Episode> episodes = null;
        try {
            episodes = EpisodeUtils.parseFile(pathTxtSerieParse + nomDeLaSerie + ".txt");
            for (Episode episode : episodes) {
                System.out.println(episode);
                System.out.println("-----");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return episodes;
    }

    public static void directoryCreation(String pathForDownloads) {
        File binDir = new File(pathForDownloads);
        if (!binDir.exists()) {
            if (!binDir.mkdirs()) {
                System.out.println("unable to create bin directory");
            }
        }
    }

    public String cleanStringFromSpecialCharactersMp4(String input) {
        input = input.replaceAll(" ", "_").toLowerCase();
        input = input.replaceAll("-", "_").replaceAll("____", "_").replaceAll("___", "_").replaceAll("__", "_").toLowerCase();
        input = Normalizer.normalize(input, Normalizer.Form.NFD);
        input = input.replaceAll("[^\\p{ASCII}]", "");
        input = input.replaceAll("/", "_").replaceAll("'", "").replaceAll("\\p{M}", "").toLowerCase();
        input = input.replaceAll("\\(", "").replaceAll("\\)", "").toLowerCase();
        input = input.replaceAll(":", "_").toLowerCase();
        input = input.replaceAll(",", "").toLowerCase();
        input = input.replaceAll("\"", "").toLowerCase();
        input = input.replaceAll("\\.", "").toLowerCase();
        input = input.replaceAll("____", "_").replaceAll("___", "_").replaceAll("__", "_").toLowerCase();
        input = input.replaceAll("mp4", "").toLowerCase();

        System.out.println(" Longitud del fitxer " + input.length());
        if (input.length() > MAX_FILE_NAME_LENGTH) {
            input = trimStringToLength(input, 128);
        }
        input = input + ".mp4";

        System.out.println(" Nom del fitxer " + input);

        return input;
    }

    public String cleanStringFromSpecialCharactersJpg(String input) {
        input = input.replaceAll(" ", "_").toLowerCase();
        input = input.replaceAll("-", "_").replaceAll("____", "_").replaceAll("___", "_").replaceAll("__", "_").toLowerCase();
        input = Normalizer.normalize(input, Normalizer.Form.NFD);
        input = input.replaceAll("[^\\p{ASCII}]", "");
        input = input.replaceAll("/", "_").replaceAll("'", "").replaceAll("\\p{M}", "").toLowerCase();
        input = input.replaceAll("\\(", "").replaceAll("\\)", "").toLowerCase();
        input = input.replaceAll(":", "_").toLowerCase();
        input = input.replaceAll(",", "").toLowerCase();
        input = input.replaceAll("\"", "").toLowerCase();
        input = input.replaceAll("\\.", "").toLowerCase();
        input = input.replaceAll("____", "_").replaceAll("___", "_").replaceAll("__", "_").toLowerCase();
        input = input.replaceAll("mp4", "").toLowerCase();

        System.out.println(" Longitud del fitxer " + input.length());
        if (input.length() > MAX_FILE_NAME_LENGTH) {
            input = trimStringToLength(input, 128);
        }
        input = input + ".jpg";

        System.out.println(" Nom del fitxer " + input);

        return input;
    }

    // Method to cut the string to the specified length
    public static String trimStringToLength(String input, int maxLength) {
        if (input == null) {
            return null; // Return null if input string is null
        }

        // Check if the string needs to be trimmed
        if (input.length() > maxLength) {
            return input.substring(0, maxLength); // Return the trimmed string
        }

        return input; // Return the original string if no trimming is necessary
    }
}
