package com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.core.domain.util.initializeAndCapitalize
import com.pronoidsoftware.core.presentation.designsystem.DeleteIcon
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.TaskyDarkGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightBlue2
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.core.presentation.designsystem.TaskyWhite
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyProfileBadge

@Composable
fun EventDetailVisitorDetail(
    fullName: String,
    isCreator: Boolean,
    editEnabled: Boolean,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(spacing.visitorDetailCornerRadius))
            .background(TaskyLightGray),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        TaskyProfileBadge(
            initials = fullName.initializeAndCapitalize(),
            initialColors = TaskyWhite,
            backgroundColor = TaskyGray,
            modifier = Modifier
                .padding(
                    start = spacing.visitorDetailStartPadding,
                    end = spacing.visitorDetailMiddlePadding,
                ),
        )
        Text(
            text = fullName,
            style = TextStyle(
                fontFamily = Inter,
                fontWeight = FontWeight.W500,
                fontSize = 14.sp,
                lineHeight = 12.sp,
            ),
            color = TaskyDarkGray,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .widthIn(max = spacing.visitorDetailMaxTextWidth),
        )
        Spacer(modifier = Modifier.weight(1f))
        Box(
            modifier = Modifier.padding(end = spacing.visitorDetailEndPadding),
            contentAlignment = Alignment.CenterEnd,
        ) {
            Text(
                text = stringResource(id = R.string.creator),
                style = TextStyle(
                    fontFamily = Inter,
                    fontWeight = FontWeight.W500,
                    fontSize = 14.sp,
                    lineHeight = 15.sp,
                ),
                color = TaskyLightBlue2,
                modifier = Modifier
                    .alpha(if (isCreator) 1f else 0f),
            )
            IconButton(
                enabled = editEnabled && !isCreator,
                onClick = onDeleteClick,
                modifier = Modifier
                    .alpha(if (editEnabled && !isCreator) 1f else 0f),
            ) {
                Icon(
                    imageVector = DeleteIcon,
                    contentDescription = stringResource(id = R.string.remove_visitor),
                    tint = TaskyDarkGray,
                )
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun EventDetailVisitorDetailPreview_Creator_Editable() {
    TaskyTheme {
        EventDetailVisitorDetail(
            fullName = "Ann Allen",
            isCreator = true,
            editEnabled = true,
            onDeleteClick = { },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun EventDetailVisitorDetailPreview_Creator_NotEditable() {
    TaskyTheme {
        EventDetailVisitorDetail(
            fullName = "Ann Allen",
            isCreator = true,
            editEnabled = false,
            onDeleteClick = { },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun EventDetailVisitorDetailPreview_Visitor_Editable() {
    TaskyTheme {
        EventDetailVisitorDetail(
            fullName = "Wade Warren",
            isCreator = false,
            editEnabled = true,
            onDeleteClick = { },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun EventDetailVisitorDetailPreview_Visitor_Editable_LongName() {
    TaskyTheme {
        EventDetailVisitorDetail(
            fullName = "Wade Warren the Third the Fifth thank you very much",
            isCreator = false,
            editEnabled = true,
            onDeleteClick = { },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
private fun EventDetailVisitorDetailPreview_Visitor_NotEditable() {
    TaskyTheme {
        EventDetailVisitorDetail(
            fullName = "Esther Howard",
            isCreator = false,
            editEnabled = false,
            onDeleteClick = { },
        )
    }
}
