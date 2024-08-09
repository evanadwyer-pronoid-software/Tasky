@file:OptIn(ExperimentalMaterial3Api::class)

package com.pronoidsoftware.agenda.presentation.detail.components.event.photo.previews

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.core.presentation.designsystem.CloseIcon
import com.pronoidsoftware.core.presentation.designsystem.DeleteIcon
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

// For previews and screenshot testing
// previews don't handle URIs
@Composable
private fun EventDetailPhotoDetailFromResId(
    @DrawableRes resId: Int,
    editEnabled: Boolean,
    onCloseClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    val contentColor = MaterialTheme.colorScheme.onPrimaryContainer
    BackHandler {
        onCloseClick()
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors().copy(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(),
                title = {
                    Text(
                        text = stringResource(id = R.string.photo),
                        style = TextStyle(
                            fontFamily = Inter,
                            fontWeight = FontWeight.W600,
                            fontSize = 16.sp,
                            lineHeight = 12.sp,
                        ),
                        color = contentColor,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onCloseClick) {
                        Icon(
                            imageVector = CloseIcon,
                            contentDescription = stringResource(id = R.string.go_back),
                            tint = contentColor,
                        )
                    }
                },
                actions = {
                    IconButton(
                        enabled = editEnabled,
                        onClick = onDeleteClick,
                        modifier = Modifier.alpha(if (editEnabled) 1f else 0f),
                    ) {
                        Icon(
                            imageVector = DeleteIcon,
                            contentDescription = stringResource(id = R.string.delete),
                            tint = contentColor,
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(innerPadding)
                .padding(
                    horizontal = spacing.photoDetailPaddingHorizontal,
                    vertical = spacing.photoDetailPaddingVertical,
                ),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Image(
                painter = painterResource(id = resId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(spacing.photoDetailCornerRadius)),
                contentScale = ContentScale.Crop,
            )
        }
    }
}

@Preview
@Composable
private fun EventDetailPhotoDetailPreview() {
    TaskyTheme {
        EventDetailPhotoDetailFromResId(
            resId = R.drawable.test_wedding,
            editEnabled = true,
            onCloseClick = { },
            onDeleteClick = { },
        )
    }
}
