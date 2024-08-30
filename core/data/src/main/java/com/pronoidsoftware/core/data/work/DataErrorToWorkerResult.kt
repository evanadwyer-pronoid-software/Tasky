package com.pronoidsoftware.core.data.work

import com.pronoidsoftware.core.domain.util.DataError

fun DataError.toWorkerResult(): DataErrorWorkerResult {
    return when (this) {
        DataError.Local.DISK_FULL -> DataErrorWorkerResult.FAILURE
        DataError.Local.LOGGED_OUT -> DataErrorWorkerResult.FAILURE
        DataError.Network.REQUEST_TIMEOUT -> DataErrorWorkerResult.RETRY
        DataError.Network.UNAUTHORIZED -> DataErrorWorkerResult.RETRY
        DataError.Network.CONFLICT -> DataErrorWorkerResult.RETRY
        DataError.Network.TOO_MANY_REQUESTS -> DataErrorWorkerResult.RETRY
        DataError.Network.NO_INTERNET -> DataErrorWorkerResult.RETRY
        DataError.Network.PAYLOAD_TOO_LARGE -> DataErrorWorkerResult.FAILURE
        DataError.Network.SERVER_ERROR -> DataErrorWorkerResult.RETRY
        DataError.Network.SERIALIZATION -> DataErrorWorkerResult.FAILURE
        DataError.Network.UNKNOWN -> DataErrorWorkerResult.FAILURE
    }
}

enum class DataErrorWorkerResult {
    FAILURE,
    RETRY,
}
