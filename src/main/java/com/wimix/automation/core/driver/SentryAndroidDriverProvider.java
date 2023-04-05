package com.wimix.automation.core.driver;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.remote.RemoteWebDriver;


public class SentryAndroidDriverProvider {

    private static final ThreadLocal<RemoteWebDriver> androidDriverDelegate = new ThreadLocal<>();

    public static void setAndroidDriver(RemoteWebDriver driver) {
        androidDriverDelegate.set(driver);
    }

    public static AndroidDriver getAndroidDriver() {
        return (AndroidDriver) androidDriverDelegate.get();
    }
}