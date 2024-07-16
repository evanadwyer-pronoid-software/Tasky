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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.core.presentation.designsystem.ForwardChevronIcon
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.designsystem.NotificationIcon
import com.pronoidsoftware.core.presentation.designsystem.TaskyGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

@Composable
fun AgendaDetailNotification(
    reminderDescription: String,
    modifier: Modifier = Modifier,
    icon: ImageVector = NotificationIcon,
    isEditable: Boolean = false,
    onEdit: () -> Unit = {},
) {
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
        )
        Spacer(modifier = Modifier.weight(1f))
        if (isEditable) {
            IconButton(onClick = onEdit) {
                Icon(
                    imageVector = ForwardChevronIcon,
                    contentDescription = stringResource(id = R.string.edit),
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AgendaDetailReminderPreview() {
    TaskyTheme {
        Column(
            verticalArrangement = Arrangement.SpaceBetween,
        ) {
            AgendaDetailNotification(
                reminderDescription = "30 minutes before",
            )
            AgendaDetailNotification(
                reminderDescription = "30 minutes before",
                isEditable = true,
            )
        }
    }
}
