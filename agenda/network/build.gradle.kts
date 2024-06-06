plugins {
    alias(libs.plugins.tasky.android.library)
    alias(libs.plugins.tasky.jvm.ktor)
}

android {
    namespace = "com.pronoidsoftware.agenda.network"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)
}
