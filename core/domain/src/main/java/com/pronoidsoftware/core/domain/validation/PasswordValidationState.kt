package com.pronoidsoftware.core.domain.validation

data class PasswordValidationState(
    val hasMinimumLength: Boolean = false,
    val hasDigit: Boolean = false,
    val hasLowerCaseCharacter: Boolean = false,
    val hasUpperCaseCharacter: Boolean = false,
) {
    val isPasswordValid: Boolean
        get() = hasMinimumLength && hasDigit && hasLowerCaseCharacter && hasUpperCaseCharacter
}
