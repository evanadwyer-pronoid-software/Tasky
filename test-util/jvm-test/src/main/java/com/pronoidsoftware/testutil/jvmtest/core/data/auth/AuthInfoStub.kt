package com.pronoidsoftware.testutil.jvmtest.core.data.auth

import com.pronoidsoftware.core.domain.AuthInfo
import com.pronoidsoftware.testutil.jvmtest.TestConstants

fun authInfoStub() = AuthInfo(
    accessToken = TestConstants.ACCESS_TOKEN,
    refreshToken = TestConstants.REFRESH_TOKEN,
    userId = TestConstants.USER_ID,
    fullName = TestConstants.FULL_NAME,
    email = TestConstants.VALID_EMAIL,
)
