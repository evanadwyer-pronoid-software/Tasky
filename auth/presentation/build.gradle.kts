plugins {
    alias(libs.plugins.tasky.android.feature.ui)
}

android {
    namespace = "com.pronoidsoftware.auth.presentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.auth.domain)
}
