package com.wimix.automation.ui.screens;

import io.appium.java_client.android.AndroidDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

@SuppressWarnings("UnusedReturnValue")
public class StartScreen extends AbstractScreen {

    @FindBy(xpath = "//*[@text='ALREADY HAVE AN ACCOUNT? LOG IN']")
    public WebElement alreadyHaveAnAccountTextView;

    @FindBy(xpath = "//*[@text='FREE DEMO']")
    public WebElement freeDemoButton;

    @FindBy(xpath = "//*[@text='OPEN ACCOUNT']")
    public WebElement openAccountButton;

    public StartScreen(AndroidDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
    }

    @Override
    public StartScreen openScreen() {
        return this;
    }

    @Override
    public boolean isScreenOpen() {
        return mobileScreenActionManager.isElementDisplayed(freeDemoButton);
    }

    @Override
    public StartScreen waitScreenOpen() {
        mobileScreenActionManager.waitGetVisibleElement(freeDemoButton);
        return this;
    }

    public LoginScreen openLoginScreen() {
        mobileScreenActionManager.clickOnElement(alreadyHaveAnAccountTextView);
        return new LoginScreen(driver);
    }

    public DemoAccountScreen openDemoAccountScreen() {
        mobileScreenActionManager.clickOnElement(freeDemoButton);
        return new DemoAccountScreen(driver);
    }

    public RealAccountScreen openRealAccountScreen() {
        mobileScreenActionManager.clickOnElement(openAccountButton);
        return new RealAccountScreen(driver);
    }

    //TODO
    public static class LoginScreen extends AbstractScreen {

        @FindBy(xpath = "//*[@text='WELCOME!']")
        public WebElement welcomeTextView;

        @FindBy(xpath = "//*[@text='Username or Email']")
        public WebElement emailField;

        @FindBy(xpath = "//*[@text='Password']")
        public WebElement passwordField;

        @FindBy(xpath = "//*[@text='LOG IN']")
        public WebElement loginButton;

        @FindBy(xpath = "//*[@text='FORGOT PASSWORD?']")
        public WebElement forgotPasswordTextView;

        @FindBy(xpath = "//*[@text='SIGN UP?']")
        public WebElement signUpTextView;

        public LoginScreen(AndroidDriver driver) {
            super(driver);
            PageFactory.initElements(driver, this);
        }

        @Override
        public LoginScreen openScreen() {
            new StartScreen(driver).openScreen().openLoginScreen();
            return waitScreenOpen();
        }

        @Override
        public boolean isScreenOpen() {
            return mobileScreenActionManager.isElementDisplayed(welcomeTextView);
        }

        @Override
        public LoginScreen waitScreenOpen() {
            mobileScreenActionManager.waitGetVisibleElement(welcomeTextView);
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

        public LoginScreen clickLoginButton() {
            mobileScreenActionManager.clickOnElementWithPollingInterval(loginButton);
            return this;
        }

        public LoginScreen clickForgotPassword() {
            mobileScreenActionManager.clickOnElement(forgotPasswordTextView);
            return this;
        }

        public LoginScreen clickSignUp() {
            mobileScreenActionManager.clickOnElement(signUpTextView);
            return this;
        }
    }

    //TODO
    public static class DemoAccountScreen extends AbstractScreen {

        @Override
        public AbstractScreen openScreen() {
            return null;
        }

        @Override
        public boolean isScreenOpen() {
            return false;
        }

        @Override
        public AbstractScreen waitScreenOpen() {
            return null;
        }

        public DemoAccountScreen(AndroidDriver driver) {
            super(driver);
            PageFactory.initElements(driver, this);
        }
    }

    //TODO
    public static class RealAccountScreen extends AbstractScreen {

        @Override
        public AbstractScreen openScreen() {
            return null;
        }

        @Override
        public boolean isScreenOpen() {
            return false;
        }

        @Override
        public AbstractScreen waitScreenOpen() {
            return null;
        }

        public RealAccountScreen(AndroidDriver driver) {
            super(driver);
            PageFactory.initElements(driver, this);
        }
    }
}