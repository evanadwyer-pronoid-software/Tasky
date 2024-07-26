@file:OptIn(ExperimentalMaterial3Api::class)

package com.pronoidsoftware.agenda.presentation.detail.reminder

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
fun ReminderDetailScreenRoot(
    type: AgendaItemType,
    isEditing: Boolean,
    onCloseClick: () -> Unit,
    viewModel: ReminderDetailViewModel = hiltViewModel(),
) {
    LaunchedEffect(isEditing) {
        if (isEditing) {
            viewModel.onAction(ReminderDetailAction.OnEnableEdit)
        } else {
            viewModel.onAction(ReminderDetailAction.OnDisableEdit)
        }
    }

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

            ReminderDetailEvent.OnClosed -> {
                onCloseClick()
            }
        }
    }

    ReminderDetailScreen(
        type = type,
        state = viewModel.state,
        onAction = viewModel::onAction,
    )
}

@Composable
internal fun ReminderDetailScreen(
    type: AgendaItemType,
    state: ReminderDetailState,
    onAction: (ReminderDetailAction) -> Unit,
) {
    val spacing = LocalSpacing.current
    val clock = LocalClock.current
    val dividerColor = TaskyWhite2

    if (state.isShowingCloseConfirmationDialog) {
        val onCancelAction = when {
            state.isEditingTitle -> ReminderDetailAction.OnCancelCloseTitle
            state.isEditingDescription -> ReminderDetailAction.OnCancelCloseDescription
            else -> ReminderDetailAction.OnCancelClose
        }
        val onConfirmAction = when {
            state.isEditingTitle -> ReminderDetailAction.OnConfirmCloseTitle
            state.isEditingDescription -> ReminderDetailAction.OnConfirmCloseDescription
            else -> ReminderDetailAction.OnConfirmClose
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
                onAction(ReminderDetailAction.OnCloseTitle)
            },
            onSaveClick = { newTitle ->
                onAction(ReminderDetailAction.OnSaveTitle(newTitle))
            },
        )
    } else if (state.isEditingDescription) {
        AgendaDetailEditTextScreen(
            type = EditTextType.Description,
            value = state.description ?: "",
            onBackClick = {
                onAction(ReminderDetailAction.OnCloseDescription)
            },
            onSaveClick = { newDescription ->
                onAction(ReminderDetailAction.OnSaveDescription(newDescription))
            },
        )
    } else {
        BackHandler(enabled = state.isEditing && !state.isShowingCloseConfirmationDialog) {
            onAction(ReminderDetailAction.OnClose)
        }

        if (state.isShowingDeleteConfirmationDialog) {
            TaskyDialog(
                title = stringResource(id = R.string.delete_dialog_title),
                description = stringResource(id = R.string.confirm_deletion),
                onCancel = { onAction(ReminderDetailAction.OnCancelDelete) },
                onConfirm = { onAction(ReminderDetailAction.OnConfirmDelete) },
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
                            onAction(ReminderDetailAction.OnClose)
                        } else {
                            onAction(ReminderDetailAction.OnConfirmClose)
                        }
                    },
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
                    type = type,
                )
                Spacer(modifier = Modifier.height(spacing.agendaDetailSpaceMedium))
                AgendaDetailTitle(
                    title = state.title,
                    editEnabled = state.isEditing,
                    onEdit = {
                        onAction(ReminderDetailAction.OnEditTitle)
                    },
                )
                Spacer(modifier = Modifier.height(spacing.spaceSmallMedium))
                HorizontalDivider(color = dividerColor)
                Spacer(modifier = Modifier.height(spacing.spaceSmall))
                AgendaDetailDescription(
                    description = state.description,
                    editEnabled = state.isEditing,
                    onEdit = {
                        onAction(ReminderDetailAction.OnEditDescription)
                    },
                )
                Spacer(modifier = Modifier.height(spacing.spaceSmall))
                HorizontalDivider(color = dividerColor)
                Spacer(modifier = Modifier.height(spacing.agendaDetailSpaceMediumSmall))
                AgendaDetailTime(
                    timeDescription = stringResource(id = R.string.at),
                    localDateTime = state.atTime,
                    editEnabled = state.isEditing,
                    onSelectTime = { time ->
                        onAction(ReminderDetailAction.OnSelectTime(time))
                    },
                    timePickerExpanded = state.isEditingTime,
                    toggleTimePickerExpanded = {
                        onAction(ReminderDetailAction.OnToggleTimePickerExpanded)
                    },
                    onSelectDate = { date ->
                        onAction(ReminderDetailAction.OnSelectDate(date))
                    },
                    datePickerExpanded = state.isEditingDate,
                    toggleDatePickerExpanded = {
                        onAction(ReminderDetailAction.OnToggleDatePickerExpanded)
                    },
                    clock = clock,
                )
                Spacer(modifier = Modifier.height(spacing.spaceMedium))
                HorizontalDivider(color = dividerColor)
                Spacer(modifier = Modifier.height(spacing.agendaDetailNotificationPaddingTop))
                AgendaDetailNotification(
                    reminderDescription = state.notificationDuration.text.asString(),
                    editEnabled = state.isEditing,
                    expanded = state.isEditingNotificationDuration,
                    toggleExpanded = {
                        onAction(ReminderDetailAction.OnToggleNotificationDurationExpanded)
                    },
                    onSelectNotificationDuration = { notificationDuration ->
                        onAction(
                            ReminderDetailAction.OnSelectNotificationDuration(notificationDuration),
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
                        onAction(ReminderDetailAction.OnDelete)
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
        ReminderDetailScreen(
            type = AgendaItemType.REMINDER,
            state = ReminderDetailState(
                title = "Project X",
                description = "Weekly plan\nRole distribution",
                selectedDate = LocalDate(2022, 3, 1),
                atTime = LocalDateTime(2022, 7, 21, 8, 0),
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
        ReminderDetailScreen(
            type = AgendaItemType.REMINDER,
            state = ReminderDetailState(
                title = "Project X",
                description = "Weekly plan\nRole distribution",
                selectedDate = LocalDate(2022, 3, 1),
                atTime = LocalDateTime(2022, 7, 21, 8, 0),
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
        ReminderDetailScreen(
            type = AgendaItemType.REMINDER,
            state = ReminderDetailState(
                title = "Project X",
                description = "Amet minim mollit non deserunt ullamco " +
                    "est sit aliqua dolor do amet sint. ",
                selectedDate = LocalDate(2022, 3, 1),
                atTime = LocalDateTime(2022, 7, 21, 8, 0),
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
        ReminderDetailScreen(
            type = AgendaItemType.REMINDER,
            state = ReminderDetailState(
                title = "Project X",
                description = "Amet minim mollit non deserunt ullamco " +
                    "est sit aliqua dolor do amet sint. ",
                selectedDate = LocalDate(2022, 3, 1),
                atTime = LocalDateTime(2022, 7, 21, 8, 0),
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
        ReminderDetailScreen(
            type = AgendaItemType.REMINDER,
            state = ReminderDetailState(
                title = "Project X",
                description = "Amet minim mollit non deserunt ullamco " +
                    "est sit aliqua dolor do amet sint. ",
                selectedDate = LocalDate(2022, 3, 1),
                atTime = LocalDateTime(2022, 7, 21, 8, 0),
                isEditing = true,
                isShowingCloseConfirmationDialog = true,
            ),
            onAction = {},
        )
    }
}
