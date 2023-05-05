package com.wimix.automation.ui.screens;

import io.appium.java_client.android.AndroidDriver;
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
}