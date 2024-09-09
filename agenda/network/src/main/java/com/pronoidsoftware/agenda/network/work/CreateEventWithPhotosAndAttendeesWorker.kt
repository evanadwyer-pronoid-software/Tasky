package com.pronoidsoftware.agenda.network.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.pronoidsoftware.agenda.network.dto.EventDto
import com.pronoidsoftware.agenda.network.mappers.toEvent
import com.pronoidsoftware.core.data.networking.AgendaRoutes
import com.pronoidsoftware.core.data.networking.postMultipart
import com.pronoidsoftware.core.domain.DispatcherProvider
import com.pronoidsoftware.core.domain.SessionStorage
import com.pronoidsoftware.core.domain.agendaitem.LocalAgendaDataSource
import com.pronoidsoftware.core.domain.work.WorkKeys.CREATE_EVENT_REQUEST
import com.pronoidsoftware.core.domain.work.WorkKeys.KEY_COMPRESSED_URIS_RESULT_PATHS
import com.pronoidsoftware.core.domain.work.WorkKeys.KEY_NUMBER_URIS_BEYOND_COMPRESSION
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import java.io.File
import kotlinx.coroutines.withContext

@HiltWorker
class CreateEventWithPhotosAndAttendeesWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val params: WorkerParameters,
    private val dispatchers: DispatcherProvider,
    private val httpClient: HttpClient,
    private val localAgendaDataSource: LocalAgendaDataSource,
    private val sessionStorage: SessionStorage,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            return Result.failure()
        }

        val localUserId = sessionStorage.get()?.userId ?: return Result.failure()

        val compressedPhotoUris =
            params.inputData.getStringArray(KEY_COMPRESSED_URIS_RESULT_PATHS)
        val eventRequest =
            params.inputData.getString(CREATE_EVENT_REQUEST) ?: return Result.failure()
        val remoteResult = withContext(dispatchers.io) {
            httpClient.postMultipart<EventDto>(
                route = AgendaRoutes.EVENT,
                body = MultiPartFormDataContent(
                    formData {
                        append(CREATE_EVENT_REQUEST, eventRequest)
                        compressedPhotoUris?.forEachIndexed { index, compressedPhotoUri ->
                            val compressedPhotoFile = File(compressedPhotoUri)
                            val photoBytes = compressedPhotoFile.readBytes()
                            append(
                                "photo$index",
                                photoBytes,
                                Headers.build {
                                    append(HttpHeaders.ContentType, "image/jpg")
                                    append(
                                        HttpHeaders.ContentDisposition,
                                        "filename=photo$index.jpg",
                                    )
                                },
                            )
                            compressedPhotoFile.delete()
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
                localAgendaDataSource.upsertEvent(remoteResult.data.toEvent(localUserId))
                Result.success(
                    workDataOf(
                        KEY_NUMBER_URIS_BEYOND_COMPRESSION
                            to params.inputData.getInt(
                                KEY_NUMBER_URIS_BEYOND_COMPRESSION,
                                0,
                            ),
                    ),
                )
            }
        }
    }
}
