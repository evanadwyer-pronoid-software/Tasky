plugins {
    alias(libs.plugins.tasky.android.library.compose)
    alias(libs.plugins.tasky.android.hilt.compose)
}

android {
    namespace = "com.pronoidsoftware.core.presentation.ui"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.presentation.designsystem)
}
