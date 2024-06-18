package com.pronoidsoftware.tasky

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.pronoidsoftware.auth.presentation.register.RegisterScreenRoot
import com.pronoidsoftware.core.presentation.designsystem.TaskyTheme
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
        enableEdgeToEdge()
        setContent {
            TaskyTheme {
                RegisterScreenRoot(
                    onLogInClick = { Timber.wtf("trying to login...") },
                    onSuccessfulRegistration = { },
                )
            }
        }
    }
}
