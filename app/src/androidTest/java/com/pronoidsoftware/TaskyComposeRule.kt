package com.pronoidsoftware

import androidx.compose.ui.test.junit4.AndroidComposeTestRule
import androidx.test.ext.junit.rules.ActivityScenarioRule
import com.pronoidsoftware.tasky.MainActivity

typealias TaskyComposeRule =
    AndroidComposeTestRule<ActivityScenarioRule<MainActivity>, MainActivity>
