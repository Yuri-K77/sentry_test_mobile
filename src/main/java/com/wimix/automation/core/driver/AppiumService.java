package com.wimix.automation.core.driver;

import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;

import java.time.Duration;

import static com.wimix.automation.core.configuration.SentryConfig.getAppiumPort;
import static com.wimix.automation.core.configuration.SentryConfig.getAppiumUrl;

public class AppiumService {

    private static final String APPIUM_SERVER_URL = getAppiumUrl();
    private static final int APPIUM_SERVER_PORT = Integer.parseInt(getAppiumPort());
    private static AppiumDriverLocalService service = null;

    private AppiumService() {
    }

    public static void runAppiumServer() {
        service = new AppiumServiceBuilder()
                .withIPAddress(APPIUM_SERVER_URL)
                .usingPort(APPIUM_SERVER_PORT)
                .withTimeout(Duration.ofSeconds(600))
                .build();
        service.start();
    }

    public static void stopAppiumServer() {
        if (service != null) {
            service.stop();
            service = null;
        }
    }
}