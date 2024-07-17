@file:OptIn(ExperimentalMaterial3Api::class)

package com.pronoidsoftware.agenda.presentation.detail.reminder

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
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
import com.pronoidsoftware.agenda.presentation.detail.components.AgendaDetailActionText
import com.pronoidsoftware.agenda.presentation.detail.components.AgendaDetailDescription
import com.pronoidsoftware.agenda.presentation.detail.components.AgendaDetailNotification
import com.pronoidsoftware.agenda.presentation.detail.components.AgendaDetailTime
import com.pronoidsoftware.agenda.presentation.detail.components.AgendaDetailTitle
import com.pronoidsoftware.agenda.presentation.detail.components.AgendaDetailToolbar
import com.pronoidsoftware.agenda.presentation.detail.components.AgendaDetailType
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.TaskyGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.core.presentation.designsystem.TaskyWhite2
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyScaffold
import com.pronoidsoftware.core.presentation.ui.ObserveAsEvents
import com.pronoidsoftware.core.presentation.ui.formatForDetail
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Composable
fun ReminderDetailScreenRoot(
    onCloseClick: () -> Unit,
    viewModel: ReminderDetailViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            ReminderDetailEvent.OnDeleted -> {
                Toast.makeText(
                    context,
                    R.string.deleted,
                    Toast.LENGTH_LONG,
                ).show()
                onCloseClick()
            }

            ReminderDetailEvent.OnSaved -> {
                Toast.makeText(
                    context,
                    R.string.saved,
                    Toast.LENGTH_LONG,
                ).show()
            }
        }
    }

    ReminderDetailScreen(
        state = viewModel.state,
        onAction = { action ->
            when (action) {
                is ReminderDetailAction.OnClose -> onCloseClick()
                else -> viewModel.onAction(action)
            }
        },
    )
}

@Composable
internal fun ReminderDetailScreen(
    state: ReminderDetailState,
    onAction: (ReminderDetailAction) -> Unit,
) {
    val spacing = LocalSpacing.current
    val dividerColor = TaskyWhite2

    var notificationDropdownExpanded by remember {
        mutableStateOf(false)
    }
    val toggleNotificationDropdownExpanded = {
        notificationDropdownExpanded = !notificationDropdownExpanded
    }

    TaskyScaffold(
        topAppBar = {
            AgendaDetailToolbar(
                title = if (state.isEditing) {
                    stringResource(id = R.string.edit_reminder)
                } else {
                    state.selectedDate.formatForDetail().asString()
                },
                onXClick = { onAction(ReminderDetailAction.OnClose) },
                isEditing = state.isEditing,
                onEditClick = { onAction(ReminderDetailAction.OnEnableEdit) },
                onSaveClick = { onAction(ReminderDetailAction.OnSave) },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
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
                .padding(top = spacing.scaffoldPaddingTop)
                .padding(horizontal = spacing.spaceMedium),
        ) {
            AgendaDetailType(
                type = stringResource(id = R.string.reminder),
                fillColor = TaskyLightGray,
                borderColor = TaskyGray,
            )
            Spacer(modifier = Modifier.height(spacing.scaffoldPaddingTop - 12.dp))
            AgendaDetailTitle(
                title = state.title,
                isEditable = state.isEditing,
                onEdit = {
                    onAction(ReminderDetailAction.OnEditTitle)
                },
            )
            Spacer(modifier = Modifier.height(22.dp - 12.dp))
            HorizontalDivider(color = dividerColor)
            Spacer(modifier = Modifier.height(20.dp - 12.dp))
            AgendaDetailDescription(
                description = state.description ?: "",
                isEditable = state.isEditing,
                onEdit = {
                    onAction(ReminderDetailAction.OnEditDescription)
                },
            )
            Spacer(modifier = Modifier.height(20.dp - 12.dp))
            HorizontalDivider(color = dividerColor)
            Spacer(modifier = Modifier.height(27.dp - 12.dp))
            AgendaDetailTime(
                timeDesignation = "At",
//                time = state.fromTime.time.toString(),
//                date = state.fromTime.date.toRelativeDateTwoYear().asString(),
                localDateTime = state.fromTime,
                isEditable = state.isEditing,
//                onEditTime = {
//                    onAction(ReminderDetailAction.OnEditTime)
//                },
                onSelectTime = { time ->
                    onAction(ReminderDetailAction.OnSelectTime(time))
                },
//                onEditDate = {
//                    onAction(ReminderDetailAction.OnEditDate)
//                },
                onSelectDate = { date ->
                    onAction(ReminderDetailAction.OnSelectDate(date))
                },
            )
            Spacer(modifier = Modifier.height(28.dp - 12.dp))
            HorizontalDivider(color = dividerColor)
            Spacer(modifier = Modifier.height(23.dp - 12.dp))
//            TaskyDropdownMenu(
//                items = NotificationDuration.notificationDurationOptions()
//                    .map { it.text.asString() },
//                expanded = notificationDropdownExpanded,
//                toggleExpanded = toggleNotificationDropdownExpanded,
//                onMenuItemClick = { index ->
//                    toggleNotificationDropdownExpanded()
//                    onAction(
//                        ReminderDetailAction.OnSelectNotificationDuration(
//                            NotificationDuration.notificationDurationOptions()[index],
//                        ),
//                    )
//                },
//            ) {
            AgendaDetailNotification(
                reminderDescription = state.notificationDuration.text.asString(),
                isEditable = state.isEditing,
                expanded = notificationDropdownExpanded,
                toggleExpanded = toggleNotificationDropdownExpanded,
                onEdit = {
                    toggleNotificationDropdownExpanded()
                    onAction(ReminderDetailAction.OnEditNotification)
                },
                onSelectNotificationDuration = { notificationDuration ->
                    onAction(
                        ReminderDetailAction.OnSelectNotificationDuration(notificationDuration),
                    )
                },
            )
//            }
            Spacer(modifier = Modifier.height(20.dp - 12.dp))
            HorizontalDivider(color = dividerColor)
            Spacer(modifier = Modifier.weight(1f))
            HorizontalDivider(color = dividerColor)
            Spacer(modifier = Modifier.height(15.dp))
            AgendaDetailActionText(
                enabled = true,
                text = "DELETE REMINDER",
                onClick = {
                    onAction(ReminderDetailAction.OnDelete)
                },
            )
            Spacer(modifier = Modifier.height(35.dp))
        }
    }
}

@Preview
@Composable
private fun ReminderDetailScreenPreview() {
    TaskyTheme {
        ReminderDetailScreen(
            state = ReminderDetailState(
                title = "Project X",
                description = "Weekly plan\nRole distribution",
                selectedDate = LocalDate(2022, 3, 1),
                fromTime = LocalDateTime(2022, 7, 21, 8, 0),
                isEditing = false,
            ),
            onAction = {},
        )
    }
}
