package com.pronoidsoftware.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicSecureTextField
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pronoidsoftware.core.presentation.designsystem.EyeClosedIcon
import com.pronoidsoftware.core.presentation.designsystem.EyeOpenedIcon
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.R
import com.pronoidsoftware.core.presentation.designsystem.TaskyGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyGray2
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

@Composable
fun TaskyPasswordTextField(
    state: TextFieldState,
    isPasswordVisible: Boolean,
    onTogglePasswordVisibility: () -> Unit,
    hint: String,
    modifier: Modifier = Modifier,
    passwordContentDescription: String? = null,
    error: Boolean = false,
) {
    var isFocused by remember {
        mutableStateOf(false)
    }
    val spacing = LocalSpacing.current
    BasicSecureTextField(
        state = state,
        textObfuscationMode = if (isPasswordVisible) {
            TextObfuscationMode.Visible
        } else {
            TextObfuscationMode.Hidden
        },
        textStyle = MaterialTheme.typography.bodyLarge.copy(
            color = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
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
            }
            .semantics {
                passwordContentDescription?.let {
                    contentDescription = it
                }
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
                Spacer(modifier = Modifier.width(spacing.spaceMedium))
                IconButton(
                    onClick = onTogglePasswordVisibility,
                    modifier = Modifier.size(24.dp),
                ) {
                    Icon(
                        imageVector = if (isPasswordVisible) {
                            EyeOpenedIcon
                        } else {
                            EyeClosedIcon
                        },
                        contentDescription = if (isPasswordVisible) {
                            stringResource(id = R.string.hide_password)
                        } else {
                            stringResource(id = R.string.show_password)
                        },
                        tint = TaskyGray,
                    )
                }
            }
        },
    )
}

@Preview
@Composable
private fun TaskyPasswordTextFieldPreview() {
    TaskyTheme {
        TaskyPasswordTextField(
            state = TextFieldState("Test12345"),
            isPasswordVisible = false,
            onTogglePasswordVisibility = {},
            hint = "Password",
            passwordContentDescription = "Password is valid",
            error = false,
        )
    }
}
