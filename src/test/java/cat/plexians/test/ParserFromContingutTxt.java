package cat.plexians.test;

import cat.plexians.main.BaseWebDriver;
import org.testng.annotations.Test;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParserFromContingutTxt {

    @Test
    public void downloadChibiEpisodes() {

        String nomDeLaSerie = "Prodigiosa_Les_aventures_de_Ladybug_i_Gat_Noir"; // Read from config

        // RUTA DEL FITXER TXT D'ENTRADA (Canvia-ho a la teva ruta real)
        String inputTxtPath = File.separator + "Users" + File.separator + "guillemhernandezsola" + File.separator + "code" + File.separator + "manga-anime-cat-plexians-parser" + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "contingut" + File.separator + "dibuixos_animats" + File.separator + "20260204_ladybug_sx3.txt";

        // RUTA ON VOLS GUARDAR ELS VIDEOS
        String outputFolderPath = File.separator + "Volumes" + File.separator + "02_2TB" + File.separator + "manganime" + File.separator + nomDeLaSerie + File.separator;

        // 1. Crear directori si no existeix
        new File(outputFolderPath).mkdirs();

        // 2. Llegir el fitxer i extreure URLs d'Info
        List<String> infoUrls = extractInfoUrls(inputTxtPath);

        System.out.println("S'han trobat " + infoUrls.size() + " enllaços d'informació.");

        for (String infoUrl : infoUrls) {
            try {
                System.out.println("Processant: " + infoUrl);

                // 3. Obtenir el JSON de la URL
                String jsonContent = fetchJsonFromUrl(infoUrl);

                // 4. Extreure dades del JSON (Títol complet, Slug, URL mp4 720p)
                String titolComplet = extractJsonValue(jsonContent, "titol_complet");
                String slug = extractJsonValue(jsonContent, "slug");
                String videoUrl = extractVideoUrl720p(jsonContent);

                if (videoUrl == null || videoUrl.isEmpty()) {
                    System.err.println("NO s'ha trobat URL 720p per: " + titolComplet);
                    continue;
                }

                // 5. Construir nom del fitxer i netejar caràcters il·legals
                String filename = sanitizeFilename(titolComplet + " - " + slug + ".mp4");
                String finalOutputPath = outputFolderPath + filename;

                // 6. Descarregar el fitxer
                System.out.println("Descarregant: " + filename);
                downloadFile(videoUrl, finalOutputPath);
                System.out.println("Descarregat correctament!\n");

            } catch (Exception e) {
                System.err.println("Error processant la URL: " + infoUrl);
                e.printStackTrace();
            }
        }
    }

    // --- Mètodes Auxiliars ---

    private List<String> extractInfoUrls(String filePath) {
        List<String> urls = new ArrayList<>();
        try (Scanner scanner = new Scanner(new File(filePath))) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine().trim();
                if (line.startsWith("Info:")) {
                    urls.add(line.replace("Info:", "").trim());
                }
            }
        } catch (FileNotFoundException e) {
            System.err.println("Fitxer TXT no trobat: " + filePath);
        }
        return urls;
    }

    private String fetchJsonFromUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(5000);
        conn.setReadTimeout(5000);

        try (BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
            StringBuilder result = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                result.append(line);
            }
            return result.toString();
        }
    }

    // Extreu valors simples del JSON usant Regex (per evitar dependències externes com Jackson/Gson)
    private String extractJsonValue(String json, String key) {
        // Busca "key":"valor"
        Pattern pattern = Pattern.compile("\"" + key + "\":\"(.*?)\"");
        Matcher matcher = pattern.matcher(json);
        if (matcher.find()) {
            return matcher.group(1);
        }
        return "Desconegut";
    }

    // Lògica específica per trobar el fitxer 720p dins l'array de media
    private String extractVideoUrl720p(String json) {
        // Aquesta regex busca un bloc que contingui "file":"URL" ... "label":"720p"
        // O "label":"720p" ... "file":"URL". L'ordre al JSON pot variar.

        // Estratègia simple: Busquem tots els objectes dins de "media" -> "url"
        // Com que fer-ho amb regex pur és complex, busquem directament la cadena del fitxer
        // associada a 720p.

        // Pattern: "file":"(url)","label":"720p"
        Pattern p1 = Pattern.compile("\"file\":\"(https:[^\"]+)\",\"label\":\"720p\"");
        Matcher m1 = p1.matcher(json);
        if (m1.find()) return m1.group(1);

        // Pattern invers: "label":"720p","ott":false,"active":false,"file":"(url)"
        // Això és més arriscat amb regex. Si el format és consistent com l'exemple:
        // {"file":"...","label":"720p"...}
        return null;
    }

    private String sanitizeFilename(String name) {
        // Reemplaça caràcters no vàlids per a noms de fitxer (\ / : * ? " < > |)
        return name.replaceAll("[\\\\/:*?\"<>|]", "_");
    }

    private void downloadFile(String fileURL, String saveDir) throws IOException {
        URL url = new URL(fileURL);
        HttpURLConnection httpConn = (HttpURLConnection) url.openConnection();
        int responseCode = httpConn.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            try (InputStream inputStream = httpConn.getInputStream(); FileOutputStream outputStream = new FileOutputStream(saveDir)) {

                byte[] buffer = new byte[4096];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }
        } else {
            System.out.println("No s'ha pogut descarregar. Codi HTTP: " + responseCode);
        }
        httpConn.disconnect();
    }
}