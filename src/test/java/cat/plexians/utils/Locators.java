package cat.plexians.utils;

import org.openqa.selenium.By;

public class Locators {
    public static By identifyLocationStrategy(String objectId) {
        By by = null;
        if (objectId.startsWith("/") || objectId.startsWith("(") || objectId.startsWith(".//*")) {
            by = By.xpath(objectId);
        } else if (objectId.startsWith("div") || objectId.startsWith("span") || objectId
                .startsWith("he") || objectId.startsWith("css")) {
            by = By.cssSelector(objectId);
        } else {
            by = By.id(objectId);
        }
        return by;
    }
}
