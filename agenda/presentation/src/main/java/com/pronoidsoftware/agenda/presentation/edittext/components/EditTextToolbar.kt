@file:OptIn(ExperimentalMaterial3Api::class)

package com.pronoidsoftware.agenda.presentation.edittext.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.core.presentation.designsystem.BackChevronIcon
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

@Composable
fun EditTextToolbar(
    title: String,
    actionText: String,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
) {
    CenterAlignedTopAppBar(
        scrollBehavior = scrollBehavior,
        modifier = modifier,
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.displayLarge,
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = BackChevronIcon,
                    contentDescription = stringResource(id = R.string.go_back),
                )
            }
        },
        actions = {
            TextButton(onClick = onSaveClick) {
                Text(
                    text = actionText,
                    style = MaterialTheme.typography.displayLarge.copy(
                        fontSize = 16.sp,
                    ),
                )
            }
        },
    )
}

@Preview
@Composable
private fun EditTextToolbarPreview() {
    TaskyTheme {
        EditTextToolbar(
            title = "EDIT DESCRIPTION",
            actionText = "Save",
            onBackClick = {},
            onSaveClick = {},
        )
    }
}
