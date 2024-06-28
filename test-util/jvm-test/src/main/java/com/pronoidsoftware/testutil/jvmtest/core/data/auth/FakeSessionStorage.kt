package com.pronoidsoftware.testutil.jvmtest.core.data.auth

import com.pronoidsoftware.core.domain.AuthInfo
import com.pronoidsoftware.core.domain.SessionStorage

class FakeSessionStorage : SessionStorage {
    private var authInfo: AuthInfo? = null

    override suspend fun get(): AuthInfo? {
        return this.authInfo
    }

    override suspend fun set(authInfo: AuthInfo?) {
        this.authInfo = authInfo
    }
}
