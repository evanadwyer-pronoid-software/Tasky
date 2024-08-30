@file:OptIn(ExperimentalMaterial3Api::class)

package com.pronoidsoftware.agenda.presentation.detail

import android.Manifest
import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.work.WorkInfo
import androidx.work.WorkManager
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
import com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.components.AddVisitorDialog
import com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.components.EventDetailVisitorList
import com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.model.toVisitorUi
import com.pronoidsoftware.core.domain.ConnectivityObserver
import com.pronoidsoftware.core.domain.agendaitem.AgendaItemType
import com.pronoidsoftware.core.domain.agendaitem.Photo
import com.pronoidsoftware.core.domain.work.WorkKeys.KEY_NUMBER_URIS_BEYOND_COMPRESSION
import com.pronoidsoftware.core.presentation.designsystem.LocalClock
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.TaskyGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.core.presentation.designsystem.TaskyWhite2
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyDialog
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyScaffold
import com.pronoidsoftware.core.presentation.designsystem.util.ignoreColumnPadding
import com.pronoidsoftware.core.presentation.ui.ObserveAsEvents
import com.pronoidsoftware.core.presentation.ui.formatFullDate
import com.pronoidsoftware.core.presentation.ui.getTypeString
import com.pronoidsoftware.core.presentation.ui.hasNotificationPermission
import com.pronoidsoftware.core.presentation.ui.shouldShowNotificationPermissionRationale
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime

@Composable
fun AgendaDetailScreenRoot(
    onCloseClick: () -> Unit,
    viewModel: AgendaDetailViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    ObserveAsEvents(flow = viewModel.events) { event ->
        when (event) {
            is AgendaDetailEvent.OnError -> {
                Toast.makeText(
                    context,
                    event.error.asString(context),
                    Toast.LENGTH_LONG,
                ).show()
            }

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
        state = viewModel.state,
        connectionStatusFlow = viewModel.connectionStatus,
        onAction = viewModel::onAction,
    )
}

@Composable
internal fun AgendaDetailScreen(
    state: AgendaDetailState,
    connectionStatusFlow: StateFlow<ConnectivityObserver.Status>,
    onAction: (AgendaDetailAction) -> Unit,
) {
    val spacing = LocalSpacing.current
    val clock = LocalClock.current
    val dividerColor = TaskyWhite2
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri ->
            uri?.let {
                onAction(AgendaDetailAction.OnAddPhotoClick(Photo.Local(uri.toString())))
            }
        },
    )
    val context = LocalContext.current
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { isPermissionGranted ->
        val hasNotificationPermission = if (Build.VERSION.SDK_INT >= 33) {
            isPermissionGranted
        } else {
            true
        }
        val activity = context as ComponentActivity
        val showNotificationRationale = activity.shouldShowNotificationPermissionRationale()
        onAction(
            AgendaDetailAction.SubmitNotificationPermissionInfo(
                acceptedNotificationPermission = hasNotificationPermission,
                showNotificationRationale = showNotificationRationale,
            ),
        )
    }
    val connectionStatus by connectionStatusFlow.collectAsStateWithLifecycle()

    LaunchedEffect(true) {
        val activity = context as ComponentActivity
        val showNotificationRationale = activity.shouldShowNotificationPermissionRationale()
        onAction(
            AgendaDetailAction.SubmitNotificationPermissionInfo(
                acceptedNotificationPermission = context.hasNotificationPermission(),
                showNotificationRationale = showNotificationRationale,
            ),
        )
        if (!showNotificationRationale) {
            permissionLauncher.requestTaskyPermissions(context)
        }
    }

    val eventWorkResult = getDetailAsEvent(state)?.workId?.let { eventWorkId ->
        WorkManager.getInstance(context)
            .getWorkInfoByIdLiveData(eventWorkId)
            .observeAsState()
            .value
    }

    LaunchedEffect(eventWorkResult?.state) {
        if (eventWorkResult?.state == WorkInfo.State.SUCCEEDED) {
            val skippedPhotos = eventWorkResult.outputData.getInt(
                KEY_NUMBER_URIS_BEYOND_COMPRESSION,
                0,
            )
            if (skippedPhotos > 0) {
                Toast.makeText(
                    context,
                    context.getString(R.string.skipped_photos, skippedPhotos),
                    Toast.LENGTH_LONG,
                ).show()
            } else {
                Toast.makeText(
                    context,
                    context.getString(R.string.saved),
                    Toast.LENGTH_LONG,
                ).show()
            }
            onAction(AgendaDetailAction.LoadEvent)
        }
    }

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
    } else if (getDetailAsEvent(state)?.selectedPhotoToView != null) {
        getDetailAsEvent(state)?.selectedPhotoToView?.let { photo ->
            EventDetailPhotoDetail(
                photo = photo,
                editEnabled = state.isEditing &&
                    connectionStatus == ConnectivityObserver.Status.AVAILABLE &&
                    getDetailAsEvent(state)?.isUserEventCreator ?: false,
                onCloseClick = {
                    onAction(AgendaDetailAction.OnClosePhotoClick)
                },
                onDeleteClick = {
                    onAction(AgendaDetailAction.OnDeletePhotoClick(photo))
                },
            )
        }
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
        if (getDetailAsEvent(state)?.isShowingAddVisitorDialog == true) {
            getDetailAsEvent(state)?.let { eventDetails ->
                AddVisitorDialog(
                    title = stringResource(id = R.string.add_visitor),
                    buttonText = stringResource(id = R.string.add),
                    onAddClick = { email ->
                        onAction(AgendaDetailAction.OnAddVisitorClick(email))
                    },
                    onCancel = {
                        onAction(AgendaDetailAction.OnToggleAddVisitorDialog)
                    },
                    isAddingAttendee = eventDetails.isAddingVisitor,
                    emailTextFieldState = eventDetails.visitorToAddEmail,
                    isEmailValid = eventDetails.isVisitorToAddEmailValid,
                    errorMessage = eventDetails.addVisitorErrorMessage?.asString() ?: "",
                )
            }
        }
        if (state.showNotificationRationale) {
            TaskyDialog(
                title = stringResource(R.string.permission_required),
                description = stringResource(R.string.notification_rationale),
                onCancel = { /* Dismissal not allowed for permissions */ },
                onConfirm = {
                    onAction(AgendaDetailAction.DismissNotificationRationaleDialog)
                    permissionLauncher.requestTaskyPermissions(context)
                },
            )
        }
        TaskyScaffold(
            topAppBar = {
                AgendaDetailToolbar(
                    title = if (state.isEditing) {
                        stringResource(
                            id = R.string.edit_agenda_item,
                            getTypeString(type = state.agendaItemType, isUppercase = true),
                        )
                    } else {
                        stringResource(
                            id = R.string.today_is,
                            state.selectedDate.formatFullDate(clock),
                        )
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
                    canEnableEdit = eventWorkResult?.state?.isFinished ?: true,
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
                state.agendaItemType?.let { type ->
                    AgendaDetailType(
                        type = type,
                    )
                }
                Spacer(modifier = Modifier.height(spacing.agendaDetailSpaceMedium))
                AgendaDetailTitle(
                    title = state.title.ifEmpty {
                        stringResource(
                            id = R.string.new_agenda_item,
                            getTypeString(type = state.agendaItemType),
                        )
                    },
                    editEnabled = state.isEditing &&
                        getDetailAsEvent(state)?.isUserEventCreator ?: true,
                    onEdit = {
                        onAction(AgendaDetailAction.OnEditTitle)
                    },
                    isCompleted = getDetailAsTask(state)?.completed == true,
                )
                Spacer(modifier = Modifier.height(spacing.spaceSmallMedium))
                HorizontalDivider(color = dividerColor)
                Spacer(modifier = Modifier.height(spacing.spaceSmall))
                AgendaDetailDescription(
                    description = state.description,
                    editEnabled = state.isEditing &&
                        getDetailAsEvent(state)?.isUserEventCreator ?: true,
                    onEdit = {
                        onAction(AgendaDetailAction.OnEditDescription)
                    },
                )
                Spacer(modifier = Modifier.height(spacing.spaceSmall))
                getDetailAsEvent(state)?.let { eventDetails ->
                    EventDetailPhotos(
                        photos = eventDetails.photos,
                        arePhotosFull = eventDetails.arePhotosFull,
                        editEnabled = state.isEditing &&
                            connectionStatus == ConnectivityObserver.Status.AVAILABLE &&
                            getDetailAsEvent(state)?.isUserEventCreator ?: false,
                        onAddClick = {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly,
                                ),
                            )
                        },
                        onOpenClick = { photo ->
                            onAction(AgendaDetailAction.OnOpenPhotoClick(photo))
                        },
                        modifier = Modifier
                            .ignoreColumnPadding(spacing.spaceMedium),
                    )
                    Spacer(modifier = Modifier.height(spacing.scaffoldPaddingTop))
                }
                HorizontalDivider(color = dividerColor)
                Spacer(modifier = Modifier.height(spacing.agendaDetailSpaceMediumSmall))
                AgendaDetailTime(
                    timeDescription = if (state.agendaItemType == AgendaItemType.EVENT) {
                        stringResource(id = R.string.from)
                    } else {
                        stringResource(id = R.string.at)
                    },
                    localDateTime = state.startDateTime,
                    editEnabled = state.isEditing &&
                        getDetailAsEvent(state)?.isUserEventCreator ?: true,
                    onSelectTime = { time ->
                        onAction(AgendaDetailAction.OnSelectStartTime(time))
                    },
                    timePickerExpanded = state.isEditingStartTime,
                    toggleTimePickerExpanded = {
                        onAction(AgendaDetailAction.OnToggleStartTimePickerExpanded)
                    },
                    onSelectDate = { date ->
                        onAction(AgendaDetailAction.OnSelectStartDate(date))
                    },
                    datePickerExpanded = state.isEditingStartDate,
                    toggleDatePickerExpanded = {
                        onAction(AgendaDetailAction.OnToggleStartDatePickerExpanded)
                    },
                    clock = clock,
                )
                Spacer(modifier = Modifier.height(spacing.spaceMedium))
                HorizontalDivider(color = dividerColor)
                getDetailAsEvent(state)?.let { eventDetails ->
                    AgendaDetailTime(
                        timeDescription = stringResource(id = R.string.to),
                        localDateTime = eventDetails.endDateTime,
                        editEnabled = state.isEditing &&
                            getDetailAsEvent(state)?.isUserEventCreator ?: true,
                        onSelectTime = { time ->
                            onAction(AgendaDetailAction.OnSelectEndTime(time))
                        },
                        timePickerExpanded = eventDetails.isEditingEndTime,
                        toggleTimePickerExpanded = {
                            onAction(AgendaDetailAction.OnToggleEndTimePickerExpanded)
                        },
                        onSelectDate = { date ->
                            onAction(AgendaDetailAction.OnSelectEndDate(date))
                        },
                        datePickerExpanded = eventDetails.isEditingEndDate,
                        toggleDatePickerExpanded = {
                            onAction(AgendaDetailAction.OnToggleEndDatePickerExpanded)
                        },
                        clock = clock,
                    )
                    Spacer(modifier = Modifier.height(spacing.spaceMedium))
                    HorizontalDivider(color = dividerColor)
                }
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
                getDetailAsEvent(state)?.let { eventDetails ->
                    Spacer(modifier = Modifier.height(spacing.scaffoldPaddingTop))
                    EventDetailVisitorList(
                        onAllClick = {
                            onAction(AgendaDetailAction.OnAllVisitorsClick)
                        },
                        onGoingClick = {
                            onAction(AgendaDetailAction.OnGoingVisitorsClick)
                        },
                        onNotGoingClick = {
                            onAction(AgendaDetailAction.OnNotGoingVisitorsClick)
                        },
                        onAddVisitorClick = {
                            onAction(AgendaDetailAction.OnToggleAddVisitorDialog)
                        },
                        onDeleteVisitorClick = { visitor ->
                            onAction(AgendaDetailAction.OnDeleteVisitorClick(visitor))
                        },
                        selectedFilterType = eventDetails.selectedVisitorFilter,
                        goingVisitors = eventDetails.attendees
                            .filter { it.isGoing }
                            .map {
                                it.toVisitorUi(it.userId == eventDetails.host)
                            },
                        notGoingVisitors = eventDetails.attendees
                            .filterNot { it.isGoing }
                            .map {
                                it.toVisitorUi(false)
                            },
                        editEnabled = state.isEditing &&
                            connectionStatus == ConnectivityObserver.Status.AVAILABLE &&
                            getDetailAsEvent(state)?.isUserEventCreator ?: false,
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                if (state.agendaItemType == AgendaItemType.EVENT) {
                    Spacer(modifier = Modifier.height(spacing.agendaDetailSpaceBottom))
                } else {
                    HorizontalDivider(color = dividerColor)
                    Spacer(modifier = Modifier.height(spacing.agendaDetailSpaceMediumSmall))
                }
                when {
                    state.agendaItemType != AgendaItemType.EVENT -> {
                        AgendaDetailActionText(
                            enabled = true,
                            text = stringResource(
                                id = R.string.delete_agenda_item,
                                getTypeString(type = state.agendaItemType, isUppercase = true),
                            ),
                            onClick = {
                                onAction(AgendaDetailAction.OnDelete)
                            },
                        )
                    }

                    getDetailAsEvent(state)?.isUserEventCreator == true -> {
                        AgendaDetailActionText(
                            enabled = true,
                            text = stringResource(
                                id = R.string.delete_agenda_item,
                                getTypeString(type = state.agendaItemType, isUppercase = true),
                            ),
                            onClick = {
                                onAction(AgendaDetailAction.OnDelete)
                            },
                        )
                    }

                    getDetailAsEvent(state)?.isLocalUserGoing == true -> {
                        AgendaDetailActionText(
                            enabled = true,
                            text = stringResource(
                                id = R.string.leave_event,
                            ),
                            onClick = {
                                onAction(AgendaDetailAction.OnLeaveEvent)
                            },
                        )
                    }

                    else -> {
                        AgendaDetailActionText(
                            enabled = true,
                            text = stringResource(
                                id = R.string.join_event,
                            ),
                            onClick = {
                                onAction(AgendaDetailAction.OnJoinEvent)
                            },
                        )
                    }
                }
                Spacer(modifier = Modifier.height(spacing.agendaDetailSpaceBottom))
            }
        }

        if (state.isLoading || state.isSaving) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.3f)
                    .background(TaskyGray),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

private fun getDetailAsEvent(state: AgendaDetailState): AgendaItemDetails.Event? {
    return (state.typeSpecificDetails as? AgendaItemDetails.Event)
}

private fun getDetailAsTask(state: AgendaDetailState): AgendaItemDetails.Task? {
    return (state.typeSpecificDetails as? AgendaItemDetails.Task)
}

private fun getDetailAsReminder(state: AgendaDetailState): AgendaItemDetails.Reminder? {
    return (state.typeSpecificDetails as? AgendaItemDetails.Reminder)
}

private fun ActivityResultLauncher<String>.requestTaskyPermissions(context: Context) {
    val hasNotificationPermission = context.hasNotificationPermission()
    val notificationPermission = if (Build.VERSION.SDK_INT >= 33) {
        Manifest.permission.POST_NOTIFICATIONS
    } else {
        return
    }

    if (!hasNotificationPermission) {
        launch(notificationPermission)
    }
}

@Preview
@Composable
private fun ReminderDetailScreenPreview() {
    TaskyTheme {
        CompositionLocalProvider(LocalClock provides Clock.System) {
            AgendaDetailScreen(
                state = AgendaDetailState(
                    agendaItemType = AgendaItemType.REMINDER,
                    title = "Project X",
                    description = "Weekly plan\nRole distribution",
                    selectedDate = LocalDate(2022, 3, 1),
                    startDateTime = LocalDateTime(2022, 7, 21, 8, 0),
                    isEditing = false,
                ),
                connectionStatusFlow = MutableStateFlow(ConnectivityObserver.Status.AVAILABLE),
                onAction = {},
            )
        }
    }
}

@Preview
@Composable
private fun ReminderDetailScreenPreview_EditTitle() {
    TaskyTheme {
        CompositionLocalProvider(LocalClock provides Clock.System) {
            AgendaDetailScreen(
                state = AgendaDetailState(
                    agendaItemType = AgendaItemType.REMINDER,
                    title = "Project X",
                    description = "Weekly plan\nRole distribution",
                    selectedDate = LocalDate(2022, 3, 1),
                    startDateTime = LocalDateTime(2022, 7, 21, 8, 0),
                    isEditing = true,
                    isEditingTitle = true,
                ),
                connectionStatusFlow = MutableStateFlow(ConnectivityObserver.Status.AVAILABLE),
                onAction = {},
            )
        }
    }
}

@Preview
@Composable
private fun ReminderDetailScreenPreview_EditDescription() {
    TaskyTheme {
        CompositionLocalProvider(LocalClock provides Clock.System) {
            AgendaDetailScreen(
                state = AgendaDetailState(
                    agendaItemType = AgendaItemType.REMINDER,
                    title = "Project X",
                    description = "Amet minim mollit non deserunt ullamco " +
                        "est sit aliqua dolor do amet sint. ",
                    selectedDate = LocalDate(2022, 3, 1),
                    startDateTime = LocalDateTime(2022, 7, 21, 8, 0),
                    isEditing = true,
                    isEditingDescription = true,
                ),
                connectionStatusFlow = MutableStateFlow(ConnectivityObserver.Status.AVAILABLE),
                onAction = {},
            )
        }
    }
}

@Preview
@Composable
private fun ReminderDetailScreenPreview_DeleteDialog() {
    TaskyTheme {
        CompositionLocalProvider(LocalClock provides Clock.System) {
            AgendaDetailScreen(
                state = AgendaDetailState(
                    agendaItemType = AgendaItemType.REMINDER,
                    title = "Project X",
                    description = "Amet minim mollit non deserunt ullamco " +
                        "est sit aliqua dolor do amet sint. ",
                    selectedDate = LocalDate(2022, 3, 1),
                    startDateTime = LocalDateTime(2022, 7, 21, 8, 0),
                    isEditing = true,
                    isShowingDeleteConfirmationDialog = true,
                ),
                connectionStatusFlow = MutableStateFlow(ConnectivityObserver.Status.AVAILABLE),
                onAction = {},
            )
        }
    }
}

@Preview
@Composable
private fun ReminderDetailScreenPreview_CloseDialog() {
    TaskyTheme {
        CompositionLocalProvider(LocalClock provides Clock.System) {
            AgendaDetailScreen(
                state = AgendaDetailState(
                    agendaItemType = AgendaItemType.REMINDER,
                    title = "Project X",
                    description = "Amet minim mollit non deserunt ullamco " +
                        "est sit aliqua dolor do amet sint. ",
                    selectedDate = LocalDate(2022, 3, 1),
                    startDateTime = LocalDateTime(2022, 7, 21, 8, 0),
                    isEditing = true,
                    isShowingCloseConfirmationDialog = true,
                ),
                connectionStatusFlow = MutableStateFlow(ConnectivityObserver.Status.AVAILABLE),
                onAction = {},
            )
        }
    }
}
