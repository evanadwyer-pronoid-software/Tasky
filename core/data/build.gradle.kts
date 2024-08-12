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
    implementation(libs.kotlinx.datetime)
    implementation(libs.androidx.work)

    // Crypto
    implementation(libs.androidx.security.crypto.ktx)

    androidTestImplementation(projects.testUtil.androidTest)
}
