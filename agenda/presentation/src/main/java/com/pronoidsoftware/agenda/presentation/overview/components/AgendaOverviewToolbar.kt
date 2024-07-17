@file:OptIn(ExperimentalMaterial3Api::class)

package com.pronoidsoftware.agenda.presentation.overview.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.agenda.presentation.overview.AgendaOverviewAction
import com.pronoidsoftware.core.domain.util.today
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightBlue2
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.core.presentation.designsystem.TaskyWhite2
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyDropdownMenu
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyProfileBadge
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import timber.log.Timber

@Composable
fun AgendaOverviewToolbar(
    userInitials: String,
    selectedDate: LocalDate,
    onAction: (AgendaOverviewAction) -> Unit,
    modifier: Modifier = Modifier,
    clock: Clock = Clock.System,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
) {
    val spacing = LocalSpacing.current
    var profileDropdownExpanded by remember {
        mutableStateOf(false)
    }
    val toggleProfileDropdownExpanded = {
        profileDropdownExpanded = !profileDropdownExpanded
    }
    var datePickerExpanded by remember {
        mutableStateOf(false)
    }
    val toggleDatePickerExpanded = {
        datePickerExpanded = !datePickerExpanded
    }
    TopAppBar(
        modifier = modifier,
        title = {
            AgendaOverviewDatePicker(
                selectedDate = selectedDate,
                onSelectDate = { date ->
                    onAction(AgendaOverviewAction.OnSelectDate(date))
                },
                expanded = datePickerExpanded,
                toggleExpanded = toggleDatePickerExpanded,
                clock = clock,
            )
        },
        scrollBehavior = scrollBehavior,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
        actions = {
            TaskyDropdownMenu(
                items = listOf(
                    stringResource(id = R.string.logout),
                ),
                expanded = profileDropdownExpanded,
                onMenuItemClick = { index ->
                    when (index) {
                        0 -> onAction(AgendaOverviewAction.OnLogoutClick)
                        else -> Timber.wtf("Unknown AgendaOverview Profile badge click")
                    }
                },
                toggleExpanded = toggleProfileDropdownExpanded,
            ) {
                TaskyProfileBadge(
                    initials = userInitials,
                    initialColors = TaskyLightBlue2,
                    backgroundColor = TaskyWhite2,
                    onClick = toggleProfileDropdownExpanded,
                    modifier = Modifier
                        .padding(end = spacing.spaceMediumSmall),
                )
            }
        },
    )
}

@Preview
@Composable
private fun AgendaOverviewToolbarPreview() {
    TaskyTheme {
        AgendaOverviewToolbar(
            userInitials = "AB",
            onAction = {},
            selectedDate = today(),
        )
    }
}
