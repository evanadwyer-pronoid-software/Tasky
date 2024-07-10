package com.pronoidsoftware.core.presentation.ui

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class CapitalizeInitialsTest {

    @ParameterizedTest
    @CsvSource(
        delimiter = ':',
        value = [
            "Va: VA",
            "VN : VN",
            "ab: AB",
            "ab: AB",
            "Tl: TL",
            "ab: AB",
            "Th: TH",
            "M@: M@",
            "MD: MD",
            "\"\": \"\"",
            ",,: ,,",
            "aa: AA",
            "a: A",
            "13: 13",
            "💻: 💻",
            "💻🥰: 💻🥰",
            "5⭐️: 5⭐️",
            "@/: @/",
            "@$: @$",
            "xX: XX",
            "M🤖: M🤖",
            "T.: T.",
        ],
    )
    fun `test name initialization`(name: String, expectedInitials: String) {
        assertThat(name.capitalizeInitials()).isEqualTo(expectedInitials)
    }
}
