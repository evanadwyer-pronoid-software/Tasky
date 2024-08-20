package com.pronoidsoftware.core.data.agenda

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.pronoidsoftware.core.domain.DispatcherProvider
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
    override suspend fun doWork(): Result {
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
                            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, it)
                            outputBytes = it.toByteArray()
                            quality -= (quality * 0.1).roundToInt()
                        }
                    } while (outputBytes.size > compressionThresholdInBytes && quality > 5)

                    return@async try {
                        if (outputBytes.size > compressionThresholdInBytes) {
                            ""
                        } else {
                            val file = File.createTempFile(
                                "compressed-",
                                ".jpg",
                                appContext.cacheDir,
                            )
                            file.writeBytes(outputBytes)
                            file.absolutePath
                        }
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
                    KEY_COMPRESSED_URIS_RESULT_PATHS
                        to compressedPhotoFilePaths
                            .filterNot { it.isBlank() }
                            .toTypedArray(),
                    KEY_NUMBER_URIS_BEYOND_COMPRESSION
                        to compressedPhotoFilePaths
                            .count { it.isEmpty() },
                ),
            )
        }
    }

    companion object {
        const val KEY_URIS_TO_COMPRESS = "KEY_URIS_TO_COMPRESS"
        const val KEY_COMPRESSION_THRESHOLD = "KEY_COMPRESSION_THRESHOLD"
        const val KEY_COMPRESSED_URIS_RESULT_PATHS = "KEY_COMPRESSED_URIS_RESULT_PATHS"
        const val KEY_NUMBER_URIS_BEYOND_COMPRESSION = "KEY_NUMBER_URIS_BEYOND_COMPRESSION"
    }
}
