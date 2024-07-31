@file:OptIn(ExperimentalMaterial3Api::class)

package com.pronoidsoftware.agenda.presentation.detail

import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.pronoidsoftware.agenda.domain.model.AgendaItemType
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.agenda.presentation.detail.components.AgendaDetailActionText
import com.pronoidsoftware.agenda.presentation.detail.components.AgendaDetailDescription
import com.pronoidsoftware.agenda.presentation.detail.components.AgendaDetailNotification
import com.pronoidsoftware.agenda.presentation.detail.components.AgendaDetailTime
import com.pronoidsoftware.agenda.presentation.detail.components.AgendaDetailTitle
import com.pronoidsoftware.agenda.presentation.detail.components.AgendaDetailToolbar
import com.pronoidsoftware.agenda.presentation.detail.components.AgendaDetailType
import com.pronoidsoftware.agenda.presentation.detail.components.edittext.AgendaDetailEditTextScreen
import com.pronoidsoftware.agenda.presentation.detail.components.edittext.EditTextType
import com.pronoidsoftware.core.presentation.designsystem.LocalClock
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.core.presentation.designsystem.TaskyWhite2
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyDialog
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyScaffold
import com.pronoidsoftware.core.presentation.ui.ObserveAsEvents
import com.pronoidsoftware.core.presentation.ui.formatFullDate
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Composable
fun AgendaDetailScreenRoot(
    type: AgendaItemType,
    isEditing: Boolean,
    onCloseClick: () -> Unit,
    viewModel: AgendaDetailViewModel = hiltViewModel(),
) {
    LaunchedEffect(isEditing) {
        if (isEditing) {
            viewModel.onAction(AgendaDetailAction.OnEnableEdit)
        } else {
            viewModel.onAction(AgendaDetailAction.OnDisableEdit)
        }
    }

    val context = LocalContext.current
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            AgendaDetailEvent.OnDeleted -> {
                Toast.makeText(
                    context,
                    R.string.deleted,
                    Toast.LENGTH_LONG,
                ).show()
                onCloseClick()
            }

            AgendaDetailEvent.OnSaved -> {
                Toast.makeText(
                    context,
                    R.string.saved,
                    Toast.LENGTH_LONG,
                ).show()
            }

            AgendaDetailEvent.OnClosed -> {
                onCloseClick()
            }
        }
    }

    AgendaDetailScreen(
        type = type,
        state = viewModel.state,
        onAction = viewModel::onAction,
    )
}

@Composable
internal fun AgendaDetailScreen(
    type: AgendaItemType,
    state: AgendaDetailState,
    onAction: (AgendaDetailAction) -> Unit,
) {
    val spacing = LocalSpacing.current
    val clock = LocalClock.current
    val dividerColor = TaskyWhite2

    if (state.isShowingCloseConfirmationDialog) {
        val onCancelAction = when {
            state.isEditingTitle -> AgendaDetailAction.OnCancelCloseTitle
            state.isEditingDescription -> AgendaDetailAction.OnCancelCloseDescription
            else -> AgendaDetailAction.OnCancelClose
        }
        val onConfirmAction = when {
            state.isEditingTitle -> AgendaDetailAction.OnConfirmCloseTitle
            state.isEditingDescription -> AgendaDetailAction.OnConfirmCloseDescription
            else -> AgendaDetailAction.OnConfirmClose
        }
        TaskyDialog(
            title = stringResource(id = R.string.close_dialog_title),
            description = stringResource(id = R.string.confirm_close),
            onCancel = { onAction(onCancelAction) },
            onConfirm = { onAction(onConfirmAction) },
        )
    }

    if (state.isEditingTitle) {
        AgendaDetailEditTextScreen(
            type = EditTextType.Title,
            value = state.title,
            onBackClick = {
                onAction(AgendaDetailAction.OnCloseTitle)
            },
            onSaveClick = { newTitle ->
                onAction(AgendaDetailAction.OnSaveTitle(newTitle))
            },
        )
    } else if (state.isEditingDescription) {
        AgendaDetailEditTextScreen(
            type = EditTextType.Description,
            value = state.description ?: "",
            onBackClick = {
                onAction(AgendaDetailAction.OnCloseDescription)
            },
            onSaveClick = { newDescription ->
                onAction(AgendaDetailAction.OnSaveDescription(newDescription))
            },
        )
    } else {
        BackHandler(enabled = state.isEditing && !state.isShowingCloseConfirmationDialog) {
            onAction(AgendaDetailAction.OnClose)
        }

        if (state.isShowingDeleteConfirmationDialog) {
            TaskyDialog(
                title = stringResource(id = R.string.delete_dialog_title),
                description = stringResource(id = R.string.confirm_deletion),
                onCancel = { onAction(AgendaDetailAction.OnCancelDelete) },
                onConfirm = { onAction(AgendaDetailAction.OnConfirmDelete) },
            )
        }
        TaskyScaffold(
            topAppBar = {
                AgendaDetailToolbar(
                    title = if (state.isEditing) {
                        stringResource(id = R.string.edit_reminder)
                    } else {
                        state.selectedDate.formatFullDate(clock).asString()
                    },
                    onCloseClick = {
                        if (state.isEditing) {
                            onAction(AgendaDetailAction.OnClose)
                        } else {
                            onAction(AgendaDetailAction.OnConfirmClose)
                        }
                    },
                    isEditing = state.isEditing,
                    onEditClick = { onAction(AgendaDetailAction.OnEnableEdit) },
                    onSaveClick = { onAction(AgendaDetailAction.OnSave) },
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
                    type = type,
                )
                Spacer(modifier = Modifier.height(spacing.agendaDetailSpaceMedium))
                AgendaDetailTitle(
                    title = state.title,
                    editEnabled = state.isEditing,
                    onEdit = {
                        onAction(AgendaDetailAction.OnEditTitle)
                    },
                )
                Spacer(modifier = Modifier.height(spacing.spaceSmallMedium))
                HorizontalDivider(color = dividerColor)
                Spacer(modifier = Modifier.height(spacing.spaceSmall))
                AgendaDetailDescription(
                    description = state.description,
                    editEnabled = state.isEditing,
                    onEdit = {
                        onAction(AgendaDetailAction.OnEditDescription)
                    },
                )
                Spacer(modifier = Modifier.height(spacing.spaceSmall))
                HorizontalDivider(color = dividerColor)
                Spacer(modifier = Modifier.height(spacing.agendaDetailSpaceMediumSmall))
                AgendaDetailTime(
                    timeDescription = stringResource(id = R.string.at),
                    localDateTime = state.fromTime,
                    editEnabled = state.isEditing,
                    onSelectTime = { time ->
                        onAction(AgendaDetailAction.OnSelectFromTime(time))
                    },
                    timePickerExpanded = state.isEditingFromTime,
                    toggleTimePickerExpanded = {
                        onAction(AgendaDetailAction.OnToggleFromTimePickerExpanded)
                    },
                    onSelectDate = { date ->
                        onAction(AgendaDetailAction.OnSelectFromDate(date))
                    },
                    datePickerExpanded = state.isEditingFromDate,
                    toggleDatePickerExpanded = {
                        onAction(AgendaDetailAction.OnToggleFromDatePickerExpanded)
                    },
                    clock = clock,
                )
                Spacer(modifier = Modifier.height(spacing.spaceMedium))
                HorizontalDivider(color = dividerColor)
                Spacer(modifier = Modifier.height(spacing.agendaDetailNotificationPaddingTop))
                AgendaDetailNotification(
                    notificationDescription = state.notificationDuration.text.asString(),
                    editEnabled = state.isEditing,
                    expanded = state.isEditingNotificationDuration,
                    toggleExpanded = {
                        onAction(AgendaDetailAction.OnToggleNotificationDurationExpanded)
                    },
                    onSelectNotificationDuration = { notificationDuration ->
                        onAction(
                            AgendaDetailAction.OnSelectNotificationDuration(notificationDuration),
                        )
                    },
                )
                Spacer(modifier = Modifier.height(spacing.spaceSmall))
                HorizontalDivider(color = dividerColor)
                Spacer(modifier = Modifier.weight(1f))
                HorizontalDivider(color = dividerColor)
                Spacer(modifier = Modifier.height(spacing.agendaDetailSpaceMediumSmall))
                AgendaDetailActionText(
                    enabled = true,
                    text = stringResource(id = R.string.delete_reminder),
                    onClick = {
                        onAction(AgendaDetailAction.OnDelete)
                    },
                )
                Spacer(modifier = Modifier.height(spacing.agendaDetailSpaceBottom))
            }
        }
    }
}

@Preview
@Composable
private fun ReminderDetailScreenPreview() {
    TaskyTheme {
        AgendaDetailScreen(
            type = AgendaItemType.REMINDER,
            state = AgendaDetailState(
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

@Preview
@Composable
private fun ReminderDetailScreenPreview_EditTitle() {
    TaskyTheme {
        AgendaDetailScreen(
            type = AgendaItemType.REMINDER,
            state = AgendaDetailState(
                title = "Project X",
                description = "Weekly plan\nRole distribution",
                selectedDate = LocalDate(2022, 3, 1),
                fromTime = LocalDateTime(2022, 7, 21, 8, 0),
                isEditing = true,
                isEditingTitle = true,
            ),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun ReminderDetailScreenPreview_EditDescription() {
    TaskyTheme {
        AgendaDetailScreen(
            type = AgendaItemType.REMINDER,
            state = AgendaDetailState(
                title = "Project X",
                description = "Amet minim mollit non deserunt ullamco " +
                    "est sit aliqua dolor do amet sint. ",
                selectedDate = LocalDate(2022, 3, 1),
                fromTime = LocalDateTime(2022, 7, 21, 8, 0),
                isEditing = true,
                isEditingDescription = true,
            ),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun ReminderDetailScreenPreview_DeleteDialog() {
    TaskyTheme {
        AgendaDetailScreen(
            type = AgendaItemType.REMINDER,
            state = AgendaDetailState(
                title = "Project X",
                description = "Amet minim mollit non deserunt ullamco " +
                    "est sit aliqua dolor do amet sint. ",
                selectedDate = LocalDate(2022, 3, 1),
                fromTime = LocalDateTime(2022, 7, 21, 8, 0),
                isEditing = true,
                isShowingDeleteConfirmationDialog = true,
            ),
            onAction = {},
        )
    }
}

@Preview
@Composable
private fun ReminderDetailScreenPreview_CloseDialog() {
    TaskyTheme {
        AgendaDetailScreen(
            type = AgendaItemType.REMINDER,
            state = AgendaDetailState(
                title = "Project X",
                description = "Amet minim mollit non deserunt ullamco " +
                    "est sit aliqua dolor do amet sint. ",
                selectedDate = LocalDate(2022, 3, 1),
                fromTime = LocalDateTime(2022, 7, 21, 8, 0),
                isEditing = true,
                isShowingCloseConfirmationDialog = true,
            ),
            onAction = {},
        )
    }
}
