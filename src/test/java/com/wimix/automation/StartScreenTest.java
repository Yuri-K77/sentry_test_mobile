package com.wimix.automation;

import com.wimix.automation.ui.screens.StartScreen;
import com.wimix.automation.ui.screens.StartScreen.ForgotPasswordScreen;
import org.junit.jupiter.api.*;

import static com.wimix.automation.core.configuration.SentryConfig.getEmail;
import static com.wimix.automation.core.configuration.SentryConfig.getPassword;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StartScreenTest extends BaseTest {

    private StartScreen startScreen;

    @BeforeAll
    void beforeAll() {
        startScreen = new StartScreen(driver);

    }

    @DisplayName("After successful login, Marketwatch screen should be open")
    @Test
    void makeLogin() {
        startScreen.waitScreenOpen()
                .openLoginScreen()
                .inputDataInEmailField(getEmail())
                .inputDataInPasswordField(getPassword())
                .clickShowHidePasswordButton()
                .clickShowHidePasswordButton()
                .clickRememberUserNameSwitch()
                .clickRememberUserNameSwitch()
                .clickLoginButton()
                .selectAccountItem(StartScreen.ChooseAccountPopUp.AccountItem.REAL);
        //Assertions.assertTrue(new MyTasksScreen(driver).waitIsScreenOpen());
    }

    @DisplayName("After entering and submitting an email, link should be received")
    @Test
    void afterEnteringAndSubmittingAnEmailLinkShouldBeReceived() {
        String expectedResult = "Thank you. We have sent you an email with instructions for resetting your password.";
        ForgotPasswordScreen forgotPasswordScreen = startScreen.waitScreenOpen()
                .openLoginScreen()
                .openForgotPasswordScreen()
                .inputDataInEmailField(getEmail())
                .clickSubmitButton();
        Assertions.assertEquals(expectedResult, forgotPasswordScreen.getTextFromConfirmationMessage());
    }
}