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
        // agenda overview item body
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 15.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp,
        lineHeight = 22.sp,
    ),
    bodyLarge = TextStyle(
        // auth text fields
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 30.sp,
        letterSpacing = 0.5.sp,
    ),
    labelLarge = TextStyle(
        // action button
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
    labelSmall = TextStyle(
        // menu items
        fontFamily = Inter,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 15.sp,
        color = TaskyBlack,
    ),
    headlineSmall = TextStyle(
        // profile badge
        fontFamily = Inter,
        fontWeight = FontWeight.W600,
        fontSize = 12.5.sp,
        lineHeight = 15.sp,
    ),
    headlineMedium = TextStyle(
        // agenda datepicker
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        lineHeight = 19.2.sp,
    ),
    headlineLarge = TextStyle(
        // Auth toolbar heading
        fontFamily = Inter,
        fontWeight = FontWeight.Bold,
        fontSize = 28.sp,
        lineHeight = 30.sp,
    ),
    titleMedium = TextStyle(
        // title of agenda overview item
        fontFamily = Inter,
        fontWeight = FontWeight.W700,
        fontSize = 20.sp,
        lineHeight = 16.sp,
    ),
    displaySmall = TextStyle(
        // agenda overview date widget
        fontFamily = Inter,
        fontWeight = FontWeight.W700,
        fontSize = 11.sp,
        lineHeight = 13.2.sp,
    ),
)
