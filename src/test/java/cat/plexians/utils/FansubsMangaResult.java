package cat.plexians.utils;

import java.text.Normalizer;

public class FansubsMangaResult {
    private String title;
    private String link;

    // Constructor
    public FansubsMangaResult(String title, String link) {
        this.title = cleanTitle(title); // Limpiar título
        this.link = link;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getLink() {
        return link;
    }

    // Método para limpiar el título
    public static String cleanTitle(String input) {
        if (input == null) return null;

        // Normalizar los caracteres especiales
        String normalized = Normalizer.normalize(input, Normalizer.Form.NFD);
        // Eliminar los diacríticos (acentos, tildes, etc.)
        String noSpecialChars = normalized.replaceAll("[^\\p{ASCII}]", "");

        // Eliminar los caracteres específicos (:, ')
        noSpecialChars = noSpecialChars.replaceAll("[:/'()]", " ").replace("/", " ").replace(":", " ").replace(",", " ").replace("'", " ").replaceAll("[.-]", " ");

        // Remove extra spaces
        noSpecialChars = noSpecialChars.replaceAll("\\s+", " ").trim();

        return noSpecialChars.replaceAll(" ", "-");


    }

    // Método toString para imprimir fácilmente
    @Override
    public String toString() {
        return "Títol: " + title + "\nEnllaç: " + link;
    }
}