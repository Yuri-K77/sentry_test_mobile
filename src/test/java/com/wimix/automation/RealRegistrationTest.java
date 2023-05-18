package com.wimix.automation;

import com.wimix.automation.core.utils.DataGenerator;
import com.wimix.automation.ui.screens.MarketWatchScreen;
import com.wimix.automation.ui.screens.StartScreen;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class RealRegistrationTest extends BaseTest {

    private StartScreen.RealAccountScreen realAccountScreen;
    private final String email = DataGenerator.generateRandomEmail();
    private final String password = "Password" + DataGenerator.randomString(5);
    private final String firstName = "Fn" + DataGenerator.randomString(3);
    private final String lastName = "Ln" + DataGenerator.randomString(3);

    @BeforeAll
    void beforeAll() {
        realAccountScreen = new StartScreen(driver).openScreen().openRealAccountScreen();
    }


    //TODO
    @DisplayName("Email does not exist - after completing all the registration steps Marketwatch screen should be open")
    @Test
    void afterCompletingRealRegistrationMarketwatchScreenShouldBeOpen() {
        realAccountScreen.waitScreenOpen()
                .inputDataInEmailField(email)
                .inputDataInPasswordField(password)
                .clickCreateAccountButton()
                .inputDataInFirstNameField(firstName)
                .inputDataInLastNameField(lastName)
                .clickNextButton()
                .waitIsScreenOpen();
        //Assertions.assertTrue(new MarketWatchScreen(driver).waitIsScreenOpen());
    }
}