plugins {
    alias(libs.plugins.tasky.android.feature.ui)
    alias(libs.plugins.tasky.android.hilt.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.pronoidsoftware.agenda.presentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.agenda.domain)

    implementation(libs.timber)
    implementation(libs.kotlinx.datetime)
    implementation(libs.coil)
    implementation(libs.kotlinx.serialization.json)

    screenshotTestImplementation(projects.testUtil.jvmTest)
}
