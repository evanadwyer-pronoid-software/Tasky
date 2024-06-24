package com.pronoidsoftware.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.pronoidsoftware.core.presentation.designsystem.CheckIcon
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.TaskyGray2
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

@Composable
fun TaskyTextField(
    state: TextFieldState,
    hint: String,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    endIcon: ImageVector? = null,
    endIconContentDescription: String? = null,
    error: Boolean = false,
) {
    var isFocused by remember {
        mutableStateOf(false)
    }
    val spacing = LocalSpacing.current
    BasicTextField(
        state = state,
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        keyboardOptions = KeyboardOptions(
            keyboardType = keyboardType,
        ),
        lineLimits = TextFieldLineLimits.SingleLine,
        cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurfaceVariant),
        modifier = modifier
            .clip(RoundedCornerShape(spacing.spaceSmallMedium))
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(
                width = spacing.stroke,
                color = if (error) {
                    MaterialTheme.colorScheme.error
                } else {
                    Color.Transparent
                },
                shape = RoundedCornerShape(spacing.spaceSmallMedium),
            )
            .padding(
                vertical = spacing.spaceMedium,
                horizontal = spacing.spaceMediumLarge,
            )
            .onFocusChanged {
                isFocused = it.isFocused
            },
        decorator = { innerBox ->
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Box(
                    modifier = Modifier.weight(1f),
                ) {
                    if (state.text.isEmpty() && !isFocused) {
                        Text(
                            text = hint,
                            style = MaterialTheme.typography.bodyLarge.copy(
                                color = TaskyGray2,
                            ),
                            modifier = Modifier.fillMaxWidth(),
                        )
                    }
                    innerBox()
                }
                if (endIcon != null) {
                    Spacer(modifier = Modifier.width(spacing.spaceMedium))
                    Icon(
                        imageVector = endIcon,
                        contentDescription = endIconContentDescription,
                        tint = MaterialTheme.colorScheme.primary,
                    )
                }
            }
        },
    )
}

@Preview
@Composable
private fun TaskyTextFieldPreview() {
    TaskyTheme {
        TaskyTextField(
            state = rememberTextFieldState("example@pronoid-software.com"),
            endIcon = CheckIcon,
            endIconContentDescription = "Email is valid",
            hint = "Email address",
            error = false,
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}
