package com.pronoidsoftware.auth.data

import android.util.Patterns
import com.pronoidsoftware.auth.domain.PatternValidator
import javax.inject.Inject

class EmailPatternValidator @Inject constructor() : PatternValidator {
    override fun matches(value: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }
}
