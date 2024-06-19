package com.pronoidsoftware.auth.domain

interface PatternValidator {

    fun matches(value: String): Boolean
}
