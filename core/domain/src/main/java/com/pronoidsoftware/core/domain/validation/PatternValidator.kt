package com.pronoidsoftware.core.domain.validation

interface PatternValidator {

    fun matches(value: String): Boolean
}
