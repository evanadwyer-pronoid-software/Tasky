package com.pronoidsoftware.agenda.presentation.detail.components.event.photo.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.core.domain.agendaitem.Photo
import com.pronoidsoftware.core.presentation.designsystem.PlusIcon
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightBlue2

@Composable
private fun PhotoBorder(
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
internal fun AddPhotoButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    addIcon: ImageVector = PlusIcon,
    iconColor: Color = TaskyLightBlue2,
) {
    PhotoBorder(
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
internal fun PhotoThumbnail(photo: Photo, onClick: () -> Unit, modifier: Modifier = Modifier) {
    PhotoBorder(
        modifier = modifier,
        onClick = onClick,
    ) {
        SubcomposeAsyncImage(
            model = when (photo) {
                is Photo.Local -> photo.localPhotoUri
                is Photo.Remote -> photo.url
            },
            loading = {
                CircularProgressIndicator()
            },
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize(),
            contentScale = ContentScale.Crop,
        )
    }
}
