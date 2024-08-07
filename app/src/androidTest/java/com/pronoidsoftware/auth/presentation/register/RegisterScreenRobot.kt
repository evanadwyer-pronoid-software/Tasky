package com.pronoidsoftware.auth.presentation.register

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.isFocused
import androidx.compose.ui.test.isNotDisplayed
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.pronoidsoftware.TaskyComposeRule
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

class RegisterScreenRobot(
    private val composeRule: TaskyComposeRule,
) {

    fun clickName(): RegisterScreenRobot {
        composeRule.onNodeWithText("Name").performClick()
        return this
    }

    fun inputText(value: String): RegisterScreenRobot {
        composeRule.onNode(isFocused()).performTextClearance()
        composeRule.onNode(isFocused()).performTextInput(value)
        return this
    }

    fun assertNameIsInvalid(): RegisterScreenRobot {
        composeRule.onNodeWithContentDescription("Name is valid").assertIsNotDisplayed()
        return this
    }

    fun assertNameIsValid(): RegisterScreenRobot {
        composeRule.onNodeWithContentDescription("Name is valid").assertIsDisplayed()
        return this
    }

    fun clickEmail(): RegisterScreenRobot {
        composeRule.onNodeWithText("Email address").performClick()
        return this
    }

    fun assertEmailIsInvalid(): RegisterScreenRobot {
        composeRule.onNodeWithContentDescription("Email is valid").assertIsNotDisplayed()
        return this
    }

    fun assertEmailIsValid(): RegisterScreenRobot {
        composeRule.onNodeWithContentDescription("Email is valid").assertIsDisplayed()
        return this
    }

    fun clickPassword(): RegisterScreenRobot {
        composeRule.onNodeWithText("Password").performClick()
        return this
    }

    fun hidePassword(): RegisterScreenRobot {
        composeRule.onNodeWithContentDescription("Hide password").performClick()
        return this
    }

    fun showPassword(): RegisterScreenRobot {
        composeRule.onNodeWithContentDescription("Show password").performClick()
        return this
    }

    fun assertPasswordIsHidden(): RegisterScreenRobot {
        composeRule.onNodeWithContentDescription("Hide password").isNotDisplayed()
        return this
    }

    fun assertPasswordIsShown(): RegisterScreenRobot {
        composeRule.onNodeWithContentDescription("Show password").isNotDisplayed()
        return this
    }

    fun assertPasswordIsInvalid(): RegisterScreenRobot {
        composeRule.onNodeWithContentDescription("Password is valid").assertIsNotDisplayed()
        return this
    }

    fun assertPasswordIsValid(): RegisterScreenRobot {
        composeRule.onNodeWithContentDescription("Password is valid").assertIsDisplayed()
        return this
    }

    fun assertRegisterButtonIsDisabled(): RegisterScreenRobot {
        composeRule.onNodeWithText("GET STARTED").assertIsNotEnabled()
        return this
    }

    fun assertRegisterButtonIsEnabled(): RegisterScreenRobot {
        composeRule.onNodeWithText("GET STARTED").assertIsEnabled()
        return this
    }

    fun clickRegisterButton(): RegisterScreenRobot {
        composeRule.onNodeWithText("GET STARTED").performClick()
        return this
    }

    fun assertRegistering(): RegisterScreenRobot {
        composeRule.onNodeWithContentDescription("Registering…").assertIsDisplayed()
        // allow for API to finish...
        runBlocking {
            delay(100)
        }
        return this
    }

    fun clickLoginButton(): RegisterScreenRobot {
        composeRule.onNodeWithContentDescription("Go back to login").performClick()
        return this
    }
}
