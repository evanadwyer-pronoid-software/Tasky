package com.pronoidsoftware.agenda.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pronoidsoftware.core.presentation.designsystem.Dimensions
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.TaskyBlack
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.core.presentation.designsystem.TaskyWhite

@Composable
fun TimeMarker(
    modifier: Modifier = Modifier,
    color: Color = TaskyBlack,
    strokeWidth: Dp = Dimensions().timeMarkerStrokeWidth,
    ballRadius: Dp = Dimensions().timeMarkerBallRadius,
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        drawCircle(
            color = color,
            center = Offset(
                x = ballRadius.toPx(),
                y = center.y,
            ),
            radius = ballRadius.toPx(),
        )
        drawLine(
            color = color,
            start = Offset(
                x = ballRadius.toPx(),
                y = center.y,
            ),
            end = Offset(
                x = size.width,
                y = center.y,
            ),
            strokeWidth = strokeWidth.toPx(),
        )
    }
}

@Preview
@Composable
private fun TimeMarkerPreview() {
    TaskyTheme {
        val spacing = LocalSpacing.current
        TimeMarker(
            modifier = Modifier
                .background(TaskyWhite)
                .fillMaxWidth()
                .padding(
                    start = spacing.overviewStartPadding,
                    end = spacing.overviewEndPadding,
                    top = spacing.timeMarkerBallRadius + 2.dp,
                    bottom = spacing.timeMarkerBallRadius + 2.dp,
                ),
        )
    }
}
