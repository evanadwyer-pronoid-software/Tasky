package com.pronoidsoftware.core.presentation.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider

val LightColorScheme = lightColorScheme(
    primary = TaskyGreen,
    background = TaskyWhite,
    surface = TaskyWhite,
    surfaceVariant = TaskyLightGray,
    secondary = TaskyLightGreen,
    primaryContainer = TaskyBlack,
    onPrimaryContainer = TaskyButtonWhite,
    onPrimary = TaskyWhite,
    onBackground = TaskyBlack,
    onSurface = TaskyBlack,
    onSurfaceVariant = TaskyGray,
    error = TaskyError,
)

@Composable
fun TaskyTheme(content: @Composable () -> Unit) {
    CompositionLocalProvider(LocalSpacing provides Dimensions()) {
        MaterialTheme(
            colorScheme = LightColorScheme,
            typography = Typography,
            content = content,
        )
    }
}
