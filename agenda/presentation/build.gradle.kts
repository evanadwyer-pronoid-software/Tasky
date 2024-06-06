plugins {
    alias(libs.plugins.tasky.android.feature.ui)
}

android {
    namespace = "com.pronoidsoftware.agenda.presentation"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.agenda.domain)
}
