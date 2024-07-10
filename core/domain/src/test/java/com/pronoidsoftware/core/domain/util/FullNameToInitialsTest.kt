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
            "ValidName: Va",
            "Valid Name : VN",
            "abc: ab",
            "abcd: ab",
            "This name is simply too long and therefore does not constrain to the limit: Tl",
            " a b : ab",
            "Th3M@sterName_: Th",
            "M@n: M@",
            "Mr. Debug: MD",
            "\"\": \"\"",
            ",,,,: ,,",
            "a  a: aa",
            " a a a : aa",
            "a: a",
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa: aa",
            "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa: aa",
            "1337: 13",
            "💻: 💻",
            "💻🥰🎉😈: 💻🥰",
            "@, #, $, %, ^, &, *, (, ), -, +, =, {, }, [, ], |, ;, ', \", <, >, ,, ., ?, /.: @/",
            "@$-.: @$",
            "xXHackerXx: xX",
            "Mr. 🤖: M🤖",
            "The element of ...surprise!: T.",
            "\"    \": \"\"",
        ],
    )
    fun `test name initialization`(name: String, expectedInitials: String) {
        assertThat(name.toInitials()).isEqualTo(expectedInitials)
    }

    @Test
    fun `test emoji initialization`() {
        assertThat(
            "\uD83D\uDCBB \uD83E\uDD70 \uD83C\uDF89 \uD83D\uDE08".toInitials(),
        ).isEqualTo("\uD83D\uDCBB\uD83D\uDE08")
        assertThat(
            "\uD83D\uDCBB \uD83E\uDD70 \uD83C\uDF89\uD83D\uDE08".toInitials(),
        ).isEqualTo("\uD83D\uDCBB\uD83C\uDF89")
        assertThat(
            "\uD83D\uDCBB\uD83E\uDD70\uD83C\uDF89\uD83D\uDE08".toInitials(),
        ).isEqualTo("\uD83D\uDCBB\uD83E\uDD70")
    }
}
