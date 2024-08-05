plugins {
    alias(libs.plugins.tasky.android.library)
    alias(libs.plugins.tasky.android.room)
    alias(libs.plugins.tasky.android.hilt)
}

android {
    namespace = "com.pronoidsoftware.core.database"
}

dependencies {
    implementation(projects.core.domain)

    implementation(libs.kotlinx.datetime)
}
