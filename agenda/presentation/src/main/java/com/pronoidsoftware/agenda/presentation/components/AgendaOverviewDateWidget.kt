package com.pronoidsoftware.agenda.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.TaskyDarkGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyOrange
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.core.presentation.ui.endOfMonth
import com.pronoidsoftware.core.presentation.ui.rangeTo
import com.pronoidsoftware.core.presentation.ui.startOfMonth
import com.pronoidsoftware.core.presentation.ui.today
import java.util.Locale
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate

@Composable
fun AgendaOverviewDateWidget(
    selectedDate: LocalDate,
    onSelectDate: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    val scrollIndex = selectedDate.dayOfMonth - 1
    val offsetPx = with(LocalDensity.current) { spacing.agendaDateWidgetOffset.roundToPx() }
    val rowState = rememberLazyListState(
        initialFirstVisibleItemIndex = scrollIndex,
        initialFirstVisibleItemScrollOffset = scrollIndex - offsetPx,
    )
    val coroutineScope = rememberCoroutineScope()
    LaunchedEffect(selectedDate) {
        rowState.animateScrollToItem(
            index = scrollIndex,
            scrollOffset = scrollIndex - offsetPx,
        )
    }
    val startDate = selectedDate.startOfMonth()
    val endDate = selectedDate.endOfMonth()
    val range = startDate.rangeTo(endDate).toList()
    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.agendaItemPaddingHorizontal),
        state = rowState,
    ) {
        items(
            items = range,
            key = { it.dayOfYear },
        ) { date ->
            DateDisplay(
                date = date,
                selected = date.dayOfMonth == selectedDate.dayOfMonth,
            ) { newSelectedDate ->
                if (newSelectedDate == selectedDate) {
                    coroutineScope.launch {
                        rowState.animateScrollToItem(
                            index = scrollIndex,
                            scrollOffset = scrollIndex - offsetPx,
                        )
                    }
                } else {
                    onSelectDate(newSelectedDate)
                }
            }
        }
    }
}

@Composable
fun DateDisplay(
    date: LocalDate,
    selected: Boolean,
    modifier: Modifier = Modifier,
    onclick: (LocalDate) -> Unit,
) {
    val spacing = LocalSpacing.current
    Column(
        modifier = modifier
            .height(spacing.agendaDateWidgetHeight)
            .width(spacing.agendaDateWidgetWidth)
            .clip(RoundedCornerShape(spacing.agendaDateWidgetCornerRadius))
            .background(
                if (selected) {
                    TaskyOrange
                } else {
                    Color.Transparent
                },
            )
            .clickable {
                onclick(date)
            },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
    ) {
        Text(
            text = date.dayOfWeek.name.first().uppercase(Locale.getDefault()),
            fontFamily = Inter,
            fontWeight = FontWeight.W700,
            fontSize = 11.sp,
            lineHeight = 13.2.sp,
            color = if (selected) TaskyDarkGray else TaskyGray,
        )
        Text(
            text = date.dayOfMonth.toString(),
            fontFamily = Inter,
            fontWeight = FontWeight.W700,
            fontSize = 17.sp,
            lineHeight = 20.4.sp,
            color = TaskyDarkGray,
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun AgendaOverDateWidgetPreview() {
    var selectedDate by remember {
        mutableStateOf(today())
    }
    TaskyTheme {
        AgendaOverviewDateWidget(
            selectedDate = selectedDate,
            onSelectDate = {
                selectedDate = it
            },
        )
    }
}
