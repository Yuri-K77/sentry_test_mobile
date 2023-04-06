package com.wimix.automation.ui.actions;

import com.wimix.automation.core.driver.SentryAndroidDriverProvider;
import io.appium.java_client.android.AndroidDriver;
import org.awaitility.Awaitility;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

import static com.wimix.automation.core.configuration.SentryConfig.getExplicitlyWait;
import static com.wimix.automation.core.configuration.SentryConfig.getImplicitlyWait;

@SuppressWarnings({"UnusedReturnValue", "FieldCanBeLocal"})
public class MobileScreenActionManager {

    public final AndroidDriver driver;
    private final WebDriverWait driverWait;

    public MobileScreenActionManager(AndroidDriver driver) {
        this.driver = driver;
        this.driverWait = new WebDriverWait(driver, Duration.ofSeconds(getExplicitlyWait()));
    }

    public WebElement waitGetVisibleElement(WebElement element) {
        return driverWait.until(ExpectedConditions.visibilityOf(element));
    }

    public void waitElementInvisible(WebElement element) {
        try {
            element.isDisplayed();
        } catch (NoSuchElementException e) {
            return;
        }
        driverWait.until(ExpectedConditions.invisibilityOf(element));
    }

    public WebElement waitGetClickableElement(WebElement element) {
        return driverWait.until(ExpectedConditions.elementToBeClickable(element));
    }

    public void clickOnElement(WebElement element) {
        waitGetClickableElement(element).click();
    }

    public void clickOnElementWithPollingInterval(WebElement element) {
        Awaitility
                .given()
                .pollInterval(100, TimeUnit.MILLISECONDS)
                .atMost(2000, TimeUnit.MILLISECONDS)
                .pollInSameThread()
                .ignoreExceptions()
                .until(() -> {
                    clickOnElement(element);
                    return true;
                });
    }

    public void inputDataInField(WebElement element, String text) {
        clickOnElement(element);
        SentryAndroidDriverProvider.getAndroidDriver().hideKeyboard();
        element.clear();
        element.sendKeys(text);
    }

    public boolean isElementDisplayed(WebElement element) {
        driver.manage().timeouts().implicitlyWait(Duration.ZERO);
        try {
            return element.isDisplayed();
        } catch (NoSuchElementException | StaleElementReferenceException e) {
            return false;
        } finally {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(getImplicitlyWait()));
        }
    }

    public boolean waitIsElementDisplayed(WebElement element) {
        try {
            waitGetVisibleElement(element);
            return true;
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}