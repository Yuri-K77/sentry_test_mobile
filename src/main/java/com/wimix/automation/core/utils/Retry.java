package com.wimix.automation.core.utils;

import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

public class Retry {

    private static void sleep(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public static void whileTrue(int pollingIntervalMs, int timeoutMilliseconds, Supplier<Boolean> supplier, String exceptionMsg) throws TimeoutException {
        long timeStart = System.currentTimeMillis();
        while (supplier.get()) {
            if (System.currentTimeMillis() - timeStart > timeoutMilliseconds) {
                throw new TimeoutException(String.format("Time out after %d ms: ", timeoutMilliseconds) + exceptionMsg);
            }
            sleep(pollingIntervalMs);
        }
    }
}