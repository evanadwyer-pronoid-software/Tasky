package com.pronoidsoftware.core.domain.util

fun String.toInitials(): String {
    val words = this
        .replace('\t', ' ')
        .replace('\n', ' ')
        .split(" ")
        .dropWhile { it.isBlank() }
        .dropLastWhile { it.isBlank() }
    return when (words.size) {
        0 -> "" // should not happen
        1 -> {
            getCharacters(words.first(), 2)
        }

        else -> {
            val first = words.first()
            val last = words.last()
            "${getCharacters(first, 1)}${getCharacters(last, 1)}"
        }
    }
}

private fun getCharacters(word: String, need: Int): String {
    val result = StringBuilder()
    var index = 0
    var count = 0
    while (index < word.length && count < need) {
        if (word[index].isHighSurrogate() &&
            index + 1 < word.length &&
            word[index + 1].isLowSurrogate()
        ) {
            // This is a surrogate pair (emoji), add it as a single unit
            result.append(word[index]).append(word[index + 1])
            index += 2 // Skip the next char as it's part of the emoji
        } else {
            // This is a regular character, add it
            result.append(word[index])
            index++
        }
        count++
    }
    return result.toString()
}
