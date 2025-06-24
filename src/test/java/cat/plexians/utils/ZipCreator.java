package cat.plexians.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCreator {

    // Helper method to add a file to the ZIP
    public static void addToZip(File file, ZipOutputStream zos) throws IOException {
        // Open the file as an input stream
        FileInputStream fis = new FileInputStream(file);

        // Create a new ZipEntry for the file
        ZipEntry zipEntry = new ZipEntry(file.getName());
        zos.putNextEntry(zipEntry);

        // Read the file and write to the ZIP output stream
        byte[] buffer = new byte[1024];
        int bytesRead;
        while ((bytesRead = fis.read(buffer)) > 0) {
            zos.write(buffer, 0, bytesRead);
        }

        // Close the current entry and the file input stream
        zos.closeEntry();
        fis.close();
    }

    public static void createZip(String sourceDirectory, String zipFileName) {
        try {
            // Create a FileOutputStream to write the ZIP file
            FileOutputStream fos = new FileOutputStream(zipFileName);
            // Create a ZipOutputStream to handle compression
            ZipOutputStream zos = new ZipOutputStream(fos);

            // Get the source directory as a Path
            Path sourceDirPath = Paths.get(sourceDirectory);
            Path zipFilePath = Paths.get(zipFileName);

            // Walk through the directory tree
            Files.walk(sourceDirPath).forEach(path -> {
                try {
                    // Skip directories, the ZIP file itself, and hidden files (e.g., "._" files)
                    if (Files.isDirectory(path) || path.equals(zipFilePath) || path.getFileName().toString().startsWith("._")) {
                        return;
                    }

                    // Create a ZipEntry with a relative path
                    Path relativePath = sourceDirPath.relativize(path);
                    System.out.println("ZIPPING this file: " + relativePath.toString().replace("\\", "/"));
                    ZipEntry zipEntry = new ZipEntry(relativePath.toString().replace("\\", "/")); // Normalize path for ZIP

                    // Add the file to the ZIP
                    zos.putNextEntry(zipEntry);

                    // Write the file content to the ZIP
                    Files.copy(path, zos);
                    zos.closeEntry();
                } catch (IOException e) {
                    System.err.println("Error adding file to ZIP: " + path + " - " + e.getMessage());
                }
            });

            // Close the streams
            zos.close();
            fos.close();
            System.out.println("Files successfully compressed into: " + zipFileName);

        } catch (IOException e) {
            System.err.println("Error while creating ZIP file: " + e.getMessage());
        }
    }
}
