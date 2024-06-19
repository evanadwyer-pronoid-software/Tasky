package com.pronoidsoftware.auth.domain

import assertk.assertThat
import assertk.assertions.isFalse
import assertk.assertions.isTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class UserDataValidatorTest {

    private lateinit var sut: UserDataValidator
    private lateinit var emailPatternValidatorFake: PatternValidator

    @BeforeEach
    fun setUp() {
        // we don't need to check that the email pattern matcher works, so returning true
        emailPatternValidatorFake = object : PatternValidator {
            override fun matches(value: String): Boolean {
                return true
            }
        }
        sut = UserDataValidator(emailPatternValidatorFake)
    }

    @Test
    fun `validateName returns true when name length is within valid range`() {
        val result = sut.validateName("ValidName")
        assertThat(result).isTrue()
    }

    @Test
    fun `validateName returns true when name length is within valid range with whitespace`() {
        val result = sut.validateName("Valid Name ")
        assertThat(result).isTrue()
    }

    @Test
    fun `validateName returns false when name length is less than minimum`() {
        val result = sut.validateName("abc")
        assertThat(result).isFalse()
    }

    @Test
    fun `validateName returns false when name length is more than maximum`() {
        val result = sut.validateName("a".repeat(51))
        assertThat(result).isFalse()
    }

    @Test
    fun `validateName returns false when name length is less than minimum with whitespace`() {
        val result = sut.validateName("  ab ")
        assertThat(result).isFalse()
    }

    @Test
    fun `validatePassword returns valid state when password meets all requirements`() {
        val result = sut.validatePassword("ValidPassword1")
        assertThat(result.hasMinimumLength).isTrue()
        assertThat(result.hasDigit).isTrue()
        assertThat(result.hasLowerCaseCharacter).isTrue()
        assertThat(result.hasUpperCaseCharacter).isTrue()
    }

    @Test
    fun `validatePassword returns valid when password meets all requirements with whitespace`() {
        val result = sut.validatePassword("Valid Password 1")
        assertThat(result.hasMinimumLength).isTrue()
        assertThat(result.hasDigit).isTrue()
        assertThat(result.hasLowerCaseCharacter).isTrue()
        assertThat(result.hasUpperCaseCharacter).isTrue()
    }

    @Test
    fun `validatePassword returns valid when password meets all requirements with specials`() {
        val result = sut.validatePassword("Valid@Password_1")
        assertThat(result.hasMinimumLength).isTrue()
        assertThat(result.hasDigit).isTrue()
        assertThat(result.hasLowerCaseCharacter).isTrue()
        assertThat(result.hasUpperCaseCharacter).isTrue()
    }

    @Test
    fun `validatePassword returns invalid state when password does not meet requirements`() {
        val result = sut.validatePassword("invalid")
        assertThat(result.hasMinimumLength).isFalse()
        assertThat(result.hasDigit).isFalse()
        assertThat(result.hasLowerCaseCharacter).isTrue()
        assertThat(result.hasUpperCaseCharacter).isFalse()
    }
}
