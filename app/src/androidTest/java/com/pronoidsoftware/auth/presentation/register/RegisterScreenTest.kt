package com.pronoidsoftware.auth.presentation.register

import androidx.activity.compose.setContent
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotDisplayed
import androidx.compose.ui.test.isFocused
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextClearance
import androidx.compose.ui.test.performTextInput
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.tasky.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class RegisterScreenTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    @Test
    fun testRegisterScreenUi_validName() {
        composeRule.activity.setContent {
            TaskyTheme {
                RegisterScreenRoot(
                    onLogInClick = {},
                    onSuccessfulRegistration = {},
                )
            }
        }

        composeRule.onNodeWithText("Name").assertIsDisplayed()
        composeRule.onNodeWithText("Name").performClick()
        composeRule.onNode(isFocused()).performTextInput("Val")
        composeRule.onNodeWithContentDescription("Name is valid").assertIsNotDisplayed()
        composeRule.onNode(isFocused()).performTextClearance()
        composeRule.onNode(isFocused()).performTextInput("Valid Name")
        composeRule.onNodeWithContentDescription("Name is valid").assertIsDisplayed()
    }

    @Test
    fun testRegisterScreenUi_validEmail() {
        composeRule.activity.setContent {
            TaskyTheme {
                RegisterScreenRoot(
                    onLogInClick = {},
                    onSuccessfulRegistration = {},
                )
            }
        }

        composeRule.onNodeWithText("Email address").assertIsDisplayed()
        composeRule.onNodeWithText("Email address").performClick()
        composeRule.onNode(isFocused()).performTextInput("example")
        composeRule.onNodeWithContentDescription("Email is valid").assertIsNotDisplayed()
        composeRule.onNode(isFocused()).performTextClearance()
        composeRule.onNode(isFocused()).performTextInput("example@example.com")
        composeRule.onNodeWithContentDescription("Email is valid").assertIsDisplayed()
    }

    @Test
    fun testRegisterScreenUi_validPassword() {
        composeRule.activity.setContent {
            TaskyTheme {
                RegisterScreenRoot(
                    onLogInClick = {},
                    onSuccessfulRegistration = {},
                )
            }
        }

        composeRule.onNodeWithText("Password").assertIsDisplayed()
        composeRule.onNodeWithText("Password").performClick()
        composeRule.onNode(isFocused()).performTextInput("T")
        composeRule.onNodeWithContentDescription("Password is valid").assertIsNotDisplayed()
        composeRule.onNode(isFocused()).performTextClearance()
        composeRule.onNode(isFocused()).performTextInput("Test12345")
        composeRule.onNodeWithContentDescription("Password is valid").assertIsDisplayed()
    }
}
