package com.pronoidsoftware.agenda.presentation.util

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.pronoidsoftware.core.domain.agendaitem.AgendaItemType

internal class AgendaItemTypeParameterProvider : PreviewParameterProvider<AgendaItemType> {
    override val values: Sequence<AgendaItemType> =
        sequenceOf(
            AgendaItemType.TASK,
            AgendaItemType.EVENT,
            AgendaItemType.REMINDER,
        )
}
