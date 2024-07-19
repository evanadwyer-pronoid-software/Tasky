@file:OptIn(ExperimentalMaterial3Api::class)

package com.pronoidsoftware.agenda.presentation.detail.components.edittext

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.core.presentation.designsystem.BackChevronIcon
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

@Composable
fun AgendaDetailEditTextScreen(
    type: EditTextType,
    value: String,
    onBackClick: () -> Unit,
    onSaveClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    val textFieldState = rememberTextFieldState(
        initialText = value,
    )
    val textStyle = TextStyle(
        fontFamily = Inter,
        fontWeight = FontWeight.W600,
        fontSize = 18.sp,
        lineHeight = 12.sp,
    )

    BackHandler {
        onBackClick()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                title = {
                    Text(
                        text = type.toolbarTitle.asString(),
                        style = textStyle,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = BackChevronIcon,
                            contentDescription = stringResource(id = R.string.go_back),
                        )
                    }
                },
                actions = {
                    TextButton(
                        onClick = {
                            onSaveClick(textFieldState.text.toString())
                        },
                    ) {
                        Text(
                            text = stringResource(id = R.string.save),
                            style = textStyle.copy(fontSize = 16.sp),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = spacing.editTextPaddingHorizontal),
        ) {
            HorizontalDivider(
                color = TaskyLightGray,
                thickness = spacing.strokeBold,
            )
            Spacer(modifier = Modifier.height(spacing.editTextPaddingTop))
            BasicTextField(
                state = textFieldState,
                modifier = Modifier
                    .fillMaxSize(),
                textStyle = type.bodyTextStyle,
            )
        }
    }
}

@Preview
@Composable
private fun AgendaDetailEditTextScreenPreview_Title() {
    TaskyTheme {
        AgendaDetailEditTextScreen(
            type = EditTextType.Title,
            value = "Project X",
            onBackClick = { },
            onSaveClick = { },
        )
    }
}

@Preview
@Composable
private fun AgendaDetailEditTextScreenPreview_Description() {
    TaskyTheme {
        AgendaDetailEditTextScreen(
            type = EditTextType.Description,
            value = "Amet minim mollit non deserunt ullamco est sit aliqua dolor do amet sint. ",
            onBackClick = { },
            onSaveClick = { },
        )
    }
}
