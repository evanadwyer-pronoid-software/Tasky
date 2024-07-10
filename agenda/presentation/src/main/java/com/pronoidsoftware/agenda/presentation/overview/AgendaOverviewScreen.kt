@file:OptIn(ExperimentalMaterial3Api::class)

package com.pronoidsoftware.agenda.presentation.overview

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.agenda.presentation.components.AgendaOverviewDateWidget
import com.pronoidsoftware.agenda.presentation.components.AgendaOverviewItem
import com.pronoidsoftware.agenda.presentation.components.AgendaOverviewToolbar
import com.pronoidsoftware.agenda.presentation.components.TimeMarker
import com.pronoidsoftware.agenda.presentation.overview.model.AgendaOverviewItemUi
import com.pronoidsoftware.agenda.presentation.overview.model.AgendaOverviewListItem
import com.pronoidsoftware.core.domain.agenda.AgendaItem
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.PlusIcon
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyDropdownMenu
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyFloatingActionButton
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyScaffold
import com.pronoidsoftware.core.presentation.ui.ObserveAsEvents
import com.pronoidsoftware.core.presentation.ui.toRelativeDate
import java.util.Locale

@Composable
fun AgendaOverviewScreenRoot(viewModel: AgendaOverviewViewModel = hiltViewModel()) {
    val context = LocalContext.current
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is AgendaOverviewEvent.Open -> {
                Toast.makeText(
                    context,
                    "Opened ${event.id}",
                    Toast.LENGTH_LONG,
                ).show()
            }

            is AgendaOverviewEvent.Delete -> {
                Toast.makeText(
                    context,
                    "Deleted ${event.id}",
                    Toast.LENGTH_LONG,
                ).show()
            }

            is AgendaOverviewEvent.Edit -> {
                Toast.makeText(
                    context,
                    "Edited ${event.id}",
                    Toast.LENGTH_LONG,
                ).show()
            }

            AgendaOverviewEvent.Logout -> {
                Toast.makeText(
                    context,
                    "Logout",
                    Toast.LENGTH_LONG,
                ).show()
            }

            is AgendaOverviewEvent.Create -> {
                Toast.makeText(
                    context,
                    "Created ${event.type.name}",
                    Toast.LENGTH_LONG,
                ).show()
            }
        }
    }

    AgendaOverviewScreen(
        state = viewModel.state,
        onAction = viewModel::onAction,
    )
}

@Composable
internal fun AgendaOverviewScreen(
    state: AgendaOverviewState,
    onAction: (AgendaOverviewAction) -> Unit,
) {
    val spacing = LocalSpacing.current
    var createFABDropdownExpanded by remember {
        mutableStateOf(false)
    }
    val toggleFABCreateDropdownExpanded = {
        createFABDropdownExpanded = !createFABDropdownExpanded
    }
    TaskyScaffold(
        topAppBar = {
            AgendaOverviewToolbar(
                userInitials = state.userInitials,
                onAction = onAction,
                selectedDate = state.selectedDate,
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
                expanded = createFABDropdownExpanded,
                toggleExpanded = toggleFABCreateDropdownExpanded,
                onMenuItemClick = { index ->
                    onAction(AgendaOverviewAction.OnCreateClick(AgendaItem.entries[index]))
                },
            ) {
                TaskyFloatingActionButton(
                    icon = PlusIcon,
                    contentDescription = stringResource(id = R.string.create_agenda_item),
                    onClick = toggleFABCreateDropdownExpanded,
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
                        topStart = spacing.authContainerRadius,
                        topEnd = spacing.authContainerRadius,
                    ),
                )
                .background(
                    MaterialTheme.colorScheme.background,
                    shape = RoundedCornerShape(
                        topStart = spacing.authContainerRadius,
                        topEnd = spacing.authContainerRadius,
                    ),
                )
                .padding(top = spacing.authPaddingTop),
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
                        text = state.selectedDate.toRelativeDate().asString(),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                    )
                }

                items(
                    items = state.items,
                    key = { it.id },
                ) {
                    when (it) {
                        is AgendaOverviewListItem.TimeMarker -> {
                            TimeMarker()
                        }

                        is AgendaOverviewItemUi -> {
                            AgendaOverviewItem(
                                agendaOverviewItemUi = it,
                                onMenuActionClick = { agendaOverviewAction ->
                                    onAction(agendaOverviewAction)
                                },
                                onTickClick = {
                                    onAction(AgendaOverviewAction.OnTickClick(it.id))
                                },
                                modifier = Modifier.padding(
                                    vertical = 7.5.dp,
                                ),
                            )
                        }
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
            state = AgendaOverviewState(),
            onAction = {},
        )
    }
}
