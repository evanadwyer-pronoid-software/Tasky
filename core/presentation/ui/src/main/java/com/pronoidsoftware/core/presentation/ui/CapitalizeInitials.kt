package com.pronoidsoftware.core.presentation.ui

import java.util.Locale

fun String.capitalizeInitials(): String {
    return this.uppercase(Locale.getDefault())
}
