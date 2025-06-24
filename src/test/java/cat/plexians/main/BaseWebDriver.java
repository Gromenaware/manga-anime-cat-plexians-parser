package cat.plexians.main;

import cat.plexians.utils.*;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.lang.reflect.Method;
import java.util.Properties;

public class BaseWebDriver {

    public WebDriver driver;
    public EpisodeUtils episodeUtils;
    public ParsingUtils parsingUtils;
    public WebDriverUtils wdUtils;
    public String SELECTEDBROWSER = null;
    public String EBIBLIO_LOGIN = null;
    public String EBIBLIO_PASSWD = null;


    @BeforeMethod(alwaysRun = true) //Browser Setup
    public void setUp(Method method, Object[] testArguments) {

        Properties config = PropertiesUtils.getProcessedTestProperties();
        PropertiesReader propertiesReader = new PropertiesReader(config);

        SELECTEDBROWSER = propertiesReader.getSelectedBrowser();
        EBIBLIO_LOGIN = propertiesReader.getSelectedEbiblioLogin();
        EBIBLIO_PASSWD = propertiesReader.getSelectedEbiblioPasswd();
        wdUtils = new WebDriverUtils();
        episodeUtils = new EpisodeUtils();
        parsingUtils = new ParsingUtils();

        System.out.println("Selected Browser: " + SELECTEDBROWSER);

        if (SELECTEDBROWSER.equals("unset")) {
            driver = wdUtils.setUpEdgeDriver();
        } else {
            if (SELECTEDBROWSER.equalsIgnoreCase("chrome-headless")) {
                driver = wdUtils.setUpChromeHeadlessDriver();
            } else if (SELECTEDBROWSER.equalsIgnoreCase("chrome-gui")) {
                driver = wdUtils.setUpChromeGuiDriver();
            } else if (SELECTEDBROWSER.equalsIgnoreCase("chrome-ipad-pro")) {
                driver = wdUtils.setUpChromeIpadProDriver();
            } else if (SELECTEDBROWSER.equalsIgnoreCase("firefox")) {
                driver = wdUtils.setUpGeckoDriver();
            } else if (SELECTEDBROWSER.equalsIgnoreCase("edge")) {
                driver = wdUtils.setUpEdgeDriver();
            }
        }
    }

    @AfterMethod(alwaysRun = true) //El cierre del navegador
    public void tearDown() throws Exception {
        driver.quit();
    }
}
