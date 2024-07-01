package com.pronoidsoftware.auth.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

internal class UserDataValidatorTest {

    private lateinit var userDataValidator: UserDataValidator
    private lateinit var emailPatternValidatorStub: PatternValidator

    @BeforeEach
    fun setUp() {
        // we don't need to check that the email pattern matcher works, so returning true
        emailPatternValidatorStub = object : PatternValidator {
            override fun matches(value: String): Boolean {
                return true
            }
        }
        userDataValidator = UserDataValidator(emailPatternValidatorStub)
    }

    @ParameterizedTest
    @CsvSource(
        delimiter = ':',
        value = [
            "ValidName: true",
            "Valid Name : true",
            "abc: false",
            "abcd: true",
            "This name is simply too long and therefore does not constrain to the limit: false",
            " a b : false",
            "Th3M@sterName_: true",
            "Mr. Debug: true",
            "\"\": false",
            ",,,,: true",
            "a  a: true",
            " a a a : true",
            "a: false",
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa: true",
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa: false",
            "1337: true",
            "💻: false",
            "💻🥰🎉😈: true",
            "@, #, $, %, ^, &, *, (, ), -, +, =, {, }, [, ], |, ;, ', \", <, >, ,, ., ?, /.: false",
            "@$-.: true",
        ],
    )
    fun `test valid name`(name: String, expectedIsValid: Boolean) {
        assertThat(userDataValidator.validateName(name)).isEqualTo(expectedIsValid)
    }

    @ParameterizedTest
    @CsvSource(
        delimiter = ':',
        value = [
            "ValidPassword1: true:true:true:true:true",
            "Valid Password 1: true:true:true:true:true",
            "Valid@Password_1: true:true:true:true:true",
            "invalid: false:false:true:false:false",
            "INVALID: false:false:false:true:false",
            "123456789: true:true:false:false:false",
            "abcdefghi: true:false:true:false:false",
            "ABCDEFGHI: true:false:false:true:false",
            "abc: false:false:true:false:false",
            "ABC: false:false:false:true:false",
            "123: false:true:false:false:false",
            "aB1: false:true:true:true:false",
            "aB1aB1aB1: true:true:true:true:true",
            "Invalid1: false:true:true:true:false",
            "\"\": false:false:false:false:false",
            "Test12345: true:true:true:true:true",
            "@!+()$#**: true:false:false:false:false",
        ],
    )
    fun `test valid password`(
        password: String,
        expectedHasMinLength: Boolean,
        expectedHasDigit: Boolean,
        expectedHasLowerCase: Boolean,
        expectedHasUpperCase: Boolean,
        expectedIsPasswordValid: Boolean,
    ) {
        val result = userDataValidator.validatePassword(password)
        assertThat(result.hasMinimumLength).isEqualTo(expectedHasMinLength)
        assertThat(result.hasLowerCaseCharacter).isEqualTo(expectedHasLowerCase)
        assertThat(result.hasUpperCaseCharacter).isEqualTo(expectedHasUpperCase)
        assertThat(result.hasDigit).isEqualTo(expectedHasDigit)
        assertThat(result.isPasswordValid).isEqualTo(expectedIsPasswordValid)
    }
}
