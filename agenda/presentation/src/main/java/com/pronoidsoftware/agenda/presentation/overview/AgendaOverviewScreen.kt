@file:OptIn(ExperimentalMaterial3Api::class)

package com.pronoidsoftware.agenda.presentation.overview

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pronoidsoftware.agenda.domain.AgendaItem
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.agenda.presentation.overview.components.AgendaOverviewDateWidget
import com.pronoidsoftware.agenda.presentation.overview.components.AgendaOverviewItem
import com.pronoidsoftware.agenda.presentation.overview.components.AgendaOverviewToolbar
import com.pronoidsoftware.agenda.presentation.overview.components.TimeMarker
import com.pronoidsoftware.core.domain.util.today
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.PlusIcon
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyDropdownMenu
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyFloatingActionButton
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyScaffold
import com.pronoidsoftware.core.presentation.ui.ObserveAsEvents
import com.pronoidsoftware.core.presentation.ui.toRelativeDate
import java.util.Locale
import kotlinx.datetime.Clock
import timber.log.Timber

@Composable
fun AgendaOverviewScreenRoot(viewModel: AgendaOverviewViewModel = hiltViewModel()) {
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            else -> {
                Timber.wtf("Unkown AgendaOverview event in screen")
            }
        }
    }

    AgendaOverviewScreen(
        state = viewModel.state,
        clock = viewModel.clock,
        onAction = viewModel::onAction,
    )
}

@Composable
internal fun AgendaOverviewScreen(
    state: AgendaOverviewState,
    onAction: (AgendaOverviewAction) -> Unit,
    clock: Clock = Clock.System,
) {
    val spacing = LocalSpacing.current

    TaskyScaffold(
        topAppBar = {
            AgendaOverviewToolbar(
                userInitials = state.userInitials,
                selectedDate = state.selectedDate,
                onSelectDate = { date ->
                    onAction(AgendaOverviewAction.OnSelectDate(date))
                },
                clock = clock,
                profileDropdownMenuExpanded = state.profileDropdownMenuExpanded,
                toggleProfileDropdownMenuExpanded = {
                    onAction(AgendaOverviewAction.OnToggleProfileDropdownMenu)
                },
                datePickerExpanded = state.datePickerExpanded,
                toggleDatePickerExpanded = {
                    onAction(AgendaOverviewAction.OnToggleDatePickerExpanded)
                },
                onLogoutClick = {
                    onAction(AgendaOverviewAction.OnLogoutClick)
                },
            )
        },
        floatingActionButton = {
            TaskyDropdownMenu(
                items = AgendaItem.entries.map {
                    it.name.lowercase(Locale.getDefault())
                        .replaceFirstChar { character ->
                            character.titlecase(Locale.getDefault())
                        }
                },
                expanded = state.fabDropdownMenuExpanded,
                toggleExpanded = {
                    onAction(AgendaOverviewAction.OnToggleFABDropdownMenuExpanded)
                },
                onMenuItemClick = { index ->
                    onAction(AgendaOverviewAction.OnCreateClick(AgendaItem.entries[index]))
                },
            ) {
                TaskyFloatingActionButton(
                    icon = PlusIcon,
                    contentDescription = stringResource(id = R.string.create_agenda_item),
                    onClick = {
                        onAction(AgendaOverviewAction.OnToggleFABDropdownMenuExpanded)
                    },
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(
                    top = innerPadding.calculateTopPadding(),
                    bottom = innerPadding.calculateBottomPadding(),
                )
                .fillMaxSize()
                .clip(
                    RoundedCornerShape(
                        topStart = spacing.scaffoldContainerRadius,
                        topEnd = spacing.scaffoldContainerRadius,
                    ),
                )
                .background(
                    MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(
                        topStart = spacing.scaffoldContainerRadius,
                        topEnd = spacing.scaffoldContainerRadius,
                    ),
                )
                .padding(top = spacing.scaffoldPaddingTop),
        ) {
            AgendaOverviewDateWidget(
                selectedDate = state.selectedDate,
                onSelectDate = {
                    onAction(AgendaOverviewAction.OnSelectDate(it))
                },
            )
            Spacer(modifier = Modifier.height(34.dp))
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = spacing.spaceMedium),
            ) {
                item {
                    Text(
                        text = state.selectedDate.toRelativeDate(clock).asString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                    Spacer(modifier = Modifier.height(15.5.dp))
                }

                itemsIndexed(
                    items = state.items,
                    key = { _, agendaOverviewItem -> agendaOverviewItem.id },
                ) { index, agendaOverviewItem ->
                    AgendaOverviewItem(
                        agendaOverviewItemUi = agendaOverviewItem,
                        onTickClick = {
                            onAction(AgendaOverviewAction.OnTickClick(agendaOverviewItem.id))
                        },
                        onOpenClick = { id ->
                            onAction(AgendaOverviewAction.OnOpenClick(id))
                        },
                        onEditClick = { id ->
                            onAction(AgendaOverviewAction.OnEditClick(id))
                        },
                        onDeleteClick = { id ->
                            onAction(AgendaOverviewAction.OnDeleteClick(id))
                        },
                        modifier = Modifier.padding(
                            vertical = 7.5.dp,
                        ),
                    )
                    if (index == 0) {
                        TimeMarker()
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AgendaOverviewScreenPreview() {
    TaskyTheme {
        AgendaOverviewScreen(
            state = AgendaOverviewState(
                userInitials = "AB",
                selectedDate = today(),
            ),
            onAction = {},
        )
    }
}
