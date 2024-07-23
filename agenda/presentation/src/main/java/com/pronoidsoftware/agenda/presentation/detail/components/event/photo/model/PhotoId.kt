package com.pronoidsoftware.agenda.presentation.detail.components.event.photo.model

import android.net.Uri
import androidx.annotation.DrawableRes

sealed interface PhotoId {
    data class PhotoUri(val uri: Uri) : PhotoId
    data class PhotoResId(@DrawableRes val resId: Int) : PhotoId
}
