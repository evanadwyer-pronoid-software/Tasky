package com.pronoidsoftware.agenda.presentation.overview.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.agenda.presentation.components.Tick
import com.pronoidsoftware.agenda.presentation.overview.model.AgendaOverviewItemContents
import com.pronoidsoftware.agenda.presentation.util.AgendaItemTypeParameterProvider
import com.pronoidsoftware.core.domain.agendaitem.AgendaItemType
import com.pronoidsoftware.core.presentation.designsystem.EllipsesIcon
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.TaskyBlack
import com.pronoidsoftware.core.presentation.designsystem.TaskyBrown
import com.pronoidsoftware.core.presentation.designsystem.TaskyDarkGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyGreen
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightGreen
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.core.presentation.designsystem.TaskyWhite
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyDropdownMenu
import com.pronoidsoftware.core.presentation.ui.getTypeString
import timber.log.Timber

@Composable
fun AgendaOverviewItem(
    agendaOverviewItemContents: AgendaOverviewItemContents,
    onOpenClick: (String) -> Unit,
    onEditClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    onTickClick: (() -> Unit)? = null,
) {
    val spacing = LocalSpacing.current
    val completed =
        if (agendaOverviewItemContents is AgendaOverviewItemContents.TaskOverviewUiContents) {
            agendaOverviewItemContents.completed
        } else {
            false
        }
    val backgroundColor = getBackgroundColor(agendaOverviewItemContents)
    val contentColors = getContentColors(agendaOverviewItemContents)

    var dropdownExpanded by rememberSaveable {
        mutableStateOf(false)
    }
    val toggleDropdownExpanded = {
        dropdownExpanded = !dropdownExpanded
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .height(spacing.agendaItemHeight)
            .clip(RoundedCornerShape(spacing.agendaItemCornerRadius))
            .background(backgroundColor)
            .clickable {
                onOpenClick(agendaOverviewItemContents.id)
            }
            .padding(start = spacing.agendaItemPaddingHorizontal),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Tick(
                color = contentColors.tick,
                ticked = completed,
                radius = spacing.agendaItemTickRadius,
                strokeWidth = spacing.agendaItemTickStrokeWidth,
                onClick = onTickClick,
            )
            Spacer(
                modifier = Modifier.width(spacing.agendaItemTitlePaddingStart),
            )
            val title = buildAnnotatedString {
                withStyle(
                    style = MaterialTheme.typography.titleMedium.toSpanStyle().copy(
                        textDecoration = if (completed) {
                            TextDecoration.LineThrough
                        } else {
                            TextDecoration.None
                        },
                    ),
                ) {
                    append(
                        agendaOverviewItemContents.title.ifEmpty {
                            stringResource(
                                id = R.string.new_agenda_item,
                                getTypeString(agendaOverviewItemContents),
                            )
                        },
                    )
                }
            }
            Text(
                text = title,
                color = contentColors.title,
                style = MaterialTheme.typography.titleMedium,
            )
            Spacer(modifier = Modifier.weight(1f))
            TaskyDropdownMenu(
                items = listOf(
                    stringResource(id = R.string.open),
                    stringResource(id = R.string.edit),
                    stringResource(id = R.string.delete),
                ),
                expanded = dropdownExpanded,
                toggleExpanded = toggleDropdownExpanded,
                onMenuItemClick = { index ->
                    when (index) {
                        0 -> onOpenClick(
                            agendaOverviewItemContents.id,
                        )

                        1 -> onEditClick(
                            agendaOverviewItemContents.id,
                        )

                        2 -> onDeleteClick(
                            agendaOverviewItemContents.id,
                        )

                        else -> Timber.wtf("Unknown AgendaOverviewItem menu click")
                    }
                },
            ) {
                IconButton(
                    onClick = {
                        toggleDropdownExpanded()
                    },
                ) {
                    Icon(
                        imageVector = EllipsesIcon,
                        contentDescription = stringResource(id = R.string.more),
                        tint = contentColors.menu,
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Spacer(modifier = Modifier.width(spacing.agendaItemDescriptionPaddingStart))
            Text(
                text = agendaOverviewItemContents.description,
                color = contentColors.description,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.padding(end = spacing.agendaItemDescriptionPaddingEnd),
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
        ) {
            Text(
                text = if (
                    agendaOverviewItemContents is AgendaOverviewItemContents.EventOverviewUiContents
                ) {
                    "${agendaOverviewItemContents.startDateTime} " +
                        "- ${agendaOverviewItemContents.endDateTime}"
                } else {
                    agendaOverviewItemContents.startDateTime
                },
                color = contentColors.time,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = spacing.agendaItemTimePaddingVertical),
            )
            Spacer(modifier = Modifier.width(spacing.agendaItemDescriptionPaddingEnd))
        }
    }
}

private fun getContentColors(agendaOverviewItemContents: AgendaOverviewItemContents) =
    when (agendaOverviewItemContents) {
        is AgendaOverviewItemContents.EventOverviewUiContents -> {
            ContentColors(
                tick = TaskyBlack,
                title = TaskyBlack,
                menu = TaskyBrown,
                description = TaskyDarkGray,
                time = TaskyDarkGray,
            )
        }

        is AgendaOverviewItemContents.ReminderOverviewUiContents -> {
            ContentColors(
                tick = TaskyBlack,
                title = TaskyBlack,
                menu = TaskyBrown,
                description = TaskyDarkGray,
                time = TaskyDarkGray,
            )
        }

        is AgendaOverviewItemContents.TaskOverviewUiContents -> {
            ContentColors(
                tick = TaskyWhite,
                title = TaskyWhite,
                menu = TaskyWhite,
                description = TaskyWhite,
                time = TaskyWhite,
            )
        }
    }

private fun getBackgroundColor(agendaOverviewItemContents: AgendaOverviewItemContents) =
    when (agendaOverviewItemContents) {
        is AgendaOverviewItemContents.EventOverviewUiContents -> {
            TaskyLightGreen
        }

        is AgendaOverviewItemContents.ReminderOverviewUiContents -> {
            TaskyLightGray
        }

        is AgendaOverviewItemContents.TaskOverviewUiContents -> {
            TaskyGreen
        }
    }

private data class ContentColors(
    val tick: Color,
    val title: Color,
    val menu: Color,
    val description: Color,
    val time: Color,
)

@Composable
private fun getTypeString(agendaOverviewItemContents: AgendaOverviewItemContents): String {
    return getTypeString(
        type = when (agendaOverviewItemContents) {
            is AgendaOverviewItemContents.EventOverviewUiContents -> {
                AgendaItemType.EVENT
            }

            is AgendaOverviewItemContents.ReminderOverviewUiContents -> {
                AgendaItemType.REMINDER
            }

            is AgendaOverviewItemContents.TaskOverviewUiContents -> {
                AgendaItemType.TASK
            }
        },
    )
}

@Preview
@Composable
private fun AgendaOverviewItemUiPreview(
    @PreviewParameter(AgendaItemTypeParameterProvider::class) type: AgendaItemType,
) {
    TaskyTheme {
        var completed by remember {
            mutableStateOf(false)
        }
        val spacing = LocalSpacing.current
        when (type) {
            AgendaItemType.EVENT -> {
                AgendaOverviewItem(
                    agendaOverviewItemContents = AgendaOverviewItemContents.EventOverviewUiContents(
                        id = "",
                        title = type.name,
                        description = "Lorem ipsum dolor sit amet, consectetur adipi scing elit, " +
                            "sed do eiusmod tempor incididunt ut labore",
                        startDateTime = "Mar 5, 10:30",
                        endDateTime = "Mar 5, 11:00",
                    ),
                    onTickClick = {
                        completed = !completed
                    },
                    onOpenClick = { },
                    onEditClick = { },
                    onDeleteClick = { },
                    modifier = Modifier
                        .background(TaskyWhite)
                        .fillMaxWidth()
                        .padding(
                            start = spacing.overviewStartPadding,
                            end = spacing.overviewEndPadding,
                            top = 7.5.dp,
                            bottom = 7.5.dp,
                        ),
                )
            }

            AgendaItemType.TASK -> {
                AgendaOverviewItem(
                    agendaOverviewItemContents = AgendaOverviewItemContents.TaskOverviewUiContents(
                        id = "",
                        title = type.name,
                        description = "Lorem ipsum dolor sit amet, consectetur adipi scing elit, " +
                            "sed do eiusmod tempor incididunt ut labore",
                        startDateTime = "Mar 5, 10:30",
                        completed = true,
                    ),
                    onTickClick = {
                        completed = !completed
                    },
                    onOpenClick = { },
                    onEditClick = { },
                    onDeleteClick = { },
                    modifier = Modifier
                        .background(TaskyWhite)
                        .fillMaxWidth()
                        .padding(
                            start = spacing.overviewStartPadding,
                            end = spacing.overviewEndPadding,
                            top = 7.5.dp,
                            bottom = 7.5.dp,
                        ),
                )
            }

            AgendaItemType.REMINDER -> {
                AgendaOverviewItem(
                    agendaOverviewItemContents =
                    AgendaOverviewItemContents.ReminderOverviewUiContents(
                        id = "",
                        title = type.name,
                        description = "Lorem ipsum dolor sit amet, consectetur adipi scing elit, " +
                            "sed do eiusmod tempor incididunt ut labore",
                        startDateTime = "Mar 5, 10:30",
                    ),
                    onTickClick = {
                        completed = !completed
                    },
                    onOpenClick = { },
                    onEditClick = { },
                    onDeleteClick = { },
                    modifier = Modifier
                        .background(TaskyWhite)
                        .fillMaxWidth()
                        .padding(
                            start = spacing.overviewStartPadding,
                            end = spacing.overviewEndPadding,
                            top = 7.5.dp,
                            bottom = 7.5.dp,
                        ),
                )
            }
        }
    }
}
