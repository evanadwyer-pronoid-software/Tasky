@file:OptIn(ExperimentalFoundationApi::class)

package com.pronoidsoftware.auth.presentation.register

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.ui.test.junit4.createComposeRule
import com.pronoidsoftware.auth.domain.PasswordValidationState
import org.junit.Rule
import org.junit.Test

class RegisterScreenIsolationTest {

    @get:Rule
    val composeRule = createComposeRule()

    @Test
    fun testRegisterScreenUi_empty() {
        RegisterScreenIsolationRobot(composeRule)
            .navigateTo(RegisterState())
            .assertNameIsDisplayed()
            .assertEmailIsDisplayed()
            .assertPasswordIsDisplayed()
            .assertNameIsInvalid()
            .assertEmailIsInvalid()
            .assertPasswordIsInvalid()
            .assertRegisterButtonIsDisabled()
    }

    @Test
    fun testRegisterScreenUi_allValid() {
        val registerState = RegisterState(
            isNameValid = true,
            isEmailValid = true,
            passwordValidationState = PasswordValidationState(
                hasMinimumLength = true,
                hasDigit = true,
                hasLowerCaseCharacter = true,
                hasUpperCaseCharacter = true,
            ),
        )

        RegisterScreenIsolationRobot(composeRule)
            .navigateTo(registerState)
            .assertNameIsValid()
            .assertEmailIsValid()
            .assertPasswordIsValid()
            .assertRegisterButtonIsEnabled()
    }

    @Test
    fun testRegisterScreenUi_IsRegistering() {
        RegisterScreenIsolationRobot(composeRule)
            .navigateTo(RegisterState(isRegistering = true))
            .assertRegistering()
    }
}
