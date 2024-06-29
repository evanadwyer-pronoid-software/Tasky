package com.pronoidsoftware.auth.presentation

import android.content.SharedPreferences
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.pronoidsoftware.core.TaskyAndroidTest
import com.pronoidsoftware.core.data.auth.EncryptedSessionStorage
import com.pronoidsoftware.core.data.di.SessionStorageModule
import com.pronoidsoftware.core.domain.AuthInfo
import com.pronoidsoftware.core.domain.SessionStorage
import com.pronoidsoftware.tasky.MainActivity
import com.pronoidsoftware.testutil.jvmtest.TestConstants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test

@UninstallModules(SessionStorageModule::class)
@HiltAndroidTest
class AuthFeatureLoggedInTest : TaskyAndroidTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Module
    @InstallIn(SingletonComponent::class)
    object LoggedInSessionStorageModule {

        @Provides
        @Singleton
        fun provideLoggedInSessionStorage(sharedPreferences: SharedPreferences): SessionStorage {
            val sessionStorage = EncryptedSessionStorage(sharedPreferences)
            runBlocking {
                val authInfo = AuthInfo(
                    accessToken = TestConstants.ACCESS_TOKEN,
                    refreshToken = TestConstants.REFRESH_TOKEN,
                    userId = TestConstants.USER_ID,
                    fullName = TestConstants.FULL_NAME,
                )
                sessionStorage.set(authInfo)
            }
            return sessionStorage
        }
    }

    @Test
    fun testAuthFeature_alreadyLoggedIn() {
        composeRule.onNodeWithText("AgendaScreen").assertIsDisplayed()
    }
}
