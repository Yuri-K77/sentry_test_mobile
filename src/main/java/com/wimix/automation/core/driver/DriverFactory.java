package com.wimix.automation.core.driver;

import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;

import static com.wimix.automation.core.configuration.SentryConfig.*;

public class DriverFactory {

    private static final String APP_DIRECTORY = "src/main/resources";

    private static final String APP_NAME = getApplication();

    private static final String APP_PACKAGE = getPackage();

    private static final String APP_FILE_ABSOLUTE_PATH = getAppFilePath();

    private static final String DEVICE_NAME = getDeviceName();

    private static final String OS_VERSION = getDeviceOsVersion();

    private static final String APPIUM_SERVER_URL = getAppiumUrl();

    private static final int APPIUM_SERVER_PORT = Integer.parseInt(getAppiumPort());

    private DriverFactory() {
    }

    public static void configureDriver() {
        UiAutomator2Options options = new UiAutomator2Options()
                .autoGrantPermissions()
                .setIsHeadless(false)
                .setDeviceName(DEVICE_NAME)
                .setApp(APP_FILE_ABSOLUTE_PATH)
                .setPlatformName("Android")
                .setPlatformVersion(OS_VERSION)
                .setAppPackage(APP_PACKAGE)
                .setAppActivity("zegoal.com.zegoal.presentation.ui.start.StartActivity")
                .eventTimings();
        try {
            AndroidDriver driver = new AndroidDriver(new URL("http://" + APPIUM_SERVER_URL + ":" + APPIUM_SERVER_PORT), options);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(getImplicitlyWait()));
            driver.hideKeyboard();
            SentryAndroidDriverProvider.setAndroidDriver(driver);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Применяя ThreadLocal для хранения объекта AndroidDriver,
     * мы храним для каждого потока(тестового класса который выполняется параллельно),
     * свой объект драйвера. При этом, мы не создаем целую кучу объектов типа DriverFactory,
     * чтобы там в поле хранить драйвер. У нас все основные методы в DriverFactory являются статическими,
     * также как com.wimix.automation.core.driver.ZegoalAndroidDriverProvider#androidDriverDelegate.
     * Сколько бы не было потоков(тестовых классов работающих параллельно), у нас объект ThreadLocal
     * хранит изолированно для каждого потока свой драйвер.
     */

    public static void configureBrowserStackDriver() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("project", getBsProjectName());
        desiredCapabilities.setCapability("build", getBsAppVersion() + "  [" + getBsSessionStartTime() + "]");
        desiredCapabilities.setCapability("name", getBsAppVersion());
        desiredCapabilities.setCapability("browserstack.debug", true);
        desiredCapabilities.setCapability("app", getBsAppUrl());
        desiredCapabilities.setCapability("deviceName", "Samsung Galaxy S22");
        desiredCapabilities.setCapability("os_version", "12.0");
        desiredCapabilities.setCapability("autoGrantPermissions", "true");

        try {
            AndroidDriver driver = new AndroidDriver(new URL("http://" + getBsUserName() + ":" + getBsAccessKey() + "@" + getBsServer() + "/wd/hub"), desiredCapabilities);
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(getImplicitlyWait()));
            driver.hideKeyboard();
            SentryAndroidDriverProvider.setAndroidDriver(driver);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    private static String getAppFilePath() {
        File appDir = new File(APP_DIRECTORY);
        return new File(appDir, APP_NAME).getAbsolutePath();
    }
}