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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.pronoidsoftware.agenda.presentation.R
import com.pronoidsoftware.core.domain.agendaitem.Photo
import com.pronoidsoftware.core.presentation.designsystem.Inter
import com.pronoidsoftware.core.presentation.designsystem.LocalSpacing
import com.pronoidsoftware.core.presentation.designsystem.PlusIcon
import com.pronoidsoftware.core.presentation.designsystem.TaskyBlack
import com.pronoidsoftware.core.presentation.designsystem.TaskyGray

@Composable
fun EventDetailPhotos(
    photos: List<Photo>,
    arePhotosFull: Boolean,
    editEnabled: Boolean,
    onAddClick: () -> Unit,
    onOpenClick: (Photo) -> Unit,
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
            .clickable(enabled = editEnabled) {
                onAddClick()
            },
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
    photos: List<Photo>,
    arePhotosFull: Boolean,
    editEnabled: Boolean,
    onAddClick: () -> Unit,
    onOpenClick: (Photo) -> Unit,
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
                        photo = photo,
                        onClick = {
                            onOpenClick(photo)
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
