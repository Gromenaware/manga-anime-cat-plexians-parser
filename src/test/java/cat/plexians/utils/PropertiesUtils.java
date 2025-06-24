package cat.plexians.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class PropertiesUtils {
    /**
     * Loads a properties file using namespace as file name. If the file is not found, returns an empty properties object.
     *
     * @return Properties read from the file
     */
    public static Properties loadResourceProperties() {
        Properties prop = new Properties();
        try (InputStream resourceStream = PropertiesUtils.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (resourceStream != null) {
                prop.load(new InputStreamReader(resourceStream, StandardCharsets.UTF_8));
            } else {
                System.err.println("config.properties file not found.");
            }
        } catch (IOException e) {
            System.err.println("Error loading config.properties: " + e.getMessage());
        }
        return prop;
    }

    /**
     * Overrides the resource properties with the corresponding system properties.
     *
     * @param resourceProperties the properties to be overridden with system properties
     * @param systemProperties   the properties to override with
     */
    public static void overrideProperties(Properties resourceProperties, Properties systemProperties) {
        if (resourceProperties != null) {
            for (String key : resourceProperties.stringPropertyNames()) {
                if (systemProperties.containsKey(key)) {
                    resourceProperties.setProperty(key, systemProperties.getProperty(key));
                }
            }
        }
    }

    /**
     * Returns the properties overridden with system (command-line) properties.
     *
     * @return the properties overridden with system (command-line) properties
     */
    public static Properties getProcessedTestProperties() {
        Properties resourceProperties = loadResourceProperties();
        Properties systemProperties = System.getProperties();
        overrideProperties(resourceProperties, systemProperties);
        return resourceProperties;
    }
}
