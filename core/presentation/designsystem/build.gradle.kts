plugins {
    alias(libs.plugins.tasky.android.library.compose)
}

android {
    namespace = "com.pronoidsoftware.core.presentation.designsystem"
}

dependencies {

    implementation(projects.core.domain)

    implementation(libs.kotlinx.datetime)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    debugImplementation(libs.androidx.compose.ui.tooling)
    api(libs.androidx.compose.material3)
}
