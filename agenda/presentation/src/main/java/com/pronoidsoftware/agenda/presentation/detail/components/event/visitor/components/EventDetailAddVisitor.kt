package com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.PlusIcon
import com.pronoidsoftware.core.presentation.designsystem.TaskyBlack
import com.pronoidsoftware.core.presentation.designsystem.TaskyGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

@Composable
fun EventDetailAddVisitor(
    title: String,
    editEnabled: Boolean,
    onAddVisitorClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Text(
            text = title,
            style = TextStyle(
                fontFamily = Inter,
                fontWeight = FontWeight.W700,
                fontSize = 20.sp,
                lineHeight = 16.sp,
            ),
            color = TaskyBlack,
        )
        Spacer(modifier = Modifier.width(spacing.visitorAddButtonSpacing))
        IconButton(
            enabled = editEnabled,
            onClick = onAddVisitorClick,
            modifier = Modifier
                .alpha(if (editEnabled) 1f else 0f)
                .clip(RoundedCornerShape(spacing.visitorAddButtonCornerRadius))
                .background(TaskyLightGray),
        ) {
            Icon(
                imageVector = PlusIcon,
                contentDescription = stringResource(id = R.string.add_visitor),
                tint = TaskyGray,
                modifier = Modifier,
            )
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFF00)
@Composable
private fun EventDetailAddVisitorPreview_NotEditable() {
    TaskyTheme {
        EventDetailAddVisitor(
            title = "Visitors",
            editEnabled = false,
            onAddVisitorClick = { },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFF00)
@Composable
private fun EventDetailAddVisitorPreview_Editable() {
    TaskyTheme {
        EventDetailAddVisitor(
            title = "Visitors",
            editEnabled = true,
            onAddVisitorClick = { },
        )
    }
}
