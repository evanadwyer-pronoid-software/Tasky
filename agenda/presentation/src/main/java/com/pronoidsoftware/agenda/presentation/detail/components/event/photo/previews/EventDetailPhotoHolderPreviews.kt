package com.pronoidsoftware.agenda.presentation.detail.components.event.photo.previews

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.core.presentation.designsystem.PlusIcon
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightBlue2
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

// For previews and screenshot testing
// previews don't handle URIs
@Composable
private fun PhotoBorderFromResId(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    color: Color = TaskyLightBlue2,
    size: Dp = 60.dp,
    strokeWidth: Dp = 2.dp,
    cornerRadius: Dp = 5.dp,
    content: @Composable () -> Unit = { },
) {
    Box(
        modifier = modifier
            .size(size + strokeWidth)
            .clip(RoundedCornerShape(cornerRadius))
            .border(
                width = strokeWidth,
                color = color,
                shape = RoundedCornerShape(cornerRadius),
            )
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center,
    ) {
        content()
    }
}

@Composable
internal fun AddPhotoButtonFromResId(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    addIcon: ImageVector = PlusIcon,
    iconColor: Color = TaskyLightBlue2,
) {
    PhotoBorderFromResId(
        onClick = onClick,
        modifier = modifier,
    ) {
        Icon(
            imageVector = addIcon,
            contentDescription = stringResource(id = R.string.add_photos),
            tint = iconColor,
        )
    }
}

@Composable
internal fun PhotoThumbnailFromResId(
    @DrawableRes resId: Int,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    PhotoBorderFromResId(
        modifier = modifier,
        onClick = onClick,
    ) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}

@Preview
@Composable
private fun AddPhotoButtonPreview() {
    TaskyTheme {
        AddPhotoButtonFromResId(
            onClick = { },
        )
    }
}

@Preview
@Composable
private fun PhotoThumbnailPreview() {
    TaskyTheme {
        PhotoThumbnailFromResId(
            resId = R.drawable.test_wedding,
            onClick = { },
        )
    }
}
