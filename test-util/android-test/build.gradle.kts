plugins {
    alias(libs.plugins.tasky.android.library)
    alias(libs.plugins.tasky.android.hilt)
    alias(libs.plugins.tasky.jvm.ktor)
}

android {
    namespace = "com.pronoidsoftware.testutil.androidtest"
}

dependencies {
    implementation(projects.core.domain)
    implementation(projects.core.data)

    // Crypto
    implementation(libs.androidx.security.crypto.ktx)

    api(projects.testUtil.jvmTest)
    implementation(libs.hilt.android.testing)
}
