package com.pronoidsoftware.auth.presentation

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.pronoidsoftware.core.TaskyAndroidTest
import com.pronoidsoftware.tasky.MainActivity
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class AuthFeatureLoggedInTest : TaskyAndroidTest() {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @Test
    fun testAuthFeature_alreadyLoggedIn() {
        composeRule.onNodeWithText("Today").assertIsDisplayed()
        composeRule.onNodeWithText("TE").assertIsDisplayed() // profile badge
    }
}
