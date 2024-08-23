@file:OptIn(ExperimentalMaterial3Api::class)

package com.pronoidsoftware.agenda.presentation.overview

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.agenda.presentation.overview.components.AgendaOverviewDateWidget
import com.pronoidsoftware.agenda.presentation.overview.components.AgendaOverviewItem
import com.pronoidsoftware.agenda.presentation.overview.components.AgendaOverviewToolbar
import com.pronoidsoftware.agenda.presentation.overview.components.TimeMarker
import com.pronoidsoftware.agenda.presentation.overview.model.AgendaOverviewItemContents
import com.pronoidsoftware.agenda.presentation.overview.model.AgendaOverviewItemUi
import com.pronoidsoftware.core.domain.agendaitem.AgendaItemType
import com.pronoidsoftware.core.domain.util.today
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.designsystem.LocalClock
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.PlusIcon
import com.pronoidsoftware.core.presentation.designsystem.TaskyDarkGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyDialog
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyDropdownMenu
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyFloatingActionButton
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyScaffold
import com.pronoidsoftware.core.presentation.ui.ObserveAsEvents
import com.pronoidsoftware.core.presentation.ui.formatRelativeDate
import timber.log.Timber

@Composable
fun AgendaOverviewScreenRoot(
    onNavigateToAgendaItemDetailsScreen: (AgendaItemType, Boolean, String?) -> Unit,
    onLogoutClick: () -> Unit,
    viewModel: AgendaOverviewViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            AgendaOverviewEvent.OnLogout -> {
                onLogoutClick()
            }

            AgendaOverviewEvent.OnDelete -> {
                Toast.makeText(
                    context,
                    R.string.deleted,
                    Toast.LENGTH_LONG,
                ).show()
            }

            else -> {
                Timber.wtf("Unkown AgendaOverview event in screen")
            }
        }
    }

    AgendaOverviewScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is AgendaOverviewAction.OnCreateClick -> {
                    onNavigateToAgendaItemDetailsScreen(
                        action.type,
                        true,
                        null,
                    )
                }

                is AgendaOverviewAction.OnOpenClick -> {
                    onNavigateToAgendaItemDetailsScreen(
                        action.type,
                        false,
                        action.id,
                    )
                }

                is AgendaOverviewAction.OnEditClick -> {
                    onNavigateToAgendaItemDetailsScreen(
                        action.type,
                        true,
                        action.id,
                    )
                }

                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
internal fun AgendaOverviewScreen(
    state: AgendaOverviewState,
    onAction: (AgendaOverviewAction) -> Unit,
) {
    val spacing = LocalSpacing.current
    val clock = LocalClock.current

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
                items = AgendaItemType.entries.map {
                    when (it) {
                        AgendaItemType.EVENT -> stringResource(id = R.string.event)
                        AgendaItemType.TASK -> stringResource(id = R.string.task)
                        AgendaItemType.REMINDER -> stringResource(id = R.string.reminder)
                    }
                },
                expanded = state.fabDropdownMenuExpanded,
                toggleExpanded = {
                    onAction(AgendaOverviewAction.OnToggleFABDropdownMenuExpanded)
                },
                onMenuItemClick = { index ->
                    onAction(AgendaOverviewAction.OnCreateClick(AgendaItemType.entries[index]))
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
            if (state.isShowingDeleteConfirmationDialog) {
                TaskyDialog(
                    title = stringResource(id = R.string.delete_dialog_title),
                    description = stringResource(id = R.string.confirm_deletion),
                    onCancel = { onAction(AgendaOverviewAction.OnCancelDelete) },
                    onConfirm = { onAction(AgendaOverviewAction.OnConfirmDelete) },
                )
            }

            AgendaOverviewDateWidget(
                selectedDate = state.selectedDate,
                onSelectDate = {
                    onAction(AgendaOverviewAction.OnSelectDate(it))
                },
            )
            Spacer(modifier = Modifier.height(34.dp))
            if (state.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    CircularProgressIndicator()
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = spacing.spaceMedium),
                ) {
                    item {
                        Text(
                            text = state.selectedDate.formatRelativeDate(clock).asString(),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                        Spacer(modifier = Modifier.height(15.5.dp))
                    }

                    if (state.items.isEmpty()) {
                        item {
                            Text(
                                text = stringResource(id = R.string.empty_agenda),
                                style = TextStyle(
                                    fontFamily = Inter,
                                    fontWeight = FontWeight.W400,
                                    fontSize = 16.sp,
                                    lineHeight = 15.sp,
                                    color = TaskyDarkGray,
                                ),
                            )
                        }
                    }
                    items(
                        items = state.items,
                        key = { it.id },
                    ) { agendaOverviewItem ->
                        when (agendaOverviewItem) {
                            is AgendaOverviewItemUi.Item -> {
                                AgendaOverviewItem(
                                    agendaOverviewItemContents = agendaOverviewItem.item,
                                    onTickClick = {
                                        onAction(
                                            AgendaOverviewAction.OnTickClick(agendaOverviewItem.id),
                                        )
                                    },
                                    onOpenClick = { id ->
                                        onAction(
                                            AgendaOverviewAction.OnOpenClick(
                                                type = getAgendaItemType(agendaOverviewItem),
                                                id = id,
                                            ),
                                        )
                                    },
                                    onEditClick = { id ->
                                        onAction(
                                            AgendaOverviewAction.OnEditClick(
                                                type = getAgendaItemType(agendaOverviewItem),
                                                id = id,
                                            ),
                                        )
                                    },
                                    onDeleteClick = { id ->
                                        onAction(
                                            AgendaOverviewAction.OnDeleteClick(
                                                type = getAgendaItemType(agendaOverviewItem),
                                                id = id,
                                            ),
                                        )
                                    },
                                    modifier = Modifier.padding(
                                        vertical = 7.5.dp,
                                    ),
                                )
                            }

                            is AgendaOverviewItemUi.TimeMarker -> {
                                TimeMarker()
                            }
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
    }
}

private fun getAgendaItemType(agendaOverviewItem: AgendaOverviewItemUi.Item) =
    when (agendaOverviewItem.item) {
        is AgendaOverviewItemContents.EventOverviewUiContents -> AgendaItemType.EVENT
        is AgendaOverviewItemContents.ReminderOverviewUiContents -> AgendaItemType.REMINDER
        is AgendaOverviewItemContents.TaskOverviewUiContents -> AgendaItemType.TASK
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
