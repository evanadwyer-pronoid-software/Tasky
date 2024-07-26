package com.pronoidsoftware.core.domain.validation

class UserDataValidator(
    private val emailPatternValidator: PatternValidator,
) {

    fun validateName(name: String): Boolean {
        return name.trim().length in MIN_NAME_LENGTH..MAX_NAME_LENGTH
    }

    fun validateEmail(email: String): Boolean {
        return emailPatternValidator.matches(email.trim())
    }

    fun validatePassword(password: String): PasswordValidationState {
        val hasMinLength = password.trim().length >= MIN_PASSWORD_LENGTH
        val hasDigit = password.any { it.isDigit() }
        val hasLowerCaseCharacter = password.any { it.isLowerCase() }
        val hasUpperCaseCharacter = password.any { it.isUpperCase() }

        return PasswordValidationState(
            hasMinimumLength = hasMinLength,
            hasDigit = hasDigit,
            hasLowerCaseCharacter = hasLowerCaseCharacter,
            hasUpperCaseCharacter = hasUpperCaseCharacter,
        )
    }

    companion object {
        const val MIN_NAME_LENGTH = 4
        const val MAX_NAME_LENGTH = 50
        const val MIN_PASSWORD_LENGTH = 9
    }
}
