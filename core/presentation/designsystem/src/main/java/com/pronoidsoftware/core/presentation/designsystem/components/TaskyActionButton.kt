package com.pronoidsoftware.core.presentation.designsystem.components

import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.TaskyDarkGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

@Composable
fun TaskyActionButton(
    text: String,
    isLoading: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val spacing = LocalSpacing.current
    Button(
        onClick = onClick,
        enabled = enabled,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            disabledContainerColor = TaskyLightGray,
            disabledContentColor = TaskyDarkGray,
        ),
        shape = RoundedCornerShape(100f),
        modifier = modifier
            .widthIn(max = spacing.maxButtonWidth)
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        contentPadding = PaddingValues(
            start = spacing.spaceLargeMedium,
            end = spacing.spaceLargeMedium,
            top = spacing.spaceMediumSmall,
            bottom = spacing.spaceMediumSmall,
        ),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(15.dp),
                strokeWidth = spacing.strokeBold,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
            )
        } else {
            Text(
                text = text,
                style = MaterialTheme.typography.labelLarge,
            )
        }
    }
}

class IsLoadingPreviewParameterProvider : PreviewParameterProvider<Boolean> {
    override val values: Sequence<Boolean> = sequenceOf(true, false)
}

@Preview
@Composable
private fun TaskyActionButtonPreview(
    @PreviewParameter(IsLoadingPreviewParameterProvider::class) isLoading: Boolean,
) {
    TaskyTheme {
        TaskyActionButton(
            text = "GET STARTED",
            isLoading = isLoading,
            onClick = {},
            enabled = true,
        )
    }
}
