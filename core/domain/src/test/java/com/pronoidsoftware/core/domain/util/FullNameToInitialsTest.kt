package com.pronoidsoftware.core.domain.util

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class FullNameToInitialsTest {

    @ParameterizedTest
    @CsvSource(
        delimiter = ':',
        value = [
            "ValidName: VA",
            "Valid Name : VN",
            "Valid    2Name: V2",
            "Valid\t3Name: V3",
            "'Valid\n4Name': V4",
            "'\tnew\t\nname\n': NN",
            " new  name : NN",
            "abc: AB",
            "abcd: AB",
            "This name is simply too long and therefore does not constrain to the limit: TL",
            " a b : AB",
            "Th3M@sterName_: TH",
            "M@n: M@",
            "Mr. Debug: MD",
            "\"\": \"\"",
            ",,,,: ,,",
            "a  a: AA",
            " a a a : AA",
            "a: A",
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa: AA",
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa: AA",
            "1337: 13",
            "💻: 💻",
            "💻🥰🎉😈: 💻🥰",
            "@, #, $, %, ^, &, *, (, ), -, +, =, {, }, [, ], |, ;, ', \", <, >, ,, ., ?, /.: @/",
            "@$-.: @$",
            "xXHackerXx: XX",
            "Mr. 🤖: M🤖",
            "The element of ...surprise!: T.",
            "\"    \": \"\"",
        ],
    )
    fun `test name initialization and capitalization`(name: String, expectedInitials: String) {
        assertThat(name.initializeAndCapitalize()).isEqualTo(expectedInitials)
    }

    // using this test because CSV source handles whitespace weird
    @Test
    fun `name initialization and capitalization with whitespace`() {
        assertThat("Vew\tName".initializeAndCapitalize()).isEqualTo("VN")
        assertThat("Vew\nName".initializeAndCapitalize()).isEqualTo("VN")
        assertThat("\tnew\t\nname\n".initializeAndCapitalize()).isEqualTo("NN")
        assertThat(" new  name ".initializeAndCapitalize()).isEqualTo("NN")
        assertThat(" a a a ".initializeAndCapitalize()).isEqualTo("AA")
        assertThat(" a b ".initializeAndCapitalize()).isEqualTo("AB")
        assertThat(" \ta b \n".initializeAndCapitalize()).isEqualTo("AB")
    }

    @Test
    fun `test emoji initialization`() {
        assertThat(
            "\uD83D\uDCBB \uD83E\uDD70 \uD83C\uDF89 \uD83D\uDE08".initializeAndCapitalize(),
        ).isEqualTo("\uD83D\uDCBB\uD83D\uDE08")
        assertThat(
            "\uD83D\uDCBB \uD83E\uDD70 \uD83C\uDF89\uD83D\uDE08".initializeAndCapitalize(),
        ).isEqualTo("\uD83D\uDCBB\uD83C\uDF89")
        assertThat(
            "\uD83D\uDCBB\uD83E\uDD70\uD83C\uDF89\uD83D\uDE08".initializeAndCapitalize(),
        ).isEqualTo("\uD83D\uDCBB\uD83E\uDD70")
    }
}
