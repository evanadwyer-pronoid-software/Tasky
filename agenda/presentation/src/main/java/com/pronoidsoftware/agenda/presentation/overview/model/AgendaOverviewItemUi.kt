package com.pronoidsoftware.agenda.presentation.overview.model

import androidx.compose.ui.graphics.Color
import com.pronoidsoftware.agenda.domain.model.AgendaItemType
import com.pronoidsoftware.core.presentation.designsystem.TaskyBlack
import com.pronoidsoftware.core.presentation.designsystem.TaskyBrown
import com.pronoidsoftware.core.presentation.designsystem.TaskyDarkGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyGreen
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightGreen
import com.pronoidsoftware.core.presentation.designsystem.TaskyWhite

data class AgendaOverviewItemUi(
    val id: String,
    val type: AgendaItemType,
    val title: String,
    val description: String,
    val fromTime: String,
    val toTime: String? = null,
    val completed: Boolean = false,
) {
    val backgroundColor: Color = when (type) {
        AgendaItemType.REMINDER -> TaskyLightGray
        AgendaItemType.TASK -> TaskyGreen
        AgendaItemType.EVENT -> TaskyLightGreen
    }
    val contentColor = if (type == AgendaItemType.TASK) {
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
