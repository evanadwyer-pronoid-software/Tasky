package com.pronoidsoftware.core.presentation.designsystem.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import com.pronoidsoftware.core.presentation.designsystem.EllipsesIcon
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.TaskyBrown
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.core.presentation.designsystem.TaskyWhite
import com.pronoidsoftware.core.presentation.designsystem.TaskyWhite2
import com.pronoidsoftware.core.presentation.designsystem.util.crop

@Composable
fun TaskyDropdownMenu(
    items: List<String>,
    expanded: Boolean,
    toggleExpanded: () -> Unit,
    onMenuItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val spacing = LocalSpacing.current
    Box(
        modifier = modifier,
    ) {
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                toggleExpanded()
            },
            modifier = Modifier
                .crop(vertical = spacing.spaceSmall)
                .clip(RoundedCornerShape(spacing.dropdownMenuCornerRadius))
                .background(TaskyWhite),
        ) {
            items.forEachIndexed { index, dropdownItem ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = dropdownItem,
                            style = MaterialTheme.typography.labelSmall,
                        )
                    },
                    onClick = {
                        onMenuItemClick(index)
                    },
                )
                if (index < items.size) {
                    HorizontalDivider(color = TaskyWhite2)
                }
            }
        }
        content()
    }
}

@Preview
@Composable
private fun TaskyDropdownMenuPreview() {
    TaskyTheme {
        var expanded by remember {
            mutableStateOf(true)
        }
        val toggleExpanded = {
            expanded = !expanded
        }
        TaskyDropdownMenu(
            items = listOf("Open", "Edit", "Delete"),
            expanded = expanded,
            onMenuItemClick = { },
            toggleExpanded = toggleExpanded,
        ) {
            IconButton(
                onClick = toggleExpanded,
            ) {
                Icon(
                    imageVector = EllipsesIcon,
                    contentDescription = "More",
                    tint = TaskyBrown,
                )
            }
        }
    }
}
