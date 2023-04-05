package com.wimix.automation.core.driver;

import org.openqa.selenium.remote.RemoteWebDriver;

public class AndroidDriverDelegate<T> extends RemoteWebDriver {

    private T delegate;

    public T getDelegate() {
        return delegate;
    }

    public void setDelegate(T delegate) {
        this.delegate = delegate;
    }
}