package com.wimix.automation.extensions;

import com.wimix.automation.core.driver.SentryAndroidDriverProvider;
import com.wimix.automation.core.utils.DateTimeUtils;
import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestWatcher;
import org.openqa.selenium.OutputType;

import java.io.File;

public class LocalTestHandlerExtension implements TestWatcher {

    @SneakyThrows
    @Override
    public void testFailed(ExtensionContext context, Throwable cause) {
        File source = SentryAndroidDriverProvider.getAndroidDriver().getScreenshotAs(OutputType.FILE);

        File target = new File("target/screenshots/" +
                context.getRequiredTestClass().getName() +
                "_" +
                context.getRequiredTestMethod().getName() +
                "_" +
                DateTimeUtils.getDateTimeString("dd_MM_yyyy-hh_mm_ss") +
                ".png");
        FileUtils.moveFile(source, target);
    }
}