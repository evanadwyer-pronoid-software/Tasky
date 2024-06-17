plugins {
    alias(libs.plugins.tasky.android.feature.ui)
    alias(libs.plugins.tasky.android.hilt.compose)
}

android {
    namespace = "com.pronoidsoftware.auth.presentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.auth.domain)

    implementation(libs.timber)
}
