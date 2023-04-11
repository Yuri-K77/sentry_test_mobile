package com.wimix.automation;

import com.wimix.automation.core.driver.SentryAndroidDriverProvider;
import com.wimix.automation.core.utils.AndroidDebugBridgeCommandExecutor;
import com.wimix.automation.extensions.BrowserstackAppiumExtension;
import com.wimix.automation.extensions.LocalTestHandlerExtension;
import io.appium.java_client.android.AndroidDriver;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ExtendWith({BrowserstackAppiumExtension.class, LocalTestHandlerExtension.class})
public abstract class BaseTest {

    AndroidDriver driver;

    @BeforeAll
    void setUp() {
        driver = SentryAndroidDriverProvider.getAndroidDriver();
        AndroidDebugBridgeCommandExecutor.setLocationEnabled(true);
    }

    @AfterAll
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}