package com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.model.VisitorFilterType
import com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.model.VisitorUI
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.TaskyDarkGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

@Composable
fun EventDetailVisitorList(
    onAllClick: () -> Unit,
    onGoingClick: () -> Unit,
    onNotGoingClick: () -> Unit,
    onAddVisitorClick: () -> Unit,
    onDeleteVisitorClick: () -> Unit,
    selectedFilterType: VisitorFilterType,
    goingVisitors: List<VisitorUI>,
    notGoingVisitors: List<VisitorUI>,
    editEnabled: Boolean,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        EventDetailAddVisitor(
            title = stringResource(id = R.string.visitors),
            editEnabled = editEnabled,
            onAddVisitorClick = onAddVisitorClick,
        )
        Spacer(modifier = Modifier.height(spacing.visitorSectionTitlePaddingBottom))
        EventDetailVisitorFilter(
            onAllClick = onAllClick,
            onGoingClick = onGoingClick,
            onNotGoingClick = onNotGoingClick,
            selectedFilterType = selectedFilterType,
        )
        Spacer(modifier = Modifier.height(spacing.visitorSectionFilterPaddingBottom))
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(spacing.visitorSectionListSpacingSmall),
        ) {
            val subSectionHeaderTextStyle = TextStyle(
                fontFamily = Inter,
                fontWeight = FontWeight.W500,
                fontSize = 16.sp,
                lineHeight = 15.sp,
                color = TaskyDarkGray,
            )
            when (selectedFilterType) {
                VisitorFilterType.ALL -> {
                    item {
                        Text(
                            text = stringResource(id = R.string.going),
                            style = subSectionHeaderTextStyle,
                        )
                        Spacer(modifier = Modifier.height(spacing.visitorSectionListSpacingLarge))
                    }
                    items(goingVisitors) { visitor ->
                        EventDetailVisitorDetail(
                            fullName = visitor.fullName,
                            isCreator = visitor.isCreator,
                            editEnabled = editEnabled,
                            onDeleteClick = onDeleteVisitorClick,
                        )
                    }
                    item {
                        Spacer(
                            modifier = Modifier
                                .height(spacing.visitorSectionListSpacingExtraLarge),
                        )
                        Text(
                            text = stringResource(id = R.string.not_going),
                            style = subSectionHeaderTextStyle,
                        )
                        Spacer(modifier = Modifier.height(spacing.visitorSectionListSpacingLarge))
                    }
                    items(notGoingVisitors) { visitor ->
                        EventDetailVisitorDetail(
                            fullName = visitor.fullName,
                            isCreator = visitor.isCreator,
                            editEnabled = editEnabled,
                            onDeleteClick = onDeleteVisitorClick,
                        )
                    }
                }

                VisitorFilterType.GOING -> {
                    items(goingVisitors) { visitor ->
                        EventDetailVisitorDetail(
                            fullName = visitor.fullName,
                            isCreator = visitor.isCreator,
                            editEnabled = editEnabled,
                            onDeleteClick = onDeleteVisitorClick,
                        )
                    }
                }

                VisitorFilterType.NOT_GOING -> {
                    items(notGoingVisitors) { visitor ->
                        EventDetailVisitorDetail(
                            fullName = visitor.fullName,
                            isCreator = visitor.isCreator,
                            editEnabled = editEnabled,
                            onDeleteClick = onDeleteVisitorClick,
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EventDetailVisitorListPreview() {
    TaskyTheme {
        var selectedFilter by remember {
            mutableStateOf(VisitorFilterType.ALL)
        }
        EventDetailVisitorList(
            modifier = Modifier.padding(horizontal = 16.dp),
            onAllClick = { selectedFilter = VisitorFilterType.ALL },
            onGoingClick = { selectedFilter = VisitorFilterType.GOING },
            onNotGoingClick = { selectedFilter = VisitorFilterType.NOT_GOING },
            onAddVisitorClick = { },
            onDeleteVisitorClick = { },
            editEnabled = true,
            selectedFilterType = selectedFilter,
            goingVisitors = listOf(
                VisitorUI(
                    fullName = "Ann Allen",
                    isCreator = true,
                ),
                VisitorUI(fullName = "Wade Warren"),
                VisitorUI(fullName = "Esther Howard"),
            ),
            notGoingVisitors =
            listOf(
                VisitorUI(fullName = "Wade Warren"),
                VisitorUI(fullName = "Esther Howard"),
            ),
        )
    }
}
