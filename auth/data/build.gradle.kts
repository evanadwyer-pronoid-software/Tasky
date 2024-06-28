plugins {
    alias(libs.plugins.tasky.android.library)
    alias(libs.plugins.tasky.jvm.ktor)
    alias(libs.plugins.tasky.android.hilt)
}

android {
    namespace = "com.pronoidsoftware.auth.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.auth.domain)
    testImplementation(projects.common.dependenciesForTest)
}
