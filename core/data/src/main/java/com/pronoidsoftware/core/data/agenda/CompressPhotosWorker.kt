package com.pronoidsoftware.core.data.agenda

import android.content.Context
import android.content.pm.ServiceInfo
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.pronoidsoftware.core.data.R
import com.pronoidsoftware.core.domain.DispatcherProvider
import com.pronoidsoftware.core.domain.notification.NotificationConstants
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import kotlin.math.roundToInt
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.withContext

@HiltWorker
class CompressPhotosWorker @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted private val params: WorkerParameters,
    private val dispatchers: DispatcherProvider,
) : CoroutineWorker(appContext, params) {

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return if (Build.VERSION.SDK_INT >= 34) {
            ForegroundInfo(
                id.hashCode(),
                createNotification(),
                ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE,
            )
        } else {
            ForegroundInfo(
                id.hashCode(),
                createNotification(),
            )
        }
    }

    override suspend fun doWork(): Result {
//        setForeground(getForegroundInfo())
        return withContext(dispatchers.io) {
            val stringUris = params.inputData.getStringArray(KEY_URIS_TO_COMPRESS)
            val compressionThresholdInBytes = params.inputData.getLong(
                KEY_COMPRESSION_THRESHOLD,
                0L,
            )
            val deferredCompressedFilePaths = mutableListOf<Deferred<String>>()

            stringUris?.forEach { uriToCompress ->
                val compressedPhotoUriString = async {
                    val uri = Uri.parse(uriToCompress)
                    val bytes = appContext.contentResolver.openInputStream(uri)?.use {
                        it.readBytes()
                    } ?: return@async ""
                    val bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
                    var outputBytes: ByteArray
                    var quality = 100
                    do {
                        val outputStream = ByteArrayOutputStream()
                        outputStream.use {
                            bitmap.compress(Bitmap.CompressFormat.PNG, quality, it)
                            outputBytes = it.toByteArray()
                            quality -= (quality * 0.1).roundToInt()
                        }
                    } while (outputBytes.size > compressionThresholdInBytes && quality > 5)

                    return@async try {
                        val file = File.createTempFile(
                            "compressed-",
                            ".png",
                            appContext.cacheDir,
                        )
                        file.writeBytes(outputBytes)
                        file.absolutePath
                    } catch (e: IOException) {
                        e.printStackTrace()
                        ""
                    }
                }
                deferredCompressedFilePaths.add(compressedPhotoUriString)
            }

            val compressedPhotoFilePaths = deferredCompressedFilePaths.awaitAll()

            Result.success(
                workDataOf(
                    KEY_COMPRESSED_URIS_RESULT_PATHS to compressedPhotoFilePaths.toTypedArray(),
                    KEY_COMPRESSION_THRESHOLD to compressionThresholdInBytes,
                ),
            )
        }
    }

    private fun createNotification() =
        NotificationCompat.Builder(appContext, NotificationConstants.UPLOAD_NOTIFICATION_CHANNEL_ID)
            .setContentTitle(applicationContext.getString(R.string.compressing_photos))
            .setSmallIcon(R.drawable.tasky_logo)
            .addAction(
                android.R.drawable.ic_delete,
                appContext.getString(R.string.cancel_compression),
                WorkManager.getInstance(appContext).createCancelPendingIntent(id),
            )
            .setOngoing(true)
            .build()

    companion object {
        const val KEY_URIS_TO_COMPRESS = "KEY_URIS_TO_COMPRESS"
        const val KEY_COMPRESSION_THRESHOLD = "KEY_COMPRESSION_THRESHOLD"
        const val KEY_COMPRESSED_URIS_RESULT_PATHS = "KEY_COMPRESSED_URIS_RESULT_PATHS"
    }
}
