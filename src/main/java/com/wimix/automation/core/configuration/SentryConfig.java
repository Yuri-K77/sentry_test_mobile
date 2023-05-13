package com.wimix.automation.core.configuration;

import org.apache.commons.configuration2.Configuration;

import java.util.Objects;

import static com.wimix.automation.core.utils.DateTimeUtils.getZonedDateAndTime;

public class SentryConfig {

    private static SentryConfig instance;
    private final Configuration configuration;
    private String sessionsId;
    private static String appVersion;
    private static String appUrl;
    private static String sessionStartTime;

    public SentryConfig() {
        String env = System.getProperty("sentry.env");
        if (env == null) {
            env = "prod";
        }
        configuration = new ConfigurationBuilder()
                .withEnvironmentProperties()
                .withSystemProperties()
                .withClassPathProperties("/sentry_mobile_" + env + "-config.properties")
                .withClassPathProperties("/sentry_mobile_application.properties")
                .withClassPathProperties("/sentry_mobile_browserstack.properties")
                .build();
    }

    private static SentryConfig getInstance() {
        if (instance == null) {
            instance = new SentryConfig();
        }
        return instance;
    }

    public static String getString(String key) {
        return getInstance().configuration.getString(key);
    }

    public static boolean isBrowserStack() {
        return Objects.equals(getString("runType"), "bs");
    }

    public static void addCurrentSessionId(String sessionId) {
        getInstance().sessionsId = sessionId;
    }

    public static String getCurrentSessionId() {
        return (String) getInstance().sessionsId;
    }

    public static String getAppiumUrl() {
        return getInstance().configuration.getString("appium.url");
    }

    public static String getAppiumPort() {
        return getInstance().configuration.getString("appium.port");
    }

    public static String getApplication() {
        return getInstance().configuration.getString("sentry.application");
    }

    public static String getPackage() {
        return getInstance().configuration.getString("sentry.package");
    }

    public static String getDeviceName() {
        return getInstance().configuration.getString("sentry.device.name");
    }

    public static String getDeviceOsVersion() {
        return getInstance().configuration.getString("sentry.device.os.version");
    }

    public static String getClientIdWeb() {
        return getInstance().configuration.getString("sentry.clientIdWeb");
    }

    public static String getClientSecretWeb() {
        return getInstance().configuration.getString("sentry.clientSecretWeb");
    }

    public static String getTestEmail() {
        return getInstance().configuration.getString("sentryUser.testEmail");
    }

    public static String getTestPassword() {
        return getInstance().configuration.getString("sentryUser.testPassword");
    }

    public static String getRealEmail() {
        return getInstance().configuration.getString("sentryUser.realEmail");
    }

    public static String getRealPassword() {
        return getInstance().configuration.getString("sentryUser.realPassword");
    }

    public static String getBsUserName() {
        return getInstance().configuration.getString("bs.userName");
    }

    public static String getBsAccessKey() {
        return getInstance().configuration.getString("bs.accessKey");
    }

    public static String getBsUrl() {
        return getInstance().configuration.getString("bs.baseURL");
    }

    public static String getBsServer() {
        return getInstance().configuration.getString("bs.server");
    }

    public static String getBsProjectName() {
        return getInstance().configuration.getString("bs.projectName");
    }

    public static String getBsAppVersion() {
        return appVersion;
    }

    public static void setBsAppVersion(String value) {
        appVersion = value;
    }

    public static String getBsAppUrl() {
        return appUrl;
    }

    public static void setBsAppUrl(String value) {
        appUrl = value;
    }

    public static String getBsSessionStartTime() {
        return sessionStartTime;
    }

    public static void setBsSessionZonedStartTime() {
        sessionStartTime = getZonedDateAndTime();
    }

    public static String getClientId() {
        return getInstance().configuration.getString("client_id");
    }

    public static String getClientSecretMobile() {
        return getInstance().configuration.getString("client_secret");
    }

    public static String getDeviceTimeZone() {
        return getInstance().configuration.getString("device.timezone");
    }

    public static Long getImplicitlyWait() {
        return Long.valueOf(getInstance().configuration.getString("driver.implicitly.wait"));
    }

    public static Long getExplicitlyWait() {
        return Long.valueOf(getInstance().configuration.getString("driver.explicitly.wait"));
    }
}