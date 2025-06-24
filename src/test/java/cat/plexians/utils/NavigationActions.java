package cat.plexians.utils;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static cat.plexians.utils.Locators.identifyLocationStrategy;

public class NavigationActions {
    private static final Logger LOGGER = LoggerFactory.getLogger(NavigationActions.class);

    protected final WebDriver driver;
    public final RemoteWebDriverWait wait;

    public NavigationActions(WebDriver driver) {
        this.driver = driver;
        this.wait =
                new RemoteWebDriverWait(driver, 2);
    }

    public static void click(WebDriver driver, WebElement webElement) {
        webElement.click();
    }

    public static void click(WebDriver driver, String objectId) {
        driver.findElement(identifyLocationStrategy(objectId)).click();
    }

    public static WebElement getLastWebelement(WebDriver driver, String locator) {
        List<WebElement> elements = driver.findElements(identifyLocationStrategy(locator));
        return elements.get(elements.size() - 1);
    }

    public static WebElement getFirstWebelement(WebDriver driver, String locator) {
        List<WebElement> elements = driver.findElements(identifyLocationStrategy(locator));
        return elements.get(0);
    }

    public static int countElementsFromXpath(WebDriver driver, String locator) {
        List<WebElement> elements = driver.findElements(identifyLocationStrategy(locator));
        return elements.size();
    }

    public static boolean elementExists(WebDriver driver, By by) {
        try {
            driver.findElement(by);
        } catch (NoSuchElementException e) {
            LOGGER.info(e.toString());
            return false;
        }
        return true;
    }

    public static void hoverAndClick(WebDriver driver, WebElement webElement) {
        Actions action = new Actions(driver);
        action.moveToElement(webElement).moveToElement(webElement).click(webElement).build()
                .perform();
    }

    public static void hoverAndClick(WebDriver driver, By by) {
        Actions action = new Actions(driver);
        action.moveToElement(driver.findElement(by)).moveToElement(driver.findElement(by)).click(driver.findElement(by)).build()
                .perform();
    }

    public static void hoverAndClick(WebDriver driver, String objectId) {
        Actions action = new Actions(driver);
        WebElement elem = driver.findElement(identifyLocationStrategy(objectId));
        action.moveToElement(elem).moveToElement(elem).click(elem).build().perform();
    }

    public static void hoverElement(WebDriver driver, String objectId) {
        Actions action = new Actions(driver);
        WebElement elem = driver.findElement(identifyLocationStrategy(objectId));
        action.moveToElement(elem).moveToElement(elem).perform();
    }

    public static void hoverElement(WebDriver driver, WebElement webElement) {
        Actions action = new Actions(driver);
        action.moveToElement(webElement).moveToElement(webElement).perform();
    }

    public static void hoverElement(WebDriver driver, By by) {
        Actions action = new Actions(driver);
        action.moveToElement(driver.findElement(by)).moveToElement(driver.findElement(by)).moveToElement(driver.findElement(by)).moveToElement(driver.findElement(by)).perform();

    }

    public static WebElement getLastWebelement(WebDriver driver, List<WebElement> webElements) {
        return webElements.get(webElements.size() - 1);
    }

    public static WebElement getFirstWebelement(WebDriver driver, List<WebElement> webElements) {
        return webElements.get(0);
    }

    public static int countElementsFromXpath(WebDriver driver, List<WebElement> webElements) {
        return webElements.size();
    }

    public static boolean isElementPresent(WebElement webelement) {
        boolean exists = false;
        try {
            webelement.getTagName();
            exists = true;
        } catch (NoSuchElementException e) {
            // nothing to do.
            e.printStackTrace();
        }
        return exists;
    }

    public static boolean WebElementExists(WebDriver driver, WebElement webElement) {
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        try {
            webElement.isEnabled();
        } catch (NoSuchElementException e) {
            driver.manage().timeouts()
                    .implicitlyWait(2,
                            TimeUnit.SECONDS);
            LOGGER.info("NoSuchElementException" + e.toString());
            return false;
        }
        driver.manage().timeouts()
                .implicitlyWait(2,
                        TimeUnit.SECONDS);
        return true;
    }

    public static boolean WebElementExists(WebDriver driver, WebElement webElement, boolean logInfo) {
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        try {
            webElement.isEnabled();
        } catch (NoSuchElementException e) {
            driver.manage().timeouts()
                    .implicitlyWait(2,
                            TimeUnit.SECONDS);
            if (logInfo) {
                LOGGER.info("NoSuchElementException" + e.toString());
            }
            return false;
        }
        driver.manage().timeouts()
                .implicitlyWait(2,
                        TimeUnit.SECONDS);
        return true;
    }

    public static Boolean WaitForElement(WebDriver driver, WebElement element) {
        try {
            WebElement elementResponse =
                    (new WebDriverWait(driver, Duration.ofSeconds(10))).until(ExpectedConditions.visibilityOf(element));
            return WebElementExists(driver, elementResponse);
        } catch (TimeoutException TimeOut) {
            LOGGER.info(TimeOut.toString());
            return false;
        }
    }

    /**
     * Method  used check if a WebElement exists and it's displayed.
     *
     * @param driver
     * @param elementToCheck
     * @return
     */
    public static boolean elementPresent(WebDriver driver, WebElement elementToCheck) {
        if (WebElementExists(driver, elementToCheck) && elementToCheck.isDisplayed()) {
            return true;
        } else
            return false;
    }

    public static boolean elementPresent(WebDriver driver, WebElement elementToCheck, boolean logInfo) {
        if (WebElementExists(driver, elementToCheck, logInfo) && elementToCheck.isDisplayed()) {
            return true;
        } else
            return false;
    }

    /**
     * This method return the Xpath location from a (Xpath)WebElement return -1 if error
     *
     * @param element
     * @return
     */
    public static String getXpathLocatorFronWebElement(WebElement element) {
        String xpahtLocation = element.toString();
        try {
            xpahtLocation =
                    xpahtLocation.substring(xpahtLocation.indexOf("//"), xpahtLocation.length() - 1)
                            .trim();
        } catch (Exception e) {
            LOGGER.info(e.toString());
            xpahtLocation = "-1";
        }
        return xpahtLocation;
    }

    /**
     * this method wait for element be presents (20000)
     * return true if the elemnt is found or false
     *
     * @param driver
     * @param element
     * @param wait
     * @return
     */
    public static boolean waitForElementPresent(WebDriver driver, WebElement element,
                                                RemoteWebDriverWait wait) {
        int i = 0;
        while (!WebElementExists(driver, element) && i < 20) {
            wait.pause(1000);
            i++;
        }
        return WebElementExists(driver, element);
    }

    public void listIframesFromPage(WebDriver driver) {
        final List<WebElement> iframes = driver.findElements(By.tagName("frame"));
        for (WebElement iframe : iframes) {
            LOGGER.info(iframe.getAttribute("id"));
            LOGGER.info(iframe.getAttribute("name"));
        }
    }
}
