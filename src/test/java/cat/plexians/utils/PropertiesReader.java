package cat.plexians.utils;

import java.util.Properties;

/**
 * Handle Suites configuration in the framework with its properties.
 *
 * @author guillem.hernandez
 */
public class PropertiesReader {

    public static String EBIBLIO_LOGIN;
    public static String EBIBLIO_PASSWD;
    public static String EMAILS;
    public static String TIMEOUT;
    public static String NUM_THREADS;
    public static String DATA_PROVIDER_THREAD_COUNT;
    public static String HAS_RETRY;
    public static String RETRIES;
    public static String SUITE_NAME;
    public static String REPORTS_DIR;
    public static String TEST_PACKAGE;
    public static String SELECTEDBROWSER;

    public PropertiesReader(Properties props) {
        SUITE_NAME = props.getProperty("suite");
        TEST_PACKAGE = props.getProperty("test_package", "unset");
        EMAILS = props.getProperty("emails", "unset");
        TIMEOUT = props.getProperty("timeout", "unset");
        NUM_THREADS = props.getProperty("num_of_threads", "unset");
        DATA_PROVIDER_THREAD_COUNT = props.getProperty("data_provider_thread_count", "unset");
        HAS_RETRY = props.getProperty("has_retry", "unset");
        RETRIES = props.getProperty("retries", "unset");
        REPORTS_DIR = props.getProperty("reports_dir");
        SELECTEDBROWSER = props.getProperty("browser", "unset");
        EBIBLIO_LOGIN = props.getProperty("ebiblio_login", "unset");
        EBIBLIO_PASSWD = props.getProperty("ebiblio_passwd", "unset");
    }

    public String getSelectedBrowser() {
        return SELECTEDBROWSER.replaceAll("\\s+", "");
    }

    public String getSelectedEbiblioLogin() {
        return EBIBLIO_LOGIN.replaceAll("\\s+", "");
    }

    public String getSelectedEbiblioPasswd() {
        return EBIBLIO_PASSWD.replaceAll("\\s+", "");
    }

}