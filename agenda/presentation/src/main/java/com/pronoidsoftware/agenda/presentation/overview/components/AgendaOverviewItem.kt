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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import com.pronoidsoftware.agenda.domain.AgendaItem
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.agenda.presentation.components.Tick
import com.pronoidsoftware.agenda.presentation.overview.model.AgendaOverviewItemUi
import com.pronoidsoftware.agenda.presentation.util.AgendaOverviewItemUiParameterProvider
import com.pronoidsoftware.core.presentation.designsystem.EllipsesIcon
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.core.presentation.designsystem.TaskyWhite
import com.pronoidsoftware.core.presentation.designsystem.components.TaskyDropdownMenu
import timber.log.Timber

@Composable
fun AgendaOverviewItem(
    agendaOverviewItemUi: AgendaOverviewItemUi,
    onOpenClick: (String) -> Unit,
    onEditClick: (String) -> Unit,
    onDeleteClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    onTickClick: (() -> Unit)? = null,
) {
    val spacing = LocalSpacing.current

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
            .background(agendaOverviewItemUi.backgroundColor)
            .padding(start = spacing.agendaItemPaddingHorizontal)
            .clickable {
                onOpenClick(agendaOverviewItemUi.id)
            },
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Tick(
                color = agendaOverviewItemUi.contentColor.tick,
                ticked = agendaOverviewItemUi.completed,
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
                        textDecoration = if (agendaOverviewItemUi.completed) {
                            TextDecoration.LineThrough
                        } else {
                            TextDecoration.None
                        },
                    ),
                ) {
                    append(agendaOverviewItemUi.title)
                }
            }
            Text(
                text = title,
                color = agendaOverviewItemUi.contentColor.title,
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
                            agendaOverviewItemUi.id,
                        )

                        1 -> onEditClick(
                            agendaOverviewItemUi.id,
                        )

                        2 -> onDeleteClick(
                            agendaOverviewItemUi.id,
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
                        tint = agendaOverviewItemUi.contentColor.menu,
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Spacer(modifier = Modifier.width(spacing.agendaItemDescriptionPaddingStart))
            Text(
                text = agendaOverviewItemUi.description,
                color = agendaOverviewItemUi.contentColor.description,
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
                text = if (agendaOverviewItemUi.toTime != null) {
                    "${agendaOverviewItemUi.fromTime} - ${agendaOverviewItemUi.toTime}"
                } else {
                    agendaOverviewItemUi.fromTime
                },
                color = agendaOverviewItemUi.contentColor.time,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(vertical = spacing.agendaItemTimePaddingVertical),
            )
            Spacer(modifier = Modifier.width(spacing.agendaItemDescriptionPaddingEnd))
        }
    }
}

@Preview
@Composable
private fun AgendaOverviewItemUiPreview(
    @PreviewParameter(AgendaOverviewItemUiParameterProvider::class) type: AgendaItem,
) {
    TaskyTheme {
        var completed by remember {
            mutableStateOf(false)
        }
        val spacing = LocalSpacing.current
        AgendaOverviewItem(
            agendaOverviewItemUi = AgendaOverviewItemUi(
                id = "",
                type = type,
                title = type.name,
                description = "Lorem ipsum dolor sit amet, consectetur adipi scing elit, " +
                    "sed do eiusmod tempor incididunt ut labore",
                fromTime = "Mar 5, 10:30",
                toTime = "Mar 5, 11:00",
                completed = completed,
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
