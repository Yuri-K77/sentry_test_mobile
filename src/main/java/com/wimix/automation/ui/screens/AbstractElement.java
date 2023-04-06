package com.wimix.automation.ui.screens;

import com.wimix.automation.ui.actions.MobileScreenActionManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

public abstract class AbstractElement {

    private final WebElement container;
    public final MobileScreenActionManager mobileScreenActionManager;

    public AbstractElement(WebElement container, MobileScreenActionManager mobileScreenActionManager) {
        this.container = container;
        this.mobileScreenActionManager = mobileScreenActionManager;
    }

    public WebElement getContainer() {
        return container;
    }

    public WebElement findElement(By locator) {
        return container.findElement(locator);
    }
}