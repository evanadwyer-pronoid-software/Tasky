package com.pronoidsoftware.agenda.presentation.overview.model

import androidx.compose.ui.graphics.Color
import com.pronoidsoftware.core.domain.agenda.AgendaItem
import com.pronoidsoftware.core.presentation.designsystem.TaskyBlack
import com.pronoidsoftware.core.presentation.designsystem.TaskyBrown
import com.pronoidsoftware.core.presentation.designsystem.TaskyDarkGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyGreen
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightGreen
import com.pronoidsoftware.core.presentation.designsystem.TaskyWhite
import kotlinx.datetime.LocalDateTime

data class AgendaOverviewItemUi(
    val agendaOverviewItemUiId: String,
    val type: AgendaItem,
    val title: String,
    val description: String,
    val agendaOverviewItemUiFromTime: LocalDateTime,
    val toTime: String?,
    val completed: Boolean,
) : AgendaOverviewListItem(
    id = agendaOverviewItemUiId,
    fromTime = agendaOverviewItemUiFromTime,
) {
    val backgroundColor: Color = when (type) {
        AgendaItem.REMINDER -> TaskyLightGray
        AgendaItem.TASK -> TaskyGreen
        AgendaItem.EVENT -> TaskyLightGreen
    }
    val contentColor = if (type == AgendaItem.TASK) {
        ContentColors(
            tick = TaskyWhite,
            title = TaskyWhite,
            menu = TaskyWhite,
            description = TaskyWhite,
            time = TaskyWhite,
        )
    } else {
        ContentColors(
            tick = TaskyBlack,
            title = TaskyBlack,
            menu = TaskyBrown,
            description = TaskyDarkGray,
            time = TaskyDarkGray,
        )
    }

    data class ContentColors(
        val tick: Color,
        val title: Color,
        val menu: Color,
        val description: Color,
        val time: Color,
    )
}
