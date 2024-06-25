package com.pronoidsoftware.auth.presentation.register

import androidx.activity.compose.setContent
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.navigation.compose.rememberNavController
import com.pronoidsoftware.core.TaskyAndroidTest
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.tasky.MainActivity
import com.pronoidsoftware.tasky.NavigationRoot
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class RegisterScreenTest : TaskyAndroidTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testRegisterScreenUi_allValid() {
        composeRule.activity.setContent {
            TaskyTheme {
                val navController = rememberNavController()
                NavigationRoot(navController = navController)
                RegisterScreenRobot(composeRule)
                    .navigateTo(navController)
                    .assertRegisterButtonIsDisabled()
                    .assertNameIsInvalid()
                    .clickName()
                    .inputText("Val")
                    .assertNameIsInvalid()
                    .inputText("Valid Name")
                    .assertNameIsValid()
                    .assertRegisterButtonIsDisabled()
                    .assertEmailIsInvalid()
                    .clickEmail()
                    .inputText("example")
                    .assertEmailIsInvalid()
                    .inputText("example@example.com")
                    .assertEmailIsValid()
                    .assertRegisterButtonIsDisabled()
                    .assertPasswordIsInvalid()
                    .clickPassword()
                    .inputText("Invalid1")
                    .assertPasswordIsInvalid()
                    .inputText("Test12345")
                    .assertPasswordIsValid()
                    .showPassword()
                    .hidePassword()
                    .assertRegisterButtonIsEnabled()
                    .clickRegisterButton()
            }
        }
    }
}
