package com.wimix.automation.ui.screens;

import com.wimix.automation.ui.actions.MobileScreenActionManager;
import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.support.PageFactory;

public class MarketWatchScreen extends AbstractScreen {

    public MarketWatchScreen(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @Override
    public MarketWatchScreen openScreen() {
        return null;
    }

    @Override
    public boolean isScreenOpen() {
        return false;
    }

    @Override
    public MarketWatchScreen waitScreenOpen() {
        return null;
    }

    public static class TutorialPopUp extends AbstractElement {

        public TutorialPopUp(AndroidDriver driver, MobileScreenActionManager mobileScreenActionManager) {
            super(driver.findElement(By.xpath("//*[@resource-id='com.sentryd.trade:id/guide_container']")), mobileScreenActionManager);
        }

        public String getTextFromTutorialPopUp() {
            return findElement(By.xpath("//*[@text='Real Options for Real Traders']")).getText();
        }

        public boolean waitIsPopUpOpen() {
            mobileScreenActionManager.waitGetVisibleElement(findElement(By.xpath("//*[@text='Real Options for Real Traders']")));
            return true;
        }
    }
}