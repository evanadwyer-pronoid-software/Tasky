package com.pronoidsoftware.core.data.auth

import android.content.SharedPreferences
import com.pronoidsoftware.core.domain.AuthInfo
import com.pronoidsoftware.core.domain.DispatcherProvider
import com.pronoidsoftware.core.domain.SessionStorage
import javax.inject.Inject
import kotlinx.coroutines.withContext
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class EncryptedSessionStorage @Inject constructor(
    private val sharedPreferences: SharedPreferences,
    private val dispatchers: DispatcherProvider,
) : SessionStorage {
    override suspend fun get(): AuthInfo? {
        return withContext(dispatchers.io) {
            val json = sharedPreferences.getString(KEY_AUTH_INFO, null)
            json?.let {
                Json.decodeFromString<AuthInfoSerializable>(it).toAuthInfo()
            }
        }
    }

    override suspend fun set(authInfo: AuthInfo?) {
        withContext(dispatchers.io) {
            if (authInfo == null) {
                sharedPreferences.edit().remove(KEY_AUTH_INFO).apply()
                return@withContext
            }

            val json = Json.encodeToString(authInfo.toAuthInfoSerializable())
            sharedPreferences.edit().putString(KEY_AUTH_INFO, json).apply()
        }
    }

    companion object {
        private const val KEY_AUTH_INFO = "KEY_AUTH_INFO"
    }
}
