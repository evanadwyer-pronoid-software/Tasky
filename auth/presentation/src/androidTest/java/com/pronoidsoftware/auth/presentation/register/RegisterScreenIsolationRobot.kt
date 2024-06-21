package com.pronoidsoftware.auth.presentation.register

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.ComposeContentTestRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

class RegisterScreenIsolationRobot(
    private val composeRule: ComposeContentTestRule,
) {
    fun navigateTo(registerState: RegisterState): RegisterScreenIsolationRobot {
        composeRule.setContent {
            TaskyTheme {
                RegisterScreen(
                    state = registerState,
                    onAction = {},
                )
            }
        }
        return this
    }

    fun assertNameIsDisplayed(): RegisterScreenIsolationRobot {
        composeRule.onNodeWithText("Name").assertIsDisplayed()
        return this
    }

    fun assertNameIsInvalid(): RegisterScreenIsolationRobot {
        composeRule.onNodeWithContentDescription("Name is valid").assertIsNotDisplayed()
        return this
    }

    fun assertNameIsValid(): RegisterScreenIsolationRobot {
        composeRule.onNodeWithContentDescription("Name is valid").assertIsDisplayed()
        return this
    }

    fun assertEmailIsDisplayed(): RegisterScreenIsolationRobot {
        composeRule.onNodeWithText("Email address").assertIsDisplayed()
        return this
    }

    fun assertEmailIsInvalid(): RegisterScreenIsolationRobot {
        composeRule.onNodeWithContentDescription("Email is valid").assertIsNotDisplayed()
        return this
    }

    fun assertEmailIsValid(): RegisterScreenIsolationRobot {
        composeRule.onNodeWithContentDescription("Email is valid").assertIsDisplayed()
        return this
    }

    fun assertPasswordIsDisplayed(): RegisterScreenIsolationRobot {
        composeRule.onNodeWithText("Password").assertIsDisplayed()
        return this
    }

    fun assertPasswordIsInvalid(): RegisterScreenIsolationRobot {
        composeRule.onNodeWithContentDescription("Password is valid").assertIsNotDisplayed()
        return this
    }

    fun assertPasswordIsValid(): RegisterScreenIsolationRobot {
        composeRule.onNodeWithContentDescription("Password is valid").assertIsDisplayed()
        return this
    }

    fun assertRegisterButtonIsDisabled(): RegisterScreenIsolationRobot {
        composeRule.onNodeWithText("GET STARTED").assertIsNotEnabled()
        return this
    }

    fun assertRegisterButtonIsEnabled(): RegisterScreenIsolationRobot {
        composeRule.onNodeWithText("GET STARTED").assertIsEnabled()
        return this
    }

    fun assertRegistering(): RegisterScreenIsolationRobot {
        composeRule.onNodeWithText("GET STARTED").assertIsNotDisplayed()
        return this
    }
}
