package com.pronoidsoftware.agenda.presentation.util

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.pronoidsoftware.agenda.domain.model.AgendaItemType

internal class AgendaOverviewItemUiParameterProvider : PreviewParameterProvider<AgendaItemType> {
    override val values: Sequence<AgendaItemType> =
        sequenceOf(
            AgendaItemType.TASK,
            AgendaItemType.EVENT,
            AgendaItemType.REMINDER,
        )
}
