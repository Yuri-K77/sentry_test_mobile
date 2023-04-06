package com.wimix.automation.ui.screens;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@SuppressWarnings("UnusedReturnValue")
public class LoginScreen extends AbstractScreen {

    @FindBy(id = "inputLoginEmail")
    public WebElement emailField;

    @FindBy(id = "inputLoginPassword")
    public WebElement passwordField;

    @FindBy(id = "tvForgotPassword")
    public WebElement forgotPasswordTextView;

    @FindBy(id = "btnSignIn")
    public WebElement loginButton;

    @FindBy(id = "text_input_error_icon")
    public WebElement validationError;

    public LoginScreen(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @Override
    public LoginScreen openScreen() {
        return this;
    }

    @Override
    public boolean isScreenOpen() {
        return mobileScreenActionManager.isElementDisplayed(loginButton);
    }

    @Override
    public LoginScreen waitScreenOpen() {
        mobileScreenActionManager.waitGetVisibleElement(loginButton);
        return this;
    }

    public LoginScreen inputDataInEmailField(String text) {
        mobileScreenActionManager.inputDataInField(emailField, text);
        return this;
    }

    public LoginScreen inputDataInPasswordField(String text) {
        mobileScreenActionManager.inputDataInField(passwordField, text);
            return this;
    }

    public LoginScreen clickForgotPassword() {
        mobileScreenActionManager.clickOnElement(forgotPasswordTextView);
        return this;
    }

    public LoginScreen clickLoginButton() {
        mobileScreenActionManager.clickOnElementWithPollingInterval(loginButton);
        return this;
    }

    public boolean isValidationErrorDisplayed() {
        mobileScreenActionManager.isElementDisplayed(validationError);
        return true;
    }
}