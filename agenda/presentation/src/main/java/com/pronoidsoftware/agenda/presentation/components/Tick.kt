package com.pronoidsoftware.agenda.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import com.pronoidsoftware.agenda.domain.AgendaItem
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.agenda.presentation.util.AgendaOverviewItemUiParameterProvider
import com.pronoidsoftware.core.presentation.designsystem.CheckIcon
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.TaskyBlack
import com.pronoidsoftware.core.presentation.designsystem.TaskyGreen
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightGreen
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.core.presentation.designsystem.TaskyWhite

@Composable
fun Tick(
    color: Color,
    ticked: Boolean,
    radius: Dp,
    strokeWidth: Dp,
    modifier: Modifier = Modifier,
    tickIcon: ImageVector = CheckIcon,
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .size(radius * 2 + strokeWidth)
            .clip(CircleShape)
            .clickable(enabled = onClick != null) {
                onClick?.invoke()
            },
        contentAlignment = Alignment.Center,
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize(),
        ) {
            drawCircle(
                color = color,
                radius = radius.toPx(),
                style = Stroke(
                    width = strokeWidth.toPx(),
                ),
                center = center,
            )
        }
        if (ticked) {
            Icon(
                imageVector = tickIcon,
                contentDescription = stringResource(id = R.string.completed),
                tint = color,
                modifier = Modifier.size(radius),
            )
        }
    }
}

@Preview
@Composable
private fun TickPreview(
    @PreviewParameter(AgendaOverviewItemUiParameterProvider::class) type: AgendaItem,
) {
    val backgroundColor = when (type) {
        AgendaItem.REMINDER -> TaskyLightGray
        AgendaItem.TASK -> TaskyGreen
        AgendaItem.EVENT -> TaskyLightGreen
    }
    val spacing = LocalSpacing.current
    TaskyTheme {
        Tick(
            color = if (type == AgendaItem.TASK) TaskyWhite else TaskyBlack,
            ticked = type == AgendaItem.TASK,
            radius = spacing.agendaItemTickRadius,
            strokeWidth = spacing.agendaItemTickStrokeWidth,
            modifier = Modifier
                .background(backgroundColor),
        )
    }
}
