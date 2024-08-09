package com.pronoidsoftware.agenda.presentation.detail.components.event.photo.model

import android.net.Uri
import androidx.annotation.DrawableRes
import com.pronoidsoftware.core.domain.agendaitem.PhotoId

sealed interface LocalPhotoId : PhotoId {
    data class LocalPhotoUri(
        val uri: Uri,
    ) : LocalPhotoId {
        override val stringId: String
            get() = uri.toString()
        override val intId: Int?
            get() = null
    }

    data class LocalPhotoResId(
        @DrawableRes val resId: Int,
    ) : LocalPhotoId {
        override val stringId: String?
            get() = null
        override val intId: Int
            get() = resId
    }
}
