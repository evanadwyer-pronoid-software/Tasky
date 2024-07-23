package com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.agenda.presentation.detail.components.event.visitor.model.VisitorFilterType
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.TaskyBlack
import com.pronoidsoftware.core.presentation.designsystem.TaskyDarkGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import com.pronoidsoftware.core.presentation.designsystem.TaskyWhite

@Composable
fun EventDetailVisitorFilter(
    onAllClick: () -> Unit,
    onGoingClick: () -> Unit,
    onNotGoingClick: () -> Unit,
    selectedFilterType: VisitorFilterType,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp
    val sizeHorizontal = (screenWidthDp - (spacing.spaceMedium * 4)) / 3

    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(spacing.spaceMedium),
    ) {
        VisitorFilterSelectionPill(
            text = stringResource(id = R.string.all),
            selected = selectedFilterType == VisitorFilterType.ALL,
            onClick = onAllClick,
            modifier = Modifier
                .width(sizeHorizontal),
        )
        VisitorFilterSelectionPill(
            text = stringResource(id = R.string.going),
            selected = selectedFilterType == VisitorFilterType.GOING,
            onClick = onGoingClick,
            modifier = Modifier
                .width(sizeHorizontal),
        )
        VisitorFilterSelectionPill(
            text = stringResource(id = R.string.not_going),
            selected = selectedFilterType == VisitorFilterType.NOT_GOING,
            onClick = onNotGoingClick,
            modifier = Modifier
                .width(sizeHorizontal),
        )
    }
}

@Composable
fun VisitorFilterSelectionPill(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .clip(RoundedCornerShape(spacing.visitorFilterPillCornerRadiusPercent))
            .background(
                if (selected) {
                    TaskyBlack
                } else {
                    TaskyLightGray
                },
            )
            .clickable {
                onClick()
            },
    ) {
        Text(
            text = text,
            style = TextStyle(
                fontFamily = Inter,
                fontWeight = FontWeight.W500,
                fontSize = 14.sp,
                lineHeight = 15.sp,
            ),
            color = if (selected) {
                TaskyWhite
            } else {
                TaskyDarkGray
            },
            modifier = Modifier
                .padding(vertical = spacing.visitorFilterPillVerticalPadding),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFF0000)
@Composable
private fun EventDetailVisitorFilterPreview_AllSelected() {
    TaskyTheme {
        EventDetailVisitorFilter(
            onAllClick = { },
            onGoingClick = { },
            onNotGoingClick = { },
            selectedFilterType = VisitorFilterType.ALL,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFF0000)
@Composable
private fun EventDetailVisitorFilterPreview_GoingSelected() {
    TaskyTheme {
        EventDetailVisitorFilter(
            onAllClick = { },
            onGoingClick = { },
            onNotGoingClick = { },
            selectedFilterType = VisitorFilterType.GOING,
            modifier = Modifier.padding(16.dp),
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFF0000)
@Composable
private fun EventDetailVisitorFilterPreview_NotGoingSelected() {
    TaskyTheme {
        EventDetailVisitorFilter(
            onAllClick = { },
            onGoingClick = { },
            onNotGoingClick = { },
            selectedFilterType = VisitorFilterType.NOT_GOING,
            modifier = Modifier.padding(16.dp),
        )
    }
}
