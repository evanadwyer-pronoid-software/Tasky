package com.pronoidsoftware.auth.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.pronoidsoftware.auth.presentation.login.LoginScreenRobot
import com.pronoidsoftware.auth.presentation.register.RegisterScreenRobot
import com.pronoidsoftware.core.TaskyAndroidTest
import com.pronoidsoftware.core.domain.AuthInfo
import com.pronoidsoftware.tasky.MainActivity
import com.pronoidsoftware.testutil.androidtest.core.data.di.TestAuthInfoModule
import com.pronoidsoftware.testutil.jvmtest.TestConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import org.junit.Rule
import org.junit.Test

@UninstallModules(TestAuthInfoModule::class)
@HiltAndroidTest
class AuthFeatureE2ETest : TaskyAndroidTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Module
    @InstallIn(SingletonComponent::class)
    object AuthInfoLoggedOutModule {

        @Provides
        @Singleton
        fun provideAuthInfoLoggedOut(): AuthInfo? {
            return null
        }
    }

    @Test
    fun testAuthFeature_happyPath() {
        LoginScreenRobot(composeRule)
            .clickSignUp()

        RegisterScreenRobot(composeRule)
            .assertPasswordIsHidden()
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
            .assertPasswordIsShown()
            .hidePassword()
            .assertPasswordIsHidden()
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

        composeRule.onNodeWithText("Today").assertIsDisplayed()
        composeRule.onNodeWithText("TE").assertIsDisplayed()
    }

    @Test
    fun testAuthFeature_infoRetainedOnNavigation() {
        LoginScreenRobot(composeRule)
            .clickEmail()
            .inputText(TestConstants.VALID_EMAIL)
            .assertEmailIsValid()
            .clickSignUp()

        RegisterScreenRobot(composeRule)
            .clickLoginButton()

        LoginScreenRobot(composeRule)
            .assertEmailIsValid()
    }
}
