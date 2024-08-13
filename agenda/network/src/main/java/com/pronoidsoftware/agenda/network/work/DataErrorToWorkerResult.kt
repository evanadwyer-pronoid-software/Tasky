package com.pronoidsoftware.agenda.network.work

import androidx.work.ListenableWorker
import androidx.work.workDataOf
import com.pronoidsoftware.core.domain.util.DataError

fun DataError.toWorkerResult(): ListenableWorker.Result {
    return when (this) {
        DataError.Local.DISK_FULL -> ListenableWorker.Result.failure()
        DataError.Network.REQUEST_TIMEOUT -> ListenableWorker.Result.retry()
        DataError.Network.UNAUTHORIZED -> ListenableWorker.Result.retry()
        DataError.Network.CONFLICT -> ListenableWorker.Result.retry()
        DataError.Network.TOO_MANY_REQUESTS -> ListenableWorker.Result.retry()
        DataError.Network.NO_INTERNET -> ListenableWorker.Result.retry()
        DataError.Network.PAYLOAD_TOO_LARGE -> ListenableWorker.Result.failure(
            workDataOf(
                "ERROR" to DataError.Network.PAYLOAD_TOO_LARGE.name,
            ),
        )

        DataError.Network.SERVER_ERROR -> ListenableWorker.Result.retry()
        DataError.Network.SERIALIZATION -> ListenableWorker.Result.failure(
            workDataOf(
                "ERROR" to DataError.Network.SERIALIZATION.name,
            ),
        )

        DataError.Network.UNKNOWN -> ListenableWorker.Result.failure(
            workDataOf(
                "ERROR" to DataError.Network.UNKNOWN.name,
            ),
        )
    }
}
