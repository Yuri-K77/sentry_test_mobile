package com.wimix.automation.ui.screens;

import com.wimix.automation.ui.actions.MobileScreenActionManager;
import com.wimix.automation.ui.components.AbstractTabs;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

public abstract class AbstractScreen {

    public final AndroidDriver driver;
    public final MobileScreenActionManager mobileScreenActionManager;

    public AbstractScreen(AndroidDriver driver) {
        this.driver = driver;
        mobileScreenActionManager = new MobileScreenActionManager(driver);
    }

    public abstract AbstractScreen openScreen();

    public abstract boolean isScreenOpen();

    public abstract AbstractScreen waitScreenOpen();

    public final boolean waitIsScreenOpen() {
        try {
            waitScreenOpen();
            return true;
        } catch (TimeoutException e) {
            return false;
        }
    }

    public WebElement getScreenTabItem(AbstractTabs tabItem) {
        return driver.findElement(By.xpath("//android.widget.LinearLayout[@content-desc='" + tabItem.getValue() + "']"));
    }

    public void selectScreenTabItem(AbstractTabs tabItem) {
        if (!isScreenTabItemActive(tabItem)) {
            getScreenTabItem(tabItem).click();
        }
    }

    public boolean isScreenTabItemActive(AbstractTabs tabItem) {
        WebElement tabElement = getScreenTabItem(tabItem);
        return tabElement.getAttribute("selected").contains("true");
    }
}