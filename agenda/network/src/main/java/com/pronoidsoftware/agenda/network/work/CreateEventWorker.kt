package com.pronoidsoftware.agenda.network.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.pronoidsoftware.agenda.network.dto.EventDto
import com.pronoidsoftware.core.data.networking.AgendaRoutes
import com.pronoidsoftware.core.data.networking.postMultipart
import com.pronoidsoftware.core.domain.DispatcherProvider
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import java.io.File
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@HiltWorker
class CreateEventWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val params: WorkerParameters,
    private val dispatchers: DispatcherProvider,
    private val httpClient: HttpClient,
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        val compressedPhotoUris =
            params.inputData.getStringArray("KEY_COMPRESSED_URIS_RESULT_PATHS")
        val compressionThresholdInBytes = params.inputData.getLong("KEY_COMPRESSION_THRESHOLD", 0L)
        val eventRequest =
            params.inputData.getString("KEY_CREATE_EVENT_REQUEST") ?: return Result.failure()
        var skippedPhotos = 0
        val remoteResult = withContext(dispatchers.io) {
            httpClient.postMultipart<EventDto>(
                route = AgendaRoutes.EVENT,
                body = MultiPartFormDataContent(
                    formData {
                        append(EVENT_CREATE_REQUEST, eventRequest)
                        var photoToUploadIndex = 0
                        compressedPhotoUris
                            ?.forEach { compressedPhotoUri ->
                                if (compressedPhotoUri.isBlank()) {
                                    skippedPhotos++
                                } else {
                                    val photoName = "photo$photoToUploadIndex"
                                    val photoBytes = File(compressedPhotoUri).readBytes()
                                    if (photoBytes.size > compressionThresholdInBytes) {
                                        skippedPhotos++
                                    } else {
                                        append(
                                            photoName,
                                            photoBytes,
                                            Headers.build {
                                                append(HttpHeaders.ContentType, "image/png")
                                                append(
                                                    HttpHeaders.ContentDisposition,
                                                    "filename=$photoName.png",
                                                )
                                            },
                                        )
                                        photoToUploadIndex++
                                    }
                                }
                            }
                    },
                ),
            )
        }
        return when (remoteResult) {
            is com.pronoidsoftware.core.domain.util.Result.Error -> {
                remoteResult.error.toWorkerResult()
            }

            is com.pronoidsoftware.core.domain.util.Result.Success -> {
                Result.success(
                    workDataOf(
                        SKIPPED_PHOTO_COUNT to skippedPhotos,
                        KEY_EVENT_DTO to Json.encodeToString(remoteResult.data),
                    ),
                )
            }
        }
    }

    companion object {
        const val EVENT_CREATE_REQUEST = "create_event_request"
        const val SKIPPED_PHOTO_COUNT = "SKIPPED_PHOTO_COUNT"
        const val KEY_EVENT_DTO = "KEY_EVENT_DTO"
    }
}
