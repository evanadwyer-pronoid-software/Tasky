package com.pronoidsoftware.core.presentation.designsystem

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

val LightColorScheme = lightColorScheme(
    primary = TaskyGreen,
    background = TaskyWhite,
    surface = TaskyWhite,
    surfaceVariant = TaskyLightGray,
    secondary = TaskyLightGreen,
    primaryContainer = TaskyBlack,
    onPrimary = TaskyWhite,
    onBackground = TaskyBlack,
    onSurface = TaskyBlack,
    onSurfaceVariant = TaskyGray,
    error = TaskyError,
)

@Composable
fun TaskyTheme(content: @Composable () -> Unit) {
    val colorScheme = LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
