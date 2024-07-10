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
    val spaceMediumSmall: Dp = 12.dp,
    val spaceMedium: Dp = 16.dp,
    val spaceMediumLarge: Dp = 20.dp,
    val spaceLargeMedium: Dp = 24.dp,
    val spaceLarge: Dp = 32.dp,
    val spaceExtraLarge: Dp = 42.dp,

    // action button
    val maxButtonWidth: Dp = 320.dp,
    val actionButtonVerticalPadding: Dp = 12.5.dp,

    // dropdown menu
    val dropdownMenuCornerRadius: Dp = 7.dp,

    // auth feature
    val authPaddingTop: Dp = 30.dp,
    val authContainerRadius: Dp = 30.dp,
    val authPaddingInterior: Dp = 15.dp,
    val registerActionButtonSpacingTop: Dp = 70.dp,
    val loginActionButtonSpacingTop: Dp = 25.dp,
    val maxTextFieldWidth: Dp = 488.dp,

    // agenda feature
    val timeMarkerVerticalPadding: Dp = 2.dp,
    val timeMarkerBallRadius: Dp = 5.dp,
    val timeMarkerStrokeWidth: Dp = strokeBold,
    val agendaItemCornerRadius: Dp = 20.dp,
    val agendaItemTickRadius: Dp = 8.dp,
    val agendaItemPaddingTop: Dp = 17.dp,
    val agendaItemPaddingHorizontal: Dp = 16.dp,
    val agendaItemPaddingBottom: Dp = 12.dp,
    val agendaItemTickStrokeWidth: Dp = 1.5.dp,
    val overviewStartPadding: Dp = 14.dp,
    val overviewEndPadding: Dp = spaceSmall,
    val agendaItemHeight: Dp = 125.dp,
    val agendaItemTitlePaddingStart: Dp = 10.5.dp,
    val agendaItemDescriptionPaddingStart: Dp = 28.dp,
    val agendaItemDescriptionPaddingEnd: Dp = 15.dp,
    val agendaItemTimePaddingVertical: Dp = 12.dp,
    val agendaDateWidgetHeight: Dp = 61.dp,
    val agendaDateWidgetWidth: Dp = 40.dp,
    val agendaDateWidgetCornerRadius: Dp = 100.dp,
    val agendaDateWidgetOffset: Dp = 30.dp,
)

val LocalSpacing = compositionLocalOf { Dimensions() }
