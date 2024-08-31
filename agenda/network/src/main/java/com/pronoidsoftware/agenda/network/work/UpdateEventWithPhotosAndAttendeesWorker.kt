package com.pronoidsoftware.agenda.network.work

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.pronoidsoftware.agenda.network.dto.EventDto
import com.pronoidsoftware.agenda.network.mappers.toEvent
import com.pronoidsoftware.agenda.network.request.UpdateEventRequest
import com.pronoidsoftware.core.data.networking.AgendaRoutes
import com.pronoidsoftware.core.data.networking.putMultipart
import com.pronoidsoftware.core.data.work.DataErrorWorkerResult
import com.pronoidsoftware.core.data.work.toWorkerResult
import com.pronoidsoftware.core.database.dao.AgendaPendingSyncDao
import com.pronoidsoftware.core.database.entity.sync.CreatedEventPendingSyncEntity
import com.pronoidsoftware.core.database.mappers.toEventEntity
import com.pronoidsoftware.core.domain.DispatcherProvider
import com.pronoidsoftware.core.domain.SessionStorage
import com.pronoidsoftware.core.domain.agendaitem.LocalAgendaDataSource
import com.pronoidsoftware.core.domain.work.SyncAgendaScheduler
import com.pronoidsoftware.core.domain.work.WorkKeys.KEY_COMPRESSED_URIS_RESULT_PATHS
import com.pronoidsoftware.core.domain.work.WorkKeys.KEY_NUMBER_URIS_BEYOND_COMPRESSION
import com.pronoidsoftware.core.domain.work.WorkKeys.UPDATE_EVENT_REQUEST
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import io.ktor.client.HttpClient
import io.ktor.client.request.forms.MultiPartFormDataContent
import io.ktor.client.request.forms.formData
import io.ktor.http.Headers
import io.ktor.http.HttpHeaders
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json

@HiltWorker
class UpdateEventWithPhotosAndAttendeesWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val params: WorkerParameters,
    private val dispatchers: DispatcherProvider,
    private val httpClient: HttpClient,
    private val localAgendaDataSource: LocalAgendaDataSource,
    private val sessionStorage: SessionStorage,
    private val applicationScope: CoroutineScope,
    private val syncAgendaScheduler: SyncAgendaScheduler,
    private val agendaPendingSyncDao: AgendaPendingSyncDao,
) : CoroutineWorker(appContext, params) {
    override suspend fun doWork(): Result {
        if (runAttemptCount >= 5) {
            applicationScope.launch {
                sessionStorage.get()?.userId?.let { localUserId ->
                    params.inputData.getString(UPDATE_EVENT_REQUEST)?.let { updateEventRequest ->
                        val event = Json.decodeFromString<UpdateEventRequest>(updateEventRequest)
                            .toEvent()
                        val isCreatePendingSync =
                            agendaPendingSyncDao.getCreatedEventPendingSyncEntity(event.id) != null
                        if (isCreatePendingSync) {
                            agendaPendingSyncDao.upsertCreatedEventPendingSyncEntity(
                                CreatedEventPendingSyncEntity(
                                    event = event.toEventEntity(),
                                    userId = localUserId,
                                ),
                            )
                        } else {
                            syncAgendaScheduler.scheduleSync(
                                type = SyncAgendaScheduler.SyncType.UpdateEvent(event = event),
                            )
                        }
                    }
                }
            }.join()
            return Result.failure()
        }

        val localUserId = sessionStorage.get()?.userId ?: return Result.failure()

        val compressedPhotoUris =
            params.inputData.getStringArray(KEY_COMPRESSED_URIS_RESULT_PATHS)
        val eventRequest =
            params.inputData.getString(UPDATE_EVENT_REQUEST) ?: return Result.failure()
        val remoteResult = withContext(dispatchers.io) {
            httpClient.putMultipart<EventDto>(
                route = AgendaRoutes.EVENT,
                body = MultiPartFormDataContent(
                    formData {
                        append(UPDATE_EVENT_REQUEST, eventRequest)
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
                when (remoteResult.error.toWorkerResult()) {
                    DataErrorWorkerResult.FAILURE -> {
                        applicationScope.launch {
                            sessionStorage.get()?.userId?.let { localUserId ->
                                params.inputData.getString(UPDATE_EVENT_REQUEST)
                                    ?.let { updateEventRequest ->
                                        val event = Json.decodeFromString<UpdateEventRequest>(
                                            updateEventRequest,
                                        )
                                            .toEvent()
                                        val isCreatePendingSync =
                                            agendaPendingSyncDao.getCreatedEventPendingSyncEntity(
                                                event.id,
                                            ) != null
                                        if (isCreatePendingSync) {
                                            agendaPendingSyncDao
                                                .upsertCreatedEventPendingSyncEntity(
                                                    CreatedEventPendingSyncEntity(
                                                        event = event.toEventEntity(),
                                                        userId = localUserId,
                                                    ),
                                                )
                                        } else {
                                            syncAgendaScheduler.scheduleSync(
                                                type = SyncAgendaScheduler.SyncType.UpdateEvent(
                                                    event = event,
                                                ),
                                            )
                                        }
                                    }
                            }
                        }.join()
                        Result.failure()
                    }

                    DataErrorWorkerResult.RETRY -> {
                        Result.retry()
                    }
                }
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
