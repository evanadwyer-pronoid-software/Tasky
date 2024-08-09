package com.pronoidsoftware.core.domain.agendaitem

import java.util.UUID

interface PhotoCompressor {
    fun compressPhoto(photoUri: String): UUID
}
