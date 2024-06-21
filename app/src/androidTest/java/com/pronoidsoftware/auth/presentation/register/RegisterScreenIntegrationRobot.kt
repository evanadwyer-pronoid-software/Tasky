package com.pronoidsoftware.auth.presentation.register

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
import com.pronoidsoftware.TaskyComposeRule

class RegisterScreenIntegrationRobot(
    private val composeRule: TaskyComposeRule,
) {
    fun navigateTo(): RegisterScreenIntegrationRobot {
        // Replace this with using the navHostController
        return this
    }

    fun clickName(): RegisterScreenIntegrationRobot {
        composeRule.onNodeWithText("Name").performClick()
        return this
    }

    fun inputText(value: String): RegisterScreenIntegrationRobot {
        composeRule.onNode(isFocused()).performTextClearance()
        composeRule.onNode(isFocused()).performTextInput(value)
        return this
    }

    fun assertNameIsInvalid(): RegisterScreenIntegrationRobot {
        composeRule.onNodeWithContentDescription("Name is valid").assertIsNotDisplayed()
        return this
    }

    fun assertNameIsValid(): RegisterScreenIntegrationRobot {
        composeRule.onNodeWithContentDescription("Name is valid").assertIsDisplayed()
        return this
    }

    fun clickEmail(): RegisterScreenIntegrationRobot {
        composeRule.onNodeWithText("Email address").performClick()
        return this
    }

    fun assertEmailIsInvalid(): RegisterScreenIntegrationRobot {
        composeRule.onNodeWithContentDescription("Email is valid").assertIsNotDisplayed()
        return this
    }

    fun assertEmailIsValid(): RegisterScreenIntegrationRobot {
        composeRule.onNodeWithContentDescription("Email is valid").assertIsDisplayed()
        return this
    }

    fun clickPassword(): RegisterScreenIntegrationRobot {
        composeRule.onNodeWithText("Password").performClick()
        return this
    }

    fun assertPasswordIsInvalid(): RegisterScreenIntegrationRobot {
        composeRule.onNodeWithContentDescription("Password is valid").assertIsNotDisplayed()
        return this
    }

    fun assertPasswordIsValid(): RegisterScreenIntegrationRobot {
        composeRule.onNodeWithContentDescription("Password is valid").assertIsDisplayed()
        return this
    }

    fun assertRegisterButtonIsDisabled(): RegisterScreenIntegrationRobot {
        composeRule.onNodeWithText("GET STARTED").assertIsNotEnabled()
        return this
    }

    fun assertRegisterButtonIsEnabled(): RegisterScreenIntegrationRobot {
        composeRule.onNodeWithText("GET STARTED").assertIsEnabled()
        return this
    }

    fun clickRegisterButton(): RegisterScreenIntegrationRobot {
        composeRule.onNodeWithText("GET STARTED").performClick()
        return this
    }

    fun assertRegistering(): RegisterScreenIntegrationRobot {
        composeRule.onNodeWithText("GET STARTED").assertIsNotDisplayed()
        return this
    }
}
