@file:OptIn(ExperimentalMaterial3Api::class)

package com.pronoidsoftware.agenda.presentation.detail.reminder

import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.pronoidsoftware.agenda.domain.detail.model.EVENT
import com.pronoidsoftware.agenda.domain.detail.model.REMINDER
import com.pronoidsoftware.agenda.domain.detail.model.TASK
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
import com.pronoidsoftware.agenda.presentation.detail.components.event.photo.components.EventDetailPhotoDetail
import com.pronoidsoftware.agenda.presentation.detail.components.event.photo.components.EventDetailPhotos
import com.pronoidsoftware.agenda.presentation.detail.components.event.photo.model.PhotoId
import com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.components.AddVisitorDialog
import com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.components.EventDetailVisitorList
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
fun DetailScreenRoot(
    type: String,
    isEditing: Boolean,
    onCloseClick: () -> Unit,
    viewModel: DetailViewModel = hiltViewModel(),
) {
    LaunchedEffect(isEditing) {
        viewModel.onAction(DetailAction.OnEnableEdit)
    }

    val context = LocalContext.current
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            DetailEvent.OnDeleted -> {
                Toast.makeText(
                    context,
                    R.string.deleted,
                    Toast.LENGTH_LONG,
                ).show()
                onCloseClick()
            }

            DetailEvent.OnSaved -> {
                Toast.makeText(
                    context,
                    R.string.saved,
                    Toast.LENGTH_LONG,
                ).show()
            }

            DetailEvent.OnClosed -> {
                onCloseClick()
            }
        }
    }

    DetailScreen(
        type = type,
        state = viewModel.state,
        onAction = viewModel::onAction,
    )
}

@Composable
internal fun DetailScreen(type: String, state: DetailState, onAction: (DetailAction) -> Unit) {
    val spacing = LocalSpacing.current
    val clock = LocalClock.current
    val dividerColor = TaskyWhite2
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                onAction(DetailAction.OnAddPhotoClick(PhotoId.PhotoUri(uri)))
            }
        },
    )

    if (state.isShowingCloseConfirmationDialog) {
        val onCancelAction = when {
            state.isEditingTitle -> DetailAction.OnCancelCloseTitle
            state.isEditingDescription -> DetailAction.OnCancelCloseDescription
            else -> DetailAction.OnCancelClose
        }
        val onConfirmAction = when {
            state.isEditingTitle -> DetailAction.OnConfirmCloseTitle
            state.isEditingDescription -> DetailAction.OnConfirmCloseDescription
            else -> DetailAction.OnConfirmClose
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
                onAction(DetailAction.OnCloseTitle)
            },
            onSaveClick = { newTitle ->
                onAction(DetailAction.OnSaveTitle(newTitle))
            },
        )
    } else if (state.isEditingDescription) {
        AgendaDetailEditTextScreen(
            type = EditTextType.Description,
            value = state.description ?: "",
            onBackClick = {
                onAction(DetailAction.OnCloseDescription)
            },
            onSaveClick = { newDescription ->
                onAction(DetailAction.OnSaveDescription(newDescription))
            },
        )
    } else if (state.selectedPhoto != null) {
        EventDetailPhotoDetail(
            photo = state.selectedPhoto,
            editEnabled = state.isEditing,
            onCloseClick = { onAction(DetailAction.OnClosePhotoClick) },
            onDeleteClick = { onAction(DetailAction.OnDeletePhotoClick(state.selectedPhoto)) },
        )
    } else {
        BackHandler(enabled = state.isEditing && !state.isShowingCloseConfirmationDialog) {
            onAction(DetailAction.OnClose)
        }

        if (state.isShowingDeleteConfirmationDialog) {
            TaskyDialog(
                title = stringResource(id = R.string.delete_dialog_title),
                description = stringResource(id = R.string.confirm_deletion),
                onCancel = { onAction(DetailAction.OnCancelDelete) },
                onConfirm = { onAction(DetailAction.OnConfirmDelete) },
            )
        }
        if (state.isShowingAddVisitorDialog) {
            AddVisitorDialog(
                title = stringResource(id = R.string.add_visitor),
                buttonText = stringResource(id = R.string.add),
                onAddClick = { email ->
                    onAction(DetailAction.OnAddVisitorClick(email))
                },
                onCancel = { onAction(DetailAction.OnToggleAddVisitorDialog) },
                isAddingAttendee = state.isAddingVisitor,
                emailTextFieldState = state.visitorToAddEmail,
                isEmailValid = state.isVisitorToAddEmailValid,
                errorMessage = state.addVisitorErrorMessage,
            )
        }
        TaskyScaffold(
            topAppBar = {
                AgendaDetailToolbar(
                    title = if (state.isEditing) {
                        stringResource(id = R.string.edit_agenda_item, type.uppercase())
                    } else {
                        state.selectedDate.formatFullDate(clock).asString()
                    },
                    onCloseClick = {
                        if (state.isEditing) {
                            onAction(DetailAction.OnClose)
                        } else {
                            onAction(DetailAction.OnConfirmClose)
                        }
                    },
                    isEditing = state.isEditing,
                    onEditClick = { onAction(DetailAction.OnEnableEdit) },
                    onSaveClick = { onAction(DetailAction.OnSave) },
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
//                item {
//                    Spacer(modifier = Modifier.height(spacing.scaffoldPaddingTop))
                AgendaDetailType(
                    type = type,
//                        modifier = Modifier.padding(horizontal = spacing.spaceMedium),
                )
                Spacer(modifier = Modifier.height(spacing.agendaDetailSpaceMedium))
                AgendaDetailTitle(
                    title = state.title.ifEmpty {
                        stringResource(id = R.string.new_agenda_item, type)
                    },
                    editEnabled = state.isEditing,
                    onEdit = {
                        onAction(DetailAction.OnEditTitle)
                    },
                    isCompleted = type == TASK && state.completed,
//                        modifier = Modifier.padding(horizontal = spacing.spaceMedium),
                )
                Spacer(modifier = Modifier.height(spacing.spaceSmallMedium))
//                }
//                item {
                HorizontalDivider(
                    color = dividerColor,
//                        modifier = Modifier.padding(horizontal = spacing.spaceMedium),
                )
                Spacer(modifier = Modifier.height(spacing.spaceSmall))
                AgendaDetailDescription(
                    description = state.description,
                    editEnabled = state.isEditing,
                    onEdit = {
                        onAction(DetailAction.OnEditDescription)
                    },
//                        modifier = Modifier.padding(horizontal = spacing.spaceMedium),
                )
                Spacer(modifier = Modifier.height(spacing.spaceSmall))
                if (type == EVENT) {
//                    item {
                    EventDetailPhotos(
                        photos = state.photos,
                        arePhotosFull = state.photos.size >= 10,
                        editEnabled = state.isEditing,
                        onAddClick = {
//                            onAction(DetailAction.OnAddPhotoClick)
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly,
                                ),
                            )
                        },
                        onOpenClick = { photo ->
                            onAction(DetailAction.OnOpenPhotoClick(photo))
                        },
//                        onEditClick = { photo ->
//                            onAction(DetailAction.OnEditPhotoClick(photo))
//                        },
                        modifier = Modifier
                            .layout { measurable, constraints ->
                                val placeable = measurable.measure(
                                    constraints.copy(
                                        maxWidth = constraints.maxWidth + 32.dp.roundToPx(),
                                    ),
                                )
                                layout(placeable.width, placeable.height) {
                                    placeable.place(0, 0)
                                }
                            },
                    )
//                    }
                    Spacer(modifier = Modifier.height(spacing.scaffoldPaddingTop))
                }
                HorizontalDivider(
                    color = dividerColor,
//                        modifier = Modifier.padding(horizontal = spacing.spaceMedium),
                )
                Spacer(modifier = Modifier.height(spacing.agendaDetailSpaceMediumSmall))
                AgendaDetailTime(
                    timeDescription = stringResource(id = R.string.at),
                    localDateTime = state.fromTime,
                    editEnabled = state.isEditing,
                    onSelectTime = { time ->
                        onAction(DetailAction.OnSelectFromTime(time))
                    },
                    timePickerExpanded = state.isEditingFromTime,
                    toggleTimePickerExpanded = {
                        onAction(DetailAction.OnToggleFromTimePickerExpanded)
                    },
                    onSelectDate = { date ->
                        onAction(DetailAction.OnSelectFromDate(date))
                    },
                    datePickerExpanded = state.isEditingFromDate,
                    toggleDatePickerExpanded = {
                        onAction(DetailAction.OnToggleFromDatePickerExpanded)
                    },
                    clock = clock,
//                        modifier = Modifier.padding(horizontal = spacing.spaceMedium),
                )
                Spacer(modifier = Modifier.height(spacing.spaceMedium))
                HorizontalDivider(
                    color = dividerColor,
//                        modifier = Modifier.padding(horizontal = spacing.spaceMedium),
                )
                if (type == EVENT) {
                    Spacer(modifier = Modifier.height(spacing.agendaDetailSpaceMediumSmall))
                    AgendaDetailTime(
                        timeDescription = stringResource(id = R.string.to),
                        localDateTime = state.toTime,
                        editEnabled = state.isEditing,
                        onSelectTime = { time ->
                            onAction(DetailAction.OnSelectToTime(time))
                        },
                        timePickerExpanded = state.isEditingToTime,
                        toggleTimePickerExpanded = {
                            onAction(DetailAction.OnToggleToTimePickerExpanded)
                        },
                        onSelectDate = { date ->
                            onAction(DetailAction.OnSelectToDate(date))
                        },
                        datePickerExpanded = state.isEditingToDate,
                        toggleDatePickerExpanded = {
                            onAction(DetailAction.OnToggleToDatePickerExpanded)
                        },
                        clock = clock,
//                        modifier = Modifier.padding(horizontal = spacing.spaceMedium),
                    )
                    Spacer(modifier = Modifier.height(spacing.spaceMedium))
                    HorizontalDivider(
                        color = dividerColor,
//                        modifier = Modifier.padding(horizontal = spacing.spaceMedium),
                    )
                }
                Spacer(modifier = Modifier.height(spacing.agendaDetailNotificationPaddingTop))
                AgendaDetailNotification(
                    reminderDescription = state.notificationDuration.text.asString(),
                    editEnabled = state.isEditing,
                    expanded = state.isEditingNotificationDuration,
                    toggleExpanded = {
                        onAction(DetailAction.OnToggleNotificationDurationExpanded)
                    },
                    onSelectNotificationDuration = { notificationDuration ->
                        onAction(
                            DetailAction.OnSelectNotificationDuration(
                                notificationDuration,
                            ),
                        )
                    },
//                        modifier = Modifier.padding(horizontal = spacing.spaceMedium),
                )
                Spacer(modifier = Modifier.height(spacing.spaceSmall))
                HorizontalDivider(
                    color = dividerColor,
//                        modifier = Modifier.padding(horizontal = spacing.spaceMedium),
                )
//                }
                if (type == EVENT) {
                    Spacer(modifier = Modifier.height(spacing.scaffoldPaddingTop))
//                    val selectedFilterType = VisitorFilterType.ALL
//                    val goingVisitors = state.visitors.filter { it.isGoing }
//                    val notGoingVisitors = state.visitors.filterNot { it.isGoing }
//                    item {
                    EventDetailVisitorList(
                        onAllClick = { onAction(DetailAction.OnAllVisitorsClick) },
                        onGoingClick = { onAction(DetailAction.OnGoingVisitorsClick) },
                        onNotGoingClick = { onAction(DetailAction.OnNotGoingVisitorsClick) },
                        onAddVisitorClick = { onAction(DetailAction.OnToggleAddVisitorDialog) },
                        onDeleteVisitorClick = { visitor ->
                            onAction(DetailAction.OnDeleteVisitorClick(visitor))
                        },
                        selectedFilterType = state.selectedVisitorFilter,
                        goingVisitors = state.visitors.filter { it.isGoing },
                        notGoingVisitors = state.visitors.filterNot { it.isGoing },
                        editEnabled = state.isEditing,
//                            modifier = Modifier.padding(horizontal = spacing.spaceMedium),
                    )
//                        EventDetailAddVisitor(
//                            title = stringResource(id = R.string.visitors),
//                            editEnabled = state.isEditing,
//                            onAddVisitorClick = { },
//                            modifier = Modifier.padding(horizontal = spacing.spaceMedium),
//                        )
//                        Spacer(modifier = Modifier.height(spacing.visitorSectionTitlePaddingBottom))
//                        EventDetailVisitorFilter(
//                            onAllClick = { },
//                            onGoingClick = { },
//                            onNotGoingClick = { },
//                            selectedFilterType = selectedFilterType,
//                            modifier = Modifier.padding(horizontal = spacing.spaceMedium),
// //            modifier = Modifier.padding(bottom = spacing.visitorSectionFilterPaddingBottom)
//                        )
//                    Spacer(modifier = Modifier.height(spacing.visitorSectionFilterPaddingBottom))
//                    }
//                    val subSectionHeaderTextStyle = TextStyle(
//                        fontFamily = Inter,
//                        fontWeight = FontWeight.W500,
//                        fontSize = 16.sp,
//                        lineHeight = 15.sp,
//                        color = TaskyDarkGray,
//                    )
//                    when (selectedFilterType) {
//                        VisitorFilterType.ALL -> {
//                            if (goingVisitors.isNotEmpty()) {
// //                                item {
//                                    Text(
//                                        text = stringResource(id = R.string.going),
//                                        style = subSectionHeaderTextStyle,
//                                        modifier = Modifier.padding(horizontal = spacing.spaceMedium),
//                                    )
//                                    Spacer(modifier = Modifier.height(spacing.visitorSectionListSpacingLarge))
// //                                }
// //                                items(goingVisitors) { visitor ->
//                                goingVisitors.forEach { visitor ->
//                                    EventDetailVisitorDetail(
//                                        fullName = visitor.fullName,
//                                        isCreator = visitor.isCreator,
//                                        editEnabled = state.isEditing,
//                                        onDeleteClick = { },
//                                        modifier = Modifier.padding(horizontal = spacing.spaceMedium),
//                                    )
//                                }
//                            }
//                            if (notGoingVisitors.isNotEmpty()) {
// //                                item {
//                                    Spacer(
//                                        modifier = Modifier
//                                            .height(spacing.visitorSectionListSpacingExtraLarge),
//                                    )
//                                    Text(
//                                        text = stringResource(id = R.string.not_going),
//                                        style = subSectionHeaderTextStyle,
//                                        modifier = Modifier.padding(horizontal = spacing.spaceMedium),
//                                    )
//                                    Spacer(modifier = Modifier.height(spacing.visitorSectionListSpacingLarge))
// //                                }
// //                                items(notGoingVisitors) { visitor ->
//                                notGoingVisitors.forEach { visitor ->
//                                    EventDetailVisitorDetail(
//                                        fullName = visitor.fullName,
//                                        isCreator = visitor.isCreator,
//                                        editEnabled = state.isEditing,
//                                        onDeleteClick = { },
//                                        modifier = Modifier.padding(horizontal = spacing.spaceMedium),
//                                    )
//                                }
//                            }
//                        }
//
//                        VisitorFilterType.GOING -> {
// //                            items(goingVisitors) { visitor ->
//                            goingVisitors.forEach { visitor ->
//                                EventDetailVisitorDetail(
//                                    fullName = visitor.fullName,
//                                    isCreator = visitor.isCreator,
//                                    editEnabled = state.isEditing,
//                                    onDeleteClick = { },
//                                    modifier = Modifier.padding(horizontal = spacing.spaceMedium),
//                                    )
//                            }
//                        }
//
//                        VisitorFilterType.NOT_GOING -> {
// //                            items(notGoingVisitors) { visitor ->
//                            notGoingVisitors.forEach { visitor ->
//                                EventDetailVisitorDetail(
//                                    fullName = visitor.fullName,
//                                    isCreator = visitor.isCreator,
//                                    editEnabled = state.isEditing,
//                                    onDeleteClick = { },
//                                    modifier = Modifier.padding(horizontal = spacing.spaceMedium),
//
//                                    )
//                            }
//                        }
//                    }
                }
//                item {
                Spacer(modifier = Modifier.weight(1f))
                if (type == EVENT) {
                    Spacer(modifier = Modifier.height(spacing.agendaDetailSpaceBottom))
                } else {
                    HorizontalDivider(
                        color = dividerColor,
//                        modifier = Modifier.padding(horizontal = spacing.spaceMedium),
                    )
                    Spacer(modifier = Modifier.height(spacing.agendaDetailSpaceMediumSmall))
                }
                AgendaDetailActionText(
                    // TODO handle joining and leaving an event as an attendee
                    enabled = true,
                    text = stringResource(id = R.string.delete_agenda_item, type.uppercase()),
                    onClick = {
                        onAction(DetailAction.OnDelete)
                    },
//                        modifier = Modifier.padding(horizontal = spacing.spaceMedium),
                )
                Spacer(modifier = Modifier.height(spacing.agendaDetailSpaceBottom))
//                }
            }
        }
    }
}

@Preview
@Composable
private fun ReminderDetailScreenPreview() {
    TaskyTheme {
        DetailScreen(
            type = REMINDER,
            state = DetailState(
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
        DetailScreen(
            type = REMINDER,
            state = DetailState(
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
        DetailScreen(
            type = REMINDER,
            state = DetailState(
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
        DetailScreen(
            type = REMINDER,
            state = DetailState(
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
        DetailScreen(
            type = REMINDER,
            state = DetailState(
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
