package cat.plexians.utils;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;

public class WebDriverUtils {

    public WebDriver setUpChromeHeadlessDriver() {
        if (OsValidator.isMac()) {
            System.out.println("Running on MacOS");
            System.setProperty("webdriver.chrome.driver", "src" + File.separator + "test" + File.separator + "resources" + File.separator + "webdriver" + File.separator + "chromedriver-macos");
        } else if (OsValidator.isUnix()) {
            System.out.println("Running on Linux");
            System.setProperty("webdriver.chrome.driver", "src" + File.separator + "test" + File.separator + "resources" + File.separator + "webdriver" + File.separator + "chromedriver");
        } else if (OsValidator.isWindows()) {
            System.out.println("Running on Windows");
            System.setProperty("webdriver.chrome.driver", "src" + File.separator + "test" + File.separator + "resources" + File.separator + "webdriver" + File.separator + "chromedriver.exe");
        }
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200", "--ignore-certificate-errors", "--disable-extensions", "--no-sandbox", "--disable-dev-shm-usage");
        return new ChromeDriver(options);
    }

    public WebDriver setUpChromeGuiDriver() {
        if (OsValidator.isMac()) {
            System.out.println("Running on MacOS");
            System.setProperty("webdriver.chrome.driver", "src" + File.separator + "test" + File.separator + "resources" + File.separator + "webdriver" + File.separator + "chromedriver-macos");
        } else if (OsValidator.isUnix()) {
            System.out.println("Running on Linux");
            System.setProperty("webdriver.chrome.driver", "src" + File.separator + "test" + File.separator + "resources" + File.separator + "webdriver" + File.separator + "chromedriver");
        } else if (OsValidator.isWindows()) {
            System.out.println("Running on Windows");
            System.setProperty("webdriver.chrome.driver", "src" + File.separator + "test" + File.separator + "resources" + File.separator + "webdriver" + File.separator + "chromedriver.exe");
        }
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu", "--window-size=1920,1200", "--ignore-certificate-errors", "--disable-extensions", "--no-sandbox", "--disable-dev-shm-usage");
        return new ChromeDriver(options);
    }

    public WebDriver setUpChromeIpadProDriver() {
        System.out.println("Running on Ipad Pro");
        System.setProperty("webdriver.chrome.driver", "src" + File.separator + "test" + File.separator + "resources" + File.separator + "webdriver" + File.separator + "chromedriver-macos");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-gpu", "--window-size=1024,1366", "--ignore-certificate-errors", "--disable-extensions", "--no-sandbox", "--disable-dev-shm-usage");
        return new ChromeDriver(options);
    }

    public WebDriver setUpGeckoDriver() {
        if (OsValidator.isMac()) {
            System.out.println("Running on MacOS");
            System.setProperty("webdriver.gecko.driver", "src" + File.separator + "test" + File.separator + "resources" + File.separator + "webdriver" + File.separator + "geckodriver-macos");
        } else if (OsValidator.isUnix()) {
            System.out.println("Running on Linux");
            System.setProperty("webdriver.gecko.driver", "src" + File.separator + "test" + File.separator + "resources" + File.separator + "webdriver" + File.separator + "geckodriver");
        } else if (OsValidator.isWindows()) {
            System.out.println("Running on Windows");
            System.setProperty("webdriver.gecko.driver", "src" + File.separator + "test" + File.separator + "resources" + File.separator + "webdriver" + File.separator + "geckodriver.exe");
        }
        // 2. Setup Options
        FirefoxOptions options = new FirefoxOptions();

        // 1. Disable Enhanced Tracking Protection (The "Shield" icon fix)
        options.addPreference("privacy.trackingprotection.enabled", false);
        options.addPreference("privacy.trackingprotection.pbmode.enabled", false);

        // 2. Accept ALL cookies (0 = Accept all, including third-party)
        options.addPreference("network.cookie.cookieBehavior", 0);

        // 3. Disable First-Party Isolation (Crucial for cross-domain library logins)
        options.addPreference("privacy.firstparty.isolate", false);

        // 4. Explicitly enable Local Storage and IndexedDB
        options.addPreference("dom.storage.enabled", true);
        options.addPreference("dom.indexedDB.enabled", true);

        // Optional: Disable the "Delete cookies on close" feature just in case
        options.addPreference("network.cookie.lifetimePolicy", 0);

        // 3. Spoof a normal User-Agent (Optional but highly recommended)
        // This makes your automated Firefox look exactly like a standard desktop Firefox
        options.addPreference("general.useragent.override", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:123.0) Gecko/20100101 Firefox/123.0");

        // Initialize the driver with these permissive settings
        return new FirefoxDriver(options);
    }

    public WebDriver setUpEdgeDriver() {
        if (OsValidator.isMac()) {
            System.out.println("Running on MacOS");
            System.setProperty("webdriver.edge.driver", "src" + File.separator + "test" + File.separator + "resources" + File.separator + "webdriver" + File.separator + "msedgedriver-macos");
        } else if (OsValidator.isUnix()) {
            System.out.println("Running on Linux");
            System.setProperty("webdriver.edge.driver", "src" + File.separator + "test" + File.separator + "resources" + File.separator + "webdriver" + File.separator + "msedgedriver-arm64.exe");
        } else if (OsValidator.isWindows()) {
            System.out.println("Running on Windows");
            System.setProperty("webdriver.edge.driver", "src" + File.separator + "test" + File.separator + "resources" + File.separator + "webdriver" + File.separator + "msedgedriver.exe");
        }
        EdgeOptions edgeOptions = new EdgeOptions();
        edgeOptions.addArguments("--headless", "--disable-gpu", "--window-size=1920,1200", "--ignore-certificate-errors", "--disable-extensions", "--no-sandbox", "--disable-dev-shm-usage");
        return new EdgeDriver(edgeOptions);
    }
}