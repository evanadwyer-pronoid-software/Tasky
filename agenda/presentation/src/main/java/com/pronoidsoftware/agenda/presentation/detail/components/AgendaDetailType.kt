package com.pronoidsoftware.agenda.presentation.detail.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pronoidsoftware.agenda.domain.detail.model.EVENT
import com.pronoidsoftware.agenda.domain.detail.model.REMINDER
import com.pronoidsoftware.agenda.domain.detail.model.TASK
import com.pronoidsoftware.agenda.presentation.overview.model.AgendaOverviewItemUi
import com.pronoidsoftware.agenda.presentation.util.AgendaOverviewItemUiParameterProvider
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.designsystem.TaskyBlack
import com.pronoidsoftware.core.presentation.designsystem.TaskyDarkGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyGreen
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightGreen
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

@Composable
fun AgendaDetailType(
    type: String,
    modifier: Modifier = Modifier,
    fillColor: Color = when (type) {
        TASK -> TaskyGreen
        EVENT -> TaskyLightGreen
        REMINDER -> TaskyLightGray
        else -> TaskyBlack
    },
    borderColor: Color = when (type) {
        "Reminder" -> TaskyGray
        else -> Color.Transparent
    },
    boxSize: Dp = 20.dp,
    boxCornerRadius: Dp = 2.dp,
) {
    val density = LocalDensity.current
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Canvas(
            modifier = Modifier
                .size(boxSize)
                .clip(RoundedCornerShape(boxCornerRadius))
                .border(
                    width = 1.dp,
                    color = borderColor,
                ),
        ) {
            val cornerRadiusPx = with(density) { boxCornerRadius.toPx() }
            drawRoundRect(
                color = fillColor,
                cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
            )
        }
        Spacer(modifier = Modifier.width(10.dp))
        Text(
            text = type,
            style = TextStyle(
                fontFamily = Inter,
                fontWeight = FontWeight.W600,
                fontSize = 16.sp,
                lineHeight = 19.2.sp,
                color = TaskyDarkGray,
            ),
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AgendaDetailTypePreview(
    @PreviewParameter(AgendaOverviewItemUiParameterProvider::class) type: AgendaOverviewItemUi,
) {
    TaskyTheme {
        when (type) {
            is AgendaOverviewItemUi.EventOverviewUi -> {
                AgendaDetailType(type = EVENT)
            }

            is AgendaOverviewItemUi.TaskOverviewUi -> {
                AgendaDetailType(type = TASK)
            }

            is AgendaOverviewItemUi.ReminderOverviewUi -> {
                AgendaDetailType(type = REMINDER)
            }
        }
    }
}
