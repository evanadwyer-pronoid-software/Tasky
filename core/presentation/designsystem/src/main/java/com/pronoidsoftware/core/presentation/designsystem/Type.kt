package com.pronoidsoftware.core.presentation.designsystem

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val Inter = FontFamily(
    Font(
        resId = R.font.inter_extralight,
        weight = FontWeight.ExtraLight,
    ),
    Font(
        resId = R.font.inter_light,
        weight = FontWeight.Light,
    ),
    Font(
        resId = R.font.inter_thin,
        weight = FontWeight.Thin,
    ),
    Font(
        resId = R.font.inter_regular,
        weight = FontWeight.Normal,
    ),
    Font(
        resId = R.font.inter_medium,
        weight = FontWeight.Medium,
    ),
    Font(
        resId = R.font.inter_semibold,
        weight = FontWeight.SemiBold,
    ),
    Font(
        resId = R.font.inter_bold,
        weight = FontWeight.Bold,
    ),
    Font(
        resId = R.font.inter_extrabold,
        weight = FontWeight.ExtraBold,
    ),
    Font(
        resId = R.font.inter_black,
        weight = FontWeight.Black,
    ),
)

val Typography = Typography(
    bodySmall = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp,
        lineHeight = 20.sp,
        color = TaskyDarkGray,
    ),
    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.5.sp,
    ),
    labelLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 30.sp,
    ),
    labelMedium = TextStyle(
        // used for register annotated string
        fontFamily = Inter,
        fontWeight = FontWeight.Medium,
        fontSize = 14.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.5.sp,
    ),
    headlineMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.SemiBold,
        fontSize = 24.sp,
        color = TaskyBlack,
    ),
    headlineLarge = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 30.sp,
    ),
)
