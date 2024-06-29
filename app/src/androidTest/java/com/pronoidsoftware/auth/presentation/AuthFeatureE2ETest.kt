package com.pronoidsoftware.auth.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.pronoidsoftware.auth.presentation.login.LoginScreenRobot
import com.pronoidsoftware.auth.presentation.register.RegisterScreenRobot
import com.pronoidsoftware.core.TaskyAndroidTest
import com.pronoidsoftware.tasky.MainActivity
import com.pronoidsoftware.testutil.jvmtest.TestConstants
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AuthFeatureE2ETest : TaskyAndroidTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testAuthFeature_happyPath() {
        LoginScreenRobot(composeRule)
            .clickSignUp()

        RegisterScreenRobot(composeRule)
            .assertRegisterButtonIsDisabled()
            .assertNameIsInvalid()
            .clickName()
            .inputText("Val")
            .assertNameIsInvalid()
            .inputText(TestConstants.FULL_NAME)
            .assertNameIsValid()
            .assertRegisterButtonIsDisabled()
            .assertEmailIsInvalid()
            .clickEmail()
            .inputText("example")
            .assertEmailIsInvalid()
            .inputText(TestConstants.VALID_EMAIL)
            .assertEmailIsValid()
            .assertRegisterButtonIsDisabled()
            .assertPasswordIsInvalid()
            .clickPassword()
            .inputText("Invalid1")
            .assertPasswordIsInvalid()
            .inputText(TestConstants.VALID_PASSWORD)
            .assertPasswordIsValid()
            .showPassword()
            .hidePassword()
            .assertRegisterButtonIsEnabled()
            .clickRegisterButton()
            .assertRegistering()

        LoginScreenRobot(composeRule)
            .assertLoginButtonIsDisabled()
            .assertEmailIsInvalid()
            .clickEmail()
            .inputText("example")
            .assertEmailIsInvalid()
            .inputText(TestConstants.VALID_EMAIL)
            .assertEmailIsValid()
            .assertLoginButtonIsDisabled()
            .clickPassword()
            .inputText(TestConstants.VALID_PASSWORD)
            .assertLoginButtonIsEnabled()
            .showPassword()
            .hidePassword()
            .assertLoginButtonIsEnabled()
            .clickLoginButton()
            .assertLoggingIn()

        composeRule.onNodeWithText("AgendaScreen").assertIsDisplayed()
    }
}
