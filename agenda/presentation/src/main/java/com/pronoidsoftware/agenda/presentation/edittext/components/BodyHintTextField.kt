package com.pronoidsoftware.agenda.presentation.edittext.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.designsystem.TaskyBlack
import com.pronoidsoftware.core.presentation.designsystem.TaskyGray2
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

@Composable
fun BodyHintTextField(
    state: TextFieldState,
    hint: String,
    modifier: Modifier = Modifier,
    background: Color = MaterialTheme.colorScheme.background,
    textStyle: TextStyle = TextStyle(color = TaskyBlack),
) {
    var isFocused by remember {
        mutableStateOf(false)
    }

    BasicTextField(
        state = state,
        modifier = modifier
            .fillMaxSize()
            .background(background)
            .onFocusChanged {
                isFocused = it.isFocused
            },
        textStyle = textStyle,
        decorator = { innerBox ->
            if (state.text.isEmpty() && !isFocused) {
                Text(
                    text = hint,
                    style = textStyle.copy(
                        color = TaskyGray2,
                    ),
                )
            } else {
                innerBox()
            }
        },
    )
}

@Preview
@Composable
private fun TransparentHintTextFieldPreview() {
    TaskyTheme {
        BodyHintTextField(
            state = TextFieldState(initialText = ""),
            hint = "Title",
            textStyle = TextStyle(
                fontFamily = Inter,
                fontWeight = FontWeight.W400,
                fontSize = 26.sp,
                lineHeight = 12.sp,
            ),
        )
    }
}
