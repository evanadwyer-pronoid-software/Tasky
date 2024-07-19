package com.pronoidsoftware.agenda.presentation.detail.components.edittext

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.ui.UiText

sealed class EditTextType(
    val toolbarTitle: UiText,
    val bodyTextStyle: TextStyle,
) {
    data object Title : EditTextType(
        toolbarTitle = UiText.StringResource(R.string.edit_title),
        bodyTextStyle = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.W400,
            fontSize = 26.sp,
            lineHeight = 30.sp,
        ),
    )

    data object Description : EditTextType(
        toolbarTitle = UiText.StringResource(R.string.edit_description),
        bodyTextStyle = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.W400,
            fontSize = 16.sp,
            lineHeight = 25.sp,
        ),
    )
}
