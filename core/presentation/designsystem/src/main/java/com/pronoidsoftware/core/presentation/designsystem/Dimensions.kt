package com.pronoidsoftware.core.presentation.designsystem

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions(
    val default: Dp = 0.dp,
    val stroke: Dp = 1.dp,
    val strokeBold: Dp = 2.dp,
    val spaceExtraSmall: Dp = 4.dp,
    val spaceSmall: Dp = 8.dp,
    val spaceSmallMedium: Dp = 10.dp,
    val spaceMediumSmall: Dp = 12.5.dp,
    val spaceMedium: Dp = 16.dp,
    val spaceMediumLarge: Dp = 20.dp,
    val spaceLargeMedium: Dp = 24.dp,
    val spaceLarge: Dp = 32.dp,
    val spaceExtraLarge: Dp = 42.dp,
    val maxButtonWidth: Dp = 320.dp,
    val maxTextFieldWidth: Dp = 488.dp,

    // auth feature
    val authPaddingTop: Dp = 30.dp,
    val authContainerRadius: Dp = 30.dp,
    val authPaddingInterior: Dp = 15.dp,
    val registerActionButtonSpacingTop: Dp = 70.dp,
    val loginActionButtonSpacingTop: Dp = 25.dp,
)

val LocalSpacing = compositionLocalOf { Dimensions() }
