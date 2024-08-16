package com.pronoidsoftware.core.presentation.ui

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.pronoidsoftware.core.domain.agendaitem.AgendaItemType

@Composable
fun getTypeString(type: AgendaItemType?, isUppercase: Boolean = false): String {
    val result = when (type) {
        AgendaItemType.EVENT -> stringResource(id = R.string.event)
        AgendaItemType.TASK -> stringResource(id = R.string.task)
        AgendaItemType.REMINDER -> stringResource(id = R.string.reminder)
        null -> ""
    }
    return if (isUppercase) {
        result.uppercase()
    } else {
        result
    }
}

fun getTypeString(type: AgendaItemType?, isUppercase: Boolean = false, context: Context): String {
    val result = when (type) {
        AgendaItemType.EVENT -> context.resources.getString(R.string.event)
        AgendaItemType.TASK -> context.resources.getString(R.string.task)
        AgendaItemType.REMINDER -> context.resources.getString(R.string.reminder)
        null -> ""
    }
    return if (isUppercase) {
        result.uppercase()
    } else {
        result
    }
}
