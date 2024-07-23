package com.pronoidsoftware.agenda.presentation.detail.components.event.photo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.agenda.presentation.detail.components.event.photo.model.PhotoId
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.PlusIcon
import com.pronoidsoftware.core.presentation.designsystem.TaskyBlack
import com.pronoidsoftware.core.presentation.designsystem.TaskyGray
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme

@Composable
fun EventDetailPhotos(
    photos: List<PhotoId>,
    arePhotosFull: Boolean,
    editEnabled: Boolean,
    onAddClick: () -> Unit,
    onOpenClick: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (photos.isEmpty()) {
        NoPhotosCarousel(
            editEnabled = editEnabled,
            onAddClick = onAddClick,
            modifier = modifier,
        )
    } else {
        PhotoCarousel(
            photos = photos,
            arePhotosFull = arePhotosFull,
            editEnabled = editEnabled,
            onAddClick = onAddClick,
            onOpenClick = onOpenClick,
            onEditClick = onEditClick,
            modifier = modifier,
        )
    }
}

@Composable
private fun NoPhotosCarousel(
    editEnabled: Boolean,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(spacing.noPhotosRowHeight)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .then(
                if (editEnabled) {
                    Modifier.clickable {
                        onAddClick()
                    }
                } else {
                    Modifier
                },
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        val emptyTextStyle = TextStyle(
            fontFamily = Inter,
            fontWeight = FontWeight.W600,
            fontSize = 16.sp,
            lineHeight = 18.sp,
        )
        val emptyTextColor = TaskyGray
        if (editEnabled) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = PlusIcon,
                    contentDescription = null,
                    tint = emptyTextColor,
                )
                Spacer(modifier = Modifier.width(22.dp))
                Text(
                    text = stringResource(id = R.string.add_photos),
                    style = emptyTextStyle,
                    color = emptyTextColor,
                )
            }
        } else {
            Text(
                text = stringResource(id = R.string.no_photos),
                style = emptyTextStyle,
                color = emptyTextColor,
            )
        }
    }
}

@Composable
private fun PhotoCarousel(
    photos: List<PhotoId>,
    arePhotosFull: Boolean,
    editEnabled: Boolean,
    onAddClick: () -> Unit,
    onOpenClick: () -> Unit,
    onEditClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val spacing = LocalSpacing.current
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(spacing.photosRowHeight)
            .background(MaterialTheme.colorScheme.surfaceVariant),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start,
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    vertical = spacing.photosRowPaddingVertical,
                ),
        ) {
            Text(
                text = stringResource(id = R.string.photos),
                style = TextStyle(
                    fontFamily = Inter,
                    fontWeight = FontWeight.W600,
                    fontSize = 20.sp,
                    lineHeight = 18.sp,
                ),
                color = TaskyBlack,
                modifier = Modifier.padding(start = spacing.spaceMedium),
            )
            Spacer(modifier = Modifier.height(spacing.photosRowPaddingVertical))
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(spacing.spaceMediumSmall),
            ) {
                itemsIndexed(
                    items = photos,
                ) { index, photo ->
                    PhotoThumbnail(
                        photoId = photo,
                        onClick = {
                            if (editEnabled) {
                                onEditClick()
                            } else {
                                onOpenClick()
                            }
                        },
                        modifier = Modifier
                            .then(
                                if (index == 0) {
                                    Modifier.padding(start = spacing.spaceMedium)
                                } else if (index == photos.lastIndex && arePhotosFull) {
                                    Modifier.padding(end = spacing.spaceMedium)
                                } else {
                                    Modifier
                                },
                            ),
                    )
                }
                if (editEnabled && !arePhotosFull) {
                    item {
                        AddPhotoButton(
                            onClick = onAddClick,
                            modifier = Modifier
                                .padding(end = spacing.spaceMedium),
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun EventDetailPhotosPreview_Empty_NotEditable() {
    TaskyTheme {
        EventDetailPhotos(
            photos = emptyList(),
            arePhotosFull = false,
            editEnabled = false,
            onAddClick = { },
            onEditClick = { },
            onOpenClick = { },
        )
    }
}

@Preview
@Composable
private fun EventDetailPhotosPreview_Empty_Editable() {
    TaskyTheme {
        EventDetailPhotos(
            photos = emptyList(),
            arePhotosFull = false,
            editEnabled = true,
            onAddClick = { },
            onEditClick = { },
            onOpenClick = { },
        )
    }
}

@Preview
@Composable
private fun EventDetailPhotosPreview_NotEmpty_NotEditable() {
    TaskyTheme {
        val weddingPhoto = PhotoId.PhotoResId(R.drawable.test_wedding)
        val orangePhoto = PhotoId.PhotoResId(R.drawable.solid_orange)
        EventDetailPhotos(
            photos = listOf(weddingPhoto, orangePhoto),
            arePhotosFull = false,
            editEnabled = false,
            onAddClick = { },
            onEditClick = { },
            onOpenClick = { },
        )
    }
}

@Preview
@Composable
private fun EventDetailPhotosPreview_NotEmpty_Editable() {
    TaskyTheme {
        val weddingPhoto = PhotoId.PhotoResId(R.drawable.test_wedding)
        val orangePhoto = PhotoId.PhotoResId(R.drawable.solid_orange)
        EventDetailPhotos(
            photos = listOf(weddingPhoto, orangePhoto),
            arePhotosFull = false,
            editEnabled = true,
            onAddClick = { },
            onEditClick = { },
            onOpenClick = { },
        )
    }
}

@Preview
@Composable
private fun EventDetailPhotosPreview_NotEmpty_Editable_NearFull() {
    TaskyTheme {
        val weddingPhoto = PhotoId.PhotoResId(R.drawable.test_wedding)
        val orangePhoto = PhotoId.PhotoResId(R.drawable.solid_orange)
        EventDetailPhotos(
            photos = listOf(
                weddingPhoto,
                orangePhoto,
                weddingPhoto,
                orangePhoto,
                weddingPhoto,
                orangePhoto,
                weddingPhoto,
                orangePhoto,
                weddingPhoto,
                orangePhoto,
            ),
            arePhotosFull = false,
            editEnabled = true,
            onAddClick = { },
            onEditClick = { },
            onOpenClick = { },
        )
    }
}

@Preview
@Composable
private fun EventDetailPhotosPreview_NotEmpty_Editable_Full() {
    TaskyTheme {
        val weddingPhoto = PhotoId.PhotoResId(R.drawable.test_wedding)
        val orangePhoto = PhotoId.PhotoResId(R.drawable.solid_orange)
        EventDetailPhotos(
            photos = listOf(
                weddingPhoto,
                orangePhoto,
                weddingPhoto,
                orangePhoto,
                weddingPhoto,
                orangePhoto,
                weddingPhoto,
                orangePhoto,
                weddingPhoto,
            ),
            arePhotosFull = true,
            editEnabled = true,
            onAddClick = { },
            onEditClick = { },
            onOpenClick = { },
        )
    }
}
