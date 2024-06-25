package com.pronoidsoftware.auth.presentation.login

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.isFocused
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import androidx.navigation.NavHostController
import com.pronoidsoftware.TaskyComposeRule
import com.pronoidsoftware.tasky.LoginScreen
import kotlinx.coroutines.runBlocking

class LoginScreenRobot(
    private val composeRule: TaskyComposeRule,
) {
    fun navigateTo(navController: NavHostController): LoginScreenRobot {
        runBlocking {
            composeRule.awaitIdle()
            composeRule.runOnUiThread {
                navController.navigate(LoginScreen) {
                    popUpTo(LoginScreen) {
                        inclusive = true
                    }
                }
            }
        }
        return this
    }

    fun inputText(value: String): LoginScreenRobot {
        composeRule.onNode(isFocused()).performTextClearance()
        composeRule.onNode(isFocused()).performTextInput(value)
        return this
    }

    fun clickEmail(): LoginScreenRobot {
        composeRule.onNodeWithText("Email address").performClick()
        return this
    }

    fun assertEmailIsInvalid(): LoginScreenRobot {
        composeRule.onNodeWithContentDescription("Email is valid").assertIsNotDisplayed()
        return this
    }

    fun assertEmailIsValid(): LoginScreenRobot {
        composeRule.onNodeWithContentDescription("Email is valid").assertIsDisplayed()
        return this
    }

    fun clickPassword(): LoginScreenRobot {
        composeRule.onNodeWithText("Password").performClick()
        return this
    }

    fun hidePassword(): LoginScreenRobot {
        composeRule.onNodeWithContentDescription("Hide password").performClick()
        return this
    }

    fun showPassword(): LoginScreenRobot {
        composeRule.onNodeWithContentDescription("Show password").performClick()
        return this
    }

    fun assertLoginButtonIsDisabled(): LoginScreenRobot {
        composeRule.onNodeWithText("LOG IN").assertIsNotEnabled()
        return this
    }

    fun assertLoginButtonIsEnabled(): LoginScreenRobot {
        composeRule.onNodeWithText("LOG IN").assertIsEnabled()
        return this
    }

    fun clickLoginButton(): LoginScreenRobot {
        composeRule.onNodeWithText("LOG IN").performClick()
        return this
    }

    fun assertLoggingIn(): LoginScreenRobot {
        composeRule.onNodeWithText("LOG IN").assertIsNotDisplayed()
        return this
    }
}
