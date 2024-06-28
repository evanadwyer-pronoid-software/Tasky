plugins {
    alias(libs.plugins.tasky.android.library)
    alias(libs.plugins.tasky.jvm.ktor)
    alias(libs.plugins.tasky.android.hilt)
}

android {
    namespace = "com.pronoidsoftware.core.data"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.database)

    implementation(libs.timber)

    // Crypto
    implementation(libs.androidx.security.crypto.ktx)
}
