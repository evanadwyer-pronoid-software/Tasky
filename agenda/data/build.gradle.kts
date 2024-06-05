plugins {
    alias(libs.plugins.tasky.android.library)
}

android {
    namespace = "com.pronoidsoftware.agenda.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.database)
    implementation(projects.agenda.domain)
}
