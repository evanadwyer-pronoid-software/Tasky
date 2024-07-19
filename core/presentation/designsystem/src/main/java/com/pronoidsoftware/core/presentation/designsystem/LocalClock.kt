package com.pronoidsoftware.core.presentation.designsystem

import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.datetime.Clock

val LocalClock = staticCompositionLocalOf<Clock> {
    error("No clock provided.")
}
