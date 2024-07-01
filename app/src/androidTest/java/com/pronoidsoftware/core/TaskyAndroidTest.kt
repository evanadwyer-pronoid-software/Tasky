package com.pronoidsoftware.core

import android.content.Context
import android.content.SharedPreferences
import androidx.test.core.app.ApplicationProvider
import com.pronoidsoftware.core.domain.SessionStorage
import dagger.hilt.android.testing.HiltAndroidRule
import javax.inject.Inject
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Rule

abstract class TaskyAndroidTest {

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    protected lateinit var context: Context

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    @Inject
    lateinit var sessionStorage: SessionStorage

    @Before
    open fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        hiltRule.inject()
    }

    @After
    open fun tearDown() {
        runBlocking {
            sessionStorage.set(null)
            sharedPreferences.edit().clear().commit()
        }
    }
}
