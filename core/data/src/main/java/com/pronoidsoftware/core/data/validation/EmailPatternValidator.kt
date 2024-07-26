package com.pronoidsoftware.core.data.validation

import android.util.Patterns
import com.pronoidsoftware.core.domain.validation.PatternValidator
import javax.inject.Inject

class EmailPatternValidator @Inject constructor() : PatternValidator {
    override fun matches(value: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }
}
