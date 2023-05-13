package com.wimix.automation;

import com.wimix.automation.core.utils.Retry;
import com.wimix.automation.ui.screens.StartScreen;
import com.wimix.automation.ui.screens.StartScreen.*;
import lombok.SneakyThrows;
import org.junit.jupiter.api.*;

import static com.wimix.automation.core.configuration.SentryConfig.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class StartScreenTest extends BaseTest {

    private StartScreen startScreen;

    @BeforeAll
    void beforeAll() {
        startScreen = new StartScreen(driver).openScreen();
    }

    @SneakyThrows
    @AfterEach
    void afterEach() {
        Retry.whileTrue(100, 5000, () -> {
            if (!startScreen.isScreenOpen()) {
                driver.navigate().back();
                return true;
            } else {
                return false;
            }
        }, "Unexpectedly, Start screen does not open");
    }

    @DisplayName("After successful login, Marketwatch screen should be open")
    @Test
    void makeLogin() {
        startScreen.waitScreenOpen()
                .openLoginScreen()
                .inputDataInEmailField(getTestEmail())
                .inputDataInPasswordField(getTestPassword())
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
                .inputDataInEmailField(getRealEmail())
                .clickSubmitButton();
        Assertions.assertEquals(expectedResult, forgotPasswordScreen.getTextFromConfirmationMessage());
    }

    @DisplayName("After tapping SIGN UP button, Real Account screen should be opened")
    @Test
    void afterClickingSignUpButtonRealAccountScreenShouldBeOpened() {
        String expectedResult = "Real Account";
        RealAccountScreen realAccountScreen = startScreen.waitScreenOpen()
                .openLoginScreen()
                .clickSignUpTextView();
        Assertions.assertEquals(expectedResult, realAccountScreen.getTextFromRealAccount());
    }
}