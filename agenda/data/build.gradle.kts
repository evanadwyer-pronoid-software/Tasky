plugins {
    alias(libs.plugins.tasky.android.library)
    alias(libs.plugins.tasky.android.hilt)
}

android {
    namespace = "com.pronoidsoftware.agenda.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.core.database)
    implementation(projects.agenda.domain)
}
