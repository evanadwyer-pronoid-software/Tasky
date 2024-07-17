package com.pronoidsoftware.agenda.presentation.detail.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.agenda.presentation.model.NotificationDuration
import com.pronoidsoftware.core.presentation.designsystem.ForwardChevronIcon
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.designsystem.NotificationIcon
import com.pronoidsoftware.core.presentation.designsystem.TaskyGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyDropdownMenu

@Composable
fun AgendaDetailNotification(
    reminderDescription: String,
    expanded: Boolean,
    toggleExpanded: () -> Unit,
    onEdit: () -> Unit,
    onSelectNotificationDuration: (NotificationDuration) -> Unit,
    modifier: Modifier = Modifier,
    icon: ImageVector = NotificationIcon,
    isEditable: Boolean = false,
) {
    val contentColor = MaterialTheme.colorScheme.onBackground
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            tint = TaskyGray,
            contentDescription = stringResource(id = R.string.reminder),
            modifier = Modifier
                .size(30.dp)
                .clip(RoundedCornerShape(5.dp))
                .background(TaskyLightGray),
        )
        Spacer(modifier = Modifier.width(13.dp))
        Text(
            text = reminderDescription,
            style = TextStyle(
                fontFamily = Inter,
                fontWeight = FontWeight.W400,
                fontSize = 16.sp,
                lineHeight = 15.sp,
            ),
            color = contentColor,
        )
        Spacer(modifier = Modifier.weight(1f))
        TaskyDropdownMenu(
            items = NotificationDuration.notificationDurationOptions()
                .map { it.text.asString() },
            expanded = expanded,
            toggleExpanded = toggleExpanded,
            onMenuItemClick = { index ->
                toggleExpanded()
                onSelectNotificationDuration(
                    NotificationDuration.notificationDurationOptions()[index],
                )
            },
        ) {
            IconButton(
                enabled = isEditable,
                onClick = onEdit,
                modifier = Modifier.alpha(if (isEditable) 1f else 0f),
            ) {
                Icon(
                    imageVector = ForwardChevronIcon,
                    contentDescription = stringResource(id = R.string.edit),
                    tint = contentColor,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AgendaDetailReminderPreview() {
    TaskyTheme {
        var expanded by remember {
            mutableStateOf(false)
        }
        val toggleExpanded = {
            expanded = !expanded
        }
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            AgendaDetailNotification(
                reminderDescription = "30 minutes before",
                expanded = expanded,
                toggleExpanded = toggleExpanded,
                onEdit = { },
                onSelectNotificationDuration = { },
            )
            AgendaDetailNotification(
                reminderDescription = "30 minutes before",
                expanded = expanded,
                toggleExpanded = toggleExpanded,
                onEdit = { },
                onSelectNotificationDuration = { },
                isEditable = true,
            )
        }
    }
}
