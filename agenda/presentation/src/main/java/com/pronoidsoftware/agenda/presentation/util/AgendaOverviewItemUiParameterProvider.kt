package com.pronoidsoftware.agenda.presentation.util

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.pronoidsoftware.core.domain.agenda.AgendaItem

internal class AgendaOverviewItemUiParameterProvider : PreviewParameterProvider<AgendaItem> {
    override val values: Sequence<AgendaItem> =
        sequenceOf(
            AgendaItem.TASK,
            AgendaItem.EVENT,
            AgendaItem.REMINDER,
        )
}
