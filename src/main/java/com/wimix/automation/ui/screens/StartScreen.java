package com.wimix.automation.ui.screens;

import com.wimix.automation.ui.actions.MobileScreenActionManager;
import io.appium.java_client.android.AndroidDriver;
import lombok.*;
import org.awaitility.Awaitility;
import org.openqa.selenium.*;
import org.openqa.selenium.support.*;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import static org.openqa.selenium.By.xpath;

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

    public static class LoginScreen extends AbstractScreen {

        @FindBy(xpath = "//*[@text='WELCOME!']")
        public WebElement welcomeTextView;

        @FindBy(xpath = "//androidx.compose.ui.platform.ComposeView/android.view.View/android.widget.EditText[1]")
        public WebElement emailField;

        @FindBy(xpath = "//androidx.compose.ui.platform.ComposeView/android.view.View/android.widget.EditText[2]")
        public WebElement passwordField;

        @FindBy(xpath = "//android.widget.ImageView[@content-desc='Show/Hide password']")
        public WebElement showHidePassword;

        @FindBy(xpath = "//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.ViewGroup[2]/android.widget.Switch")
        public WebElement rememberUserNameSwitch;

        @FindBy(xpath = "//*[@text='LOG IN']")
        public WebElement loginButton;

        @FindBy(xpath = "//*[@text='FORGOT PASSWORD?']")
        public WebElement forgotPasswordTextView;

        @FindBy(xpath = "//*[@text='SIGN UP']")
        public WebElement signUpTextView;

        public LoginScreen(AndroidDriver driver) {
            super(driver);
            PageFactory.initElements(driver, this);
        }

        @Override
        public LoginScreen openScreen() {
            new StartScreen(driver)
                    .openScreen()
                    .openLoginScreen();
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

        public LoginScreen clickShowHidePasswordButton() {
            mobileScreenActionManager.clickOnElement(showHidePassword);
            return this;
        }

        public LoginScreen clickRememberUserNameSwitch() {
            mobileScreenActionManager.clickOnElement(rememberUserNameSwitch);
            return this;
        }

        public ChooseAccountPopUp clickLoginButton() {
            mobileScreenActionManager.clickOnElement(loginButton);
            return new ChooseAccountPopUp(driver, mobileScreenActionManager);
        }

        public ForgotPasswordScreen openForgotPasswordScreen() {
            mobileScreenActionManager.clickOnElement(forgotPasswordTextView);
            return new ForgotPasswordScreen(driver);
        }

        public RealAccountScreen clickSignUpTextView() {
            mobileScreenActionManager.clickOnElement(signUpTextView);
            return new RealAccountScreen(driver);
        }
    }

    public static class ChooseAccountPopUp extends AbstractElement {

        public ChooseAccountPopUp(AndroidDriver driver, MobileScreenActionManager mobileScreenActionManager) {
            super(driver.findElement(By.xpath("//*[@resource-id='android:id/content']")), mobileScreenActionManager);
        }

        public boolean isChooseAccountPopUpOpen() {
            return mobileScreenActionManager.isElementDisplayed(getContainer());
        }

        public LoginScreen clickCloseChooseAccountScreen() {
            mobileScreenActionManager.clickOnElement(findElement(xpath("//android.widget.ImageView[@content-desc='Go back']")));
            return new LoginScreen(mobileScreenActionManager.driver);
        }

        //TODO
        private WebElement getAccountItem(AccountItem accountItem) {
            AtomicReference<WebElement> element = new AtomicReference<>();
            Awaitility
                    .given()
                    .pollInterval(100, TimeUnit.MILLISECONDS)
                    .atMost(5000, TimeUnit.MILLISECONDS)
                    .pollInSameThread()
                    .ignoreExceptions()
                    .until(() -> {
                        ChooseAccountPopUp chooseAccountPopUp = new ChooseAccountPopUp(mobileScreenActionManager.driver, mobileScreenActionManager);
                        element.set(chooseAccountPopUp.getContainer());
                        switch (accountItem) {
                            case REAL -> {
                                chooseAccountPopUp.findElement(xpath("//*[text()='REAL']"));
                            }
                            case DEMO -> {
                                chooseAccountPopUp.findElement(xpath("//*[text()='DEMO']"));
                            }
                            default -> throw new IllegalArgumentException("This item doesn't exist");
                        }
                        return true;
                    });
            return element.get();
        }

        public MarketWatchScreen selectAccountItem(AccountItem accountItem) {
            getAccountItem(accountItem).click();
            return new MarketWatchScreen(mobileScreenActionManager.driver);
        }

        @AllArgsConstructor
        @Getter
        public enum AccountItem {
            REAL("REAL"),
            DEMO("DEMO");

            private final String value;
        }
    }

    public static class ForgotPasswordScreen extends AbstractScreen {

        @FindBy(xpath = "//*[@text='Forgot your password?']")
        public WebElement forgotYourPasswordTextView;

        @FindBy(xpath = "//android.widget.ImageView[@content-desc='Go back']")
        public WebElement closeButton;

        @FindBy(xpath = "//*[@text='Submit']")
        public WebElement submitButton;

        @FindBy(xpath = "//*[@text='Return to Login']")
        public WebElement returnToLoginButton;

        @FindBy(xpath = "//android.widget.ImageView[@content-desc='Go back']")
        public WebElement cancelButton;

        @FindBy(xpath = "//androidx.compose.ui.platform.ComposeView/android.view.View/android.view.ViewGroup[2]/android.widget.FrameLayout/android.webkit.WebView/android.webkit.WebView/android.view.View/android.view.View[2]/android.view.View[2]/android.view.View/android.view.View[1]/android.view.View/android.view.View/android.view.View/android.widget.EditText")
        public WebElement emailField;

        public ForgotPasswordScreen(AndroidDriver driver) {
            super(driver);
            PageFactory.initElements(driver, this);
        }

        @Override
        public ForgotPasswordScreen openScreen() {
            new StartScreen(driver)
                    .openScreen()
                    .openLoginScreen()
                    .openForgotPasswordScreen();
            return waitScreenOpen();
        }

        @Override
        public boolean isScreenOpen() {
            return mobileScreenActionManager.isElementDisplayed(forgotYourPasswordTextView);
        }

        @Override
        public ForgotPasswordScreen waitScreenOpen() {
            mobileScreenActionManager.waitGetVisibleElement(forgotYourPasswordTextView);
            return this;
        }

        public LoginScreen clickCloseButton() {
            mobileScreenActionManager.clickOnElement(closeButton);
            return new LoginScreen(driver);
        }

        public ForgotPasswordScreen clickSubmitButton() {
            mobileScreenActionManager.clickOnElement(submitButton);
            return this;
        }

        public LoginScreen clickReturnToLoginButton() {
            mobileScreenActionManager.clickOnElement(returnToLoginButton);
            return new LoginScreen(driver);
        }

        public LoginScreen clickCancelButton() {
            mobileScreenActionManager.clickOnElement(cancelButton);
            return new LoginScreen(driver);
        }

        public ForgotPasswordScreen inputDataInEmailField(String text) {
            mobileScreenActionManager.inputDataInField(emailField, text);
            return this;
        }

        public String getTextFromConfirmationMessage() {
            return driver.findElement(By.xpath("//*[@text='Thank you. We have sent you an email with instructions for resetting your password.']")).getText();
        }
    }

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

    public static class RealAccountScreen extends AbstractScreen {

        @FindBy(xpath = "//*[@text='Real Account']")
        public WebElement realAccountTextView;

        @FindBy(xpath = "//android.widget.ImageView[@content-desc='Go back']")
        public WebElement closeButton;

        @Override
        public RealAccountScreen openScreen() {
            new StartScreen(driver)
                    .openScreen()
                    .openLoginScreen()
                    .clickSignUpTextView();
            return waitScreenOpen();
        }

        @Override
        public boolean isScreenOpen() {
            return mobileScreenActionManager.isElementDisplayed(realAccountTextView);
        }

        @Override
        public RealAccountScreen waitScreenOpen() {
            mobileScreenActionManager.waitGetVisibleElement(realAccountTextView);
            return this;
        }

        public RealAccountScreen(AndroidDriver driver) {
            super(driver);
            PageFactory.initElements(driver, this);
        }

        public String getTextFromRealAccount() {
            return driver.findElement(By.xpath("//*[@text='Real Account']")).getText();
        }

        public LoginScreen clickCloseButton() {
            mobileScreenActionManager.clickOnElement(closeButton);
            return new LoginScreen(driver);
        }
    }
}