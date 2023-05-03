package com.wimix.automation;

import com.wimix.automation.ui.screens.StartScreen;
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
                .clickLoginButton();
        //Assertions.assertTrue(new MyTasksScreen(driver).waitIsScreenOpen());
    }
}