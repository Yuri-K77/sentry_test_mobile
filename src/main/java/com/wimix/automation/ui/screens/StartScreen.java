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

    @FindBy(xpath = "//*[@content-desc='ALREADY HAVE AN ACCOUNT? LOG IN']")
    public WebElement alreadyHaveAnAccountTextView;

    @FindBy(xpath = "//*[@content-desc='FREE DEMO']")
    public WebElement freeDemoButton;

    @FindBy(xpath = "//*[@content-desc='OPEN ACCOUNT']")
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

        @FindBy(xpath = "//*[@content-desc='WELCOME!']")
        public WebElement welcomeTextView;

        @FindBy(xpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[4]/android.widget.EditText[1]")
        public WebElement emailField;

        @FindBy(xpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[4]/android.widget.EditText[2]")
        public WebElement passwordField;

        @FindBy(xpath = "//*[@content-desc='Guide']")
        public WebElement showHidePassword;

        @FindBy(xpath = "/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.FrameLayout/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View/android.view.View[4]/android.view.View[3]")
        public WebElement rememberUserNameSwitch;

        @FindBy(xpath = "//*[@content-desc='LOG IN']")
        public WebElement loginButton;

        @FindBy(xpath = "//*[@content-desc='FORGOT PASSWORD?']")
        public WebElement forgotPasswordTextView;

        @FindBy(xpath = "//*[@content-desc='SIGN UP']")
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
            super(driver.findElement(By.xpath("//*[@resource-id='android:id/content' and @class='android.widget.FrameLayout']")), mobileScreenActionManager);
        }

        public boolean isChooseAccountPopUpOpen() {
            return mobileScreenActionManager.isElementDisplayed(getContainer());
        }

        public LoginScreen clickCloseChooseAccountScreen() {
            mobileScreenActionManager.clickOnElement(findElement(xpath("//android.widget.ImageView[@content-desc='Go back']")));
            return new LoginScreen(mobileScreenActionManager.driver);
        }

        private WebElement getAccountItem(AccountItem accountItem) {
            AtomicReference<WebElement> element = new AtomicReference<>();
            Awaitility
                    .given()
                    .pollInterval(1000, TimeUnit.MILLISECONDS)
                    .atMost(10000, TimeUnit.MILLISECONDS)
                    .pollInSameThread()
                    .ignoreExceptions()
                    .until(() -> {
                        ChooseAccountPopUp chooseAccountPopUp = new ChooseAccountPopUp(mobileScreenActionManager.driver, mobileScreenActionManager);
                        element.set(chooseAccountPopUp.getContainer());
                        switch (accountItem) {
                            case REAL -> {
                                chooseAccountPopUp.getContainer().findElement(xpath("//*[@text='REAL']"));
                            }
                            case DEMO -> {
                                chooseAccountPopUp.getContainer().findElement(xpath("//*[@text='DEMO']"));
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

        public DemoAccountScreen(AndroidDriver driver) {
            super(driver);
            PageFactory.initElements(driver, this);
        }

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
    }

    public static class RealAccountScreen extends AbstractScreen {

        @FindBy(xpath = "//*[@text='Real Account']")
        public WebElement realAccountTextView;

        @FindBy(xpath = "//android.widget.ImageView[@content-desc='Go back']")
        public WebElement closeButton;

        @FindBy(xpath = "//androidx.compose.ui.platform.ComposeView/android.view.View/android.widget.EditText[1]")
        public WebElement emailField;

        @FindBy(xpath = "//androidx.compose.ui.platform.ComposeView/android.view.View/android.widget.EditText[2]")
        public WebElement passwordField;

        @FindBy(xpath = "//*[@text='CREATE ACCOUNT']")
        public WebElement createAccountButton;

        public RealAccountScreen(AndroidDriver driver) {
            super(driver);
            PageFactory.initElements(driver, this);
        }

        @Override
        public RealAccountScreen openScreen() {
            new StartScreen(driver)
                    .openScreen()
                    .openRealAccountScreen();
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

        public String getTextFromRealAccount() {
            return driver.findElement(By.xpath("//*[@text='Real Account']")).getText();
        }

        public LoginScreen clickCloseButton() {
            mobileScreenActionManager.clickOnElement(closeButton);
            return new LoginScreen(driver);
        }

        public RealAccountScreen inputDataInEmailField(String text) {
            mobileScreenActionManager.inputDataInField(emailField, text);
            return this;
        }

        public RealAccountScreen inputDataInPasswordField(String text) {
            mobileScreenActionManager.inputDataInField(passwordField, text);
            return this;
        }

        public SignUpForFreeScreen clickCreateAccountButton() {
            mobileScreenActionManager.clickOnElement(createAccountButton);
            return new SignUpForFreeScreen(driver);
        }
    }

    public static class SignUpForFreeScreen extends AbstractScreen {

        @FindBy(xpath = "//*[@text='Sign-Up for Free!']")
        public WebElement signUpForFreeView;

        @FindBy(xpath = "//*[@resource-id='question-4_247' and @class='android.widget.EditText']")
        public WebElement firstNameField;

        @FindBy(xpath = "//*[@resource-id='question-5_248' and @class='android.widget.EditText']")
        public WebElement lastNameField;

        @FindBy(xpath = "//*[@text='Next']")
        public WebElement nextButton;

        public SignUpForFreeScreen(AndroidDriver driver) {
            super(driver);
            PageFactory.initElements(driver, this);
        }

        @Override
        public SignUpForFreeScreen openScreen() {
            new StartScreen(driver)
                    .openScreen()
                    .openRealAccountScreen()
                    .clickCreateAccountButton();
            return waitScreenOpen();
        }

        @Override
        public boolean isScreenOpen() {
            return mobileScreenActionManager.isElementDisplayed(signUpForFreeView);
        }

        @Override
        public SignUpForFreeScreen waitScreenOpen() {
            mobileScreenActionManager.waitGetVisibleElement(signUpForFreeView);
            return this;
        }

        public SignUpForFreeScreen inputDataInFirstNameField(String text) {
            mobileScreenActionManager.inputDataInField(firstNameField, text);
            return this;
        }

        public SignUpForFreeScreen inputDataInLastNameField(String text) {
            mobileScreenActionManager.inputDataInField(lastNameField, text);
            return this;
        }

        public DateOfBirthScreen clickNextButton() {
            mobileScreenActionManager.clickOnElement(nextButton);
            return new DateOfBirthScreen(driver);
        }
    }

    public static class DateOfBirthScreen extends AbstractScreen {

        @FindBy(xpath = "//*[@text='Date of Birth']")
        public WebElement dateOfBirthView;

        @FindBy(xpath = "//*[@resource-id='native-datepicker' and @class='android.widget.Spinner']")
        public WebElement datePicker;

        @FindBy(xpath = "//*[@text='Next']")
        public WebElement nextButton;

        public DateOfBirthScreen(AndroidDriver driver) {
            super(driver);
            PageFactory.initElements(driver, this);
        }

        @Override
        public DateOfBirthScreen openScreen() {
            new StartScreen(driver)
                    .openScreen()
                    .openRealAccountScreen()
                    .clickCreateAccountButton()
                    .clickNextButton();
            return waitScreenOpen();
        }

        @Override
        public boolean isScreenOpen() {
            return mobileScreenActionManager.isElementDisplayed(dateOfBirthView);
        }

        @Override
        public DateOfBirthScreen waitScreenOpen() {
            mobileScreenActionManager.waitGetVisibleElement(dateOfBirthView);
            return this;
        }

        public DatePicker clickOpenCalendar() {
            mobileScreenActionManager.clickOnElement(datePicker);
            return new DatePicker(driver, mobileScreenActionManager);
        }

        public DateOfBirthScreen getSetButton() {
            mobileScreenActionManager.clickOnElement(driver.findElement(By.xpath("//*[@resource-id='android:id/button1' and @text='SET']")));
            return new DateOfBirthScreen(mobileScreenActionManager.driver);
        }

        public RegisteredAddressScreen clickNextButton() {
            mobileScreenActionManager.clickOnElement(nextButton);
            return new RegisteredAddressScreen(driver);
        }
    }

    public static class DatePicker extends AbstractElement {

        public DatePicker(AndroidDriver driver, MobileScreenActionManager mobileScreenActionManager) {
            super(driver.findElement(By.xpath("//*[@resource-id='android:id/content']")), mobileScreenActionManager);
        }

        public DateOfBirthScreen clickSetButton() {
            AtomicReference<DateOfBirthScreen> dateOfBirthScreenAtomicReference = new AtomicReference<>();
            Awaitility
                    .given()
                    .pollInterval(1000, TimeUnit.MILLISECONDS)
                    .atMost(10000, TimeUnit.MILLISECONDS)
                    .pollInSameThread()
                    .ignoreExceptions()
                    .until(() -> {
                        DateOfBirthScreen dateOfBirthScreen = new DateOfBirthScreen(mobileScreenActionManager.driver);
                        dateOfBirthScreenAtomicReference.set(dateOfBirthScreen);
                        dateOfBirthScreen.getSetButton();
                        return true;
                    });
            return dateOfBirthScreenAtomicReference.get();
        }
    }

    public static class RegisteredAddressScreen extends AbstractScreen {

        @FindBy(xpath = "//*[@text='Registered Address']")
        public WebElement registeredAddressView;

        @FindBy(xpath = "//*[@text='Next']")
        public WebElement nextButton;

        public RegisteredAddressScreen(AndroidDriver driver) {
            super(driver);
            PageFactory.initElements(driver, this);
        }

        @Override
        public RegisteredAddressScreen openScreen() {
            new StartScreen(driver)
                    .openScreen()
                    .openRealAccountScreen()
                    .clickCreateAccountButton()
                    .clickNextButton()
                    .clickNextButton();
            return waitScreenOpen();
        }

        @Override
        public boolean isScreenOpen() {
            return mobileScreenActionManager.isElementDisplayed(registeredAddressView);
        }

        @Override
        public RegisteredAddressScreen waitScreenOpen() {
            mobileScreenActionManager.waitGetVisibleElement(registeredAddressView);
            return this;
        }
    }
}