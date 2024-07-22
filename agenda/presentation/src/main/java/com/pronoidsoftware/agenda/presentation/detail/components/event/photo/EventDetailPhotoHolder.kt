package com.pronoidsoftware.agenda.presentation.detail.components.event.photo

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
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
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.core.presentation.designsystem.PlusIcon
import com.pronoidsoftware.core.presentation.designsystem.TaskyLightBlue2
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

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
    val density = LocalDensity.current
    val cornerRadiusPx = with(density) { cornerRadius.toPx() }
    Box(
        modifier = modifier
            .size(size + strokeWidth)
            .clip(RoundedCornerShape(cornerRadius))
            .clickable {
                onClick()
            },
        contentAlignment = Alignment.Center,
    ) {
        content()
        Canvas(
            modifier = Modifier.fillMaxSize(),
        ) {
            drawRoundRect(
                color = color,
                cornerRadius = CornerRadius(cornerRadiusPx, cornerRadiusPx),
                style = Stroke(
                    width = with(density) { strokeWidth.toPx() },
                ),
            )
        }
    }
}

@Composable
fun AddPhotoButton(
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
fun PhotoThumbnail(photoId: PhotoId, onClick: () -> Unit, modifier: Modifier = Modifier) {
    PhotoBorder(
        modifier = modifier,
        onClick = onClick,
    ) {
        when (photoId) {
            is PhotoId.PhotoUri -> {
                AsyncImage(
                    model = photoId.uri,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }

            is PhotoId.PhotoResId -> {
                Image(
                    painter = painterResource(id = photoId.resId),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
            }
        }
    }
}

@Preview
@Composable
private fun AddPhotoButtonPreview() {
    TaskyTheme {
        AddPhotoButton(
            onClick = { },
        )
    }
}

@Preview
@Composable
private fun PhotoThumbnailPreview() {
    TaskyTheme {
        PhotoThumbnail(
            photoId = PhotoId.PhotoResId(R.drawable.test_wedding),
            onClick = { },
        )
    }
}
