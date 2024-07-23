package com.pronoidsoftware.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.pronoidsoftware.core.presentation.designsystem.TaskyGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightBlue2
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.core.presentation.designsystem.TaskyWhite
import com.pronoidsoftware.core.presentation.designsystem.TaskyWhite2

@Composable
fun TaskyProfileBadge(
    initials: String,
    initialColors: Color,
    backgroundColor: Color,
    modifier: Modifier = Modifier,
    onClick: (() -> Unit)? = null,
) {
    Box(
        modifier = modifier
            .size(34.dp)
            .clip(CircleShape)
            .background(backgroundColor)
            .clickable(enabled = onClick != null) {
                onClick?.invoke()
            },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = initials,
            color = initialColors,
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

@Preview
@Composable
private fun TaskyProfileAgendaOverviewPreview() {
    TaskyTheme {
        TaskyProfileBadge(
            initials = "AB",
            initialColors = TaskyLightBlue2,
            backgroundColor = TaskyWhite2,
        )
    }
}

@Preview
@Composable
private fun TaskyProfileEventAttendeePreview() {
    TaskyTheme {
        TaskyProfileBadge(
            initials = "AA",
            initialColors = TaskyWhite,
            backgroundColor = TaskyGray,
        )
    }
}
