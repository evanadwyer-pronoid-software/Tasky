package com.pronoidsoftware.core.presentation.designsystem.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pronoidsoftware.core.presentation.designsystem.BackChevronIcon
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

@Composable
fun TaskyFloatingActionButton(
    icon: ImageVector,
    onClick: () -> Unit,
    contentDescription: String? = null,
    iconSize: Dp = 40.dp,
) {
    FloatingActionButton(
        onClick = {
            onClick()
        },
        elevation = FloatingActionButtonDefaults.elevation(
            0.dp,
        ),
    ) {
        Icon(
            imageVector = icon,
            contentDescription = contentDescription,
            modifier = Modifier
                .size(iconSize),
        )
    }
}

@Preview
@Composable
private fun TaskyFloatingActionButtonPreview() {
    TaskyTheme {
        TaskyFloatingActionButton(
            icon = BackChevronIcon,
            onClick = {},
        )
    }
}
