package com.pronoidsoftware.auth.presentation.login

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import com.pronoidsoftware.core.TaskyAndroidTest
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.tasky.MainActivity
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class LoginScreenTest : TaskyAndroidTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testRegisterScreenUi_allValid() {
        composeRule.activity.setContent {
            TaskyTheme {
                LoginScreenRoot(
                    onLoginSuccess = { },
                    onRegisterClick = { },
                )
            }
        }
        LoginScreenRobot(composeRule)
            .assertLoginButtonIsDisabled()
            .assertEmailIsInvalid()
            .clickEmail()
            .inputText("example")
            .assertEmailIsInvalid()
            .inputText("example@example.com")
            .assertEmailIsValid()
            .assertLoginButtonIsDisabled()
            .clickPassword()
            .inputText("Test12345")
            .assertLoginButtonIsEnabled()
            .showPassword()
            .hidePassword()
            .assertLoginButtonIsEnabled()
            .clickLoginButton()
    }
}
