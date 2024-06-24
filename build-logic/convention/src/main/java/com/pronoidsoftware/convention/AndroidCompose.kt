package com.pronoidsoftware.convention

import com.android.build.api.dsl.CommonExtension
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(commonExtension: CommonExtension<*, *, *, *, *, *>) {
    pluginManager.run {
        apply("org.jetbrains.kotlin.plugin.compose")
    }

    commonExtension.run {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = libs.findLibrary("chrisbanes.androidx.compose.bom").get()
            "implementation"(platform(bom))
            "implementation"(libs.findBundle("compose").get())
            "androidTestImplementation"(platform(bom))
            "debugImplementation"(libs.findLibrary("androidx.compose.ui.tooling.preview").get())
        }
    }
}
