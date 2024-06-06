plugins {
    alias(libs.plugins.tasky.android.library)
    alias(libs.plugins.tasky.android.room)
}

android {
    namespace = "com.pronoidsoftware.core.database"
}

dependencies {
    implementation(projects.core.domain)
}
