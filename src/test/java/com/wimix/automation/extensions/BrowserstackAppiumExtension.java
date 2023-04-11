package com.wimix.automation.extensions;

import com.wimix.automation.core.configuration.SentryConfig;
import com.wimix.automation.core.driver.AppiumService;
import com.wimix.automation.core.driver.DriverFactory;
import com.wimix.automation.core.driver.SentryAndroidDriverProvider;
import com.wimix.automation.core.utils.AndroidDebugBridgeCommandExecutor;
import com.wimix.automation.rest.BrowserStackClient;
import com.wimix.automation.rest.dto.UploadedApp;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.extension.*;
import org.openqa.selenium.NoSuchElementException;

import static org.junit.jupiter.api.extension.ExtensionContext.Namespace.GLOBAL;

@Slf4j
public class BrowserstackAppiumExtension implements BeforeAllCallback, AfterAllCallback, ExtensionContext.Store.CloseableResource, TestWatcher, LifecycleMethodExecutionExceptionHandler {

    private Boolean isAnyTestFailed = false;

    public BrowserStackClient sessionClient;

    private static boolean started = false;

    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        if (!isAnyTestFailed) {
            isAnyTestFailed = true;
        }
    }

    //this method catches any errors/exceptions on methods which are not annotated with @Test
    @Override
    public void handleBeforeAllMethodExecutionException(final ExtensionContext context, final Throwable ex) throws Throwable {
        if (!isAnyTestFailed) {
            isAnyTestFailed = true;
        }
        log.error(ex.getMessage());
        throw ex;
    }

    //this method executes before each test class
    @Override
    public void beforeAll(ExtensionContext extensionContext) throws Exception {
        if (!started) {
            extensionContext.getRoot().getStore(GLOBAL).put("test", this);
            started = true;
            if (SentryConfig.isBrowserStack()) {

                String appUrl = BrowserStackClient.uploadApp();
                UploadedApp uploadedApp = BrowserStackClient.getListUploadedApps().stream()
                        .filter(app -> app.getAppUrl().equals(appUrl))
                        .findAny()
                        .orElseThrow(() -> new NoSuchElementException("This app is not existed: " + appUrl));
                SentryConfig.setBsAppVersion(uploadedApp.getAppVersion());
                SentryConfig.setBsAppUrl(uploadedApp.getAppUrl());
                SentryConfig.setBsSessionZonedStartTime();
                DriverFactory.configureBrowserStackDriver();
            } else {
                AppiumService.runAppiumServer();
                DriverFactory.configureDriver();
            }
        }
        if (SentryConfig.isBrowserStack()) {
            this.sessionClient = new BrowserStackClient(SentryAndroidDriverProvider.getAndroidDriver().getSessionId().toString());
            extensionContext.getTestClass().ifPresent(tc -> {
                this.sessionClient.updateSession("{\"name\":\"" + tc.getSimpleName() + "\"}");
            });
        }
    }

    //this method executes after each test class
    @Override
    public void afterAll(ExtensionContext extensionContext) throws Exception {
        if (SentryConfig.isBrowserStack()) {
            if (isAnyTestFailed) {
                this.sessionClient.updateSession("{\"status\":\"" + TestStatuses.FAILED.value + "\", \"reason\":\"\"}");
            } else {
                this.sessionClient.updateSession("{\"status\":\"" + TestStatuses.PASSED.value + "\", \"reason\":\"\"}");
            }
        }
    }

    //this method executes once after all test classes
    @Override
    public void close() {
        if (SentryConfig.isBrowserStack()) {
            BrowserStackClient.deleteUploadedApk(SentryConfig.getBsAppUrl());
        } else {
            AndroidDebugBridgeCommandExecutor.saveCrashLogToFile();
            AppiumService.stopAppiumServer();
        }
    }

    @AllArgsConstructor
    @Getter
    private enum TestStatuses {
        PASSED("passed"),
        FAILED("failed");

        private final String value;
    }
}
