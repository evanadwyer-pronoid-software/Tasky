plugins {
    alias(libs.plugins.tasky.android.feature.ui)
    alias(libs.plugins.tasky.android.hilt.compose)
}

android {
    namespace = "com.pronoidsoftware.agenda.presentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.agenda.domain)
    implementation(projects.auth.domain)

    implementation(libs.timber)
    implementation(libs.kotlinx.datetime)
    implementation(libs.coil)
    implementation(libs.androidx.compose.runtime.livedata)

    screenshotTestImplementation(projects.testUtil.jvmTest)
}
