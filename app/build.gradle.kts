import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
    alias(libs.plugins.tasky.android.application.compose)
    alias(libs.plugins.tasky.android.hilt.compose)
    alias(libs.plugins.tasky.jvm.ktor)
}

android {
    namespace = "com.pronoidsoftware.tasky"
}

ktlint {
    android = true
    ignoreFailures = false
    outputToConsole = true
    reporters {
        reporter(ReporterType.CHECKSTYLE)
        reporter(ReporterType.HTML)
    }
}

dependencies {

    implementation(libs.timber)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.chrisbanes.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    testImplementation(libs.junit4)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.chrisbanes.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    // Crypto
    implementation(libs.androidx.security.crypto.ktx)

    // SplashScreen
    implementation(libs.androidx.core.splashscreen)

    // Modules
    implementation(projects.core.presentation.designsystem)
    implementation(projects.core.presentation.ui)
    implementation(projects.core.domain)
    implementation(projects.core.data)
    implementation(projects.core.database)

    implementation(projects.auth.presentation)
    implementation(projects.auth.domain)
    implementation(projects.auth.data)

    implementation(projects.agenda.presentation)
    implementation(projects.agenda.domain)
    implementation(projects.agenda.data)
    implementation(projects.agenda.network)
}

tasks.register("testAllUnitTests") {
    group = "verification"
    description = "Runs all unit tests for debug variant."

    // core tests
    dependsOn(":core:domain:test")
    dependsOn(":core:data:testDebugUnitTest")
    dependsOn(":core:database:testDebugUnitTest")
    dependsOn(":core:presentation:designsystem:testDebugUnitTest")
    dependsOn(":core:presentation:ui:testDebugUnitTest")

    // auth tests
    dependsOn(":auth:domain:test")
    dependsOn(":auth:data:testDebugUnitTest")
    dependsOn(":auth:presentation:testDebugUnitTest")

    // agenda tests
    dependsOn(":agenda:domain:test")
    dependsOn(":agenda:data:testDebugUnitTest")
    dependsOn(":agenda:network:testDebugUnitTest")
    dependsOn(":agenda:presentation:testDebugUnitTest")
}
