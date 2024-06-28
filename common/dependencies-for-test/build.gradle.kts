plugins {
    alias(libs.plugins.tasky.jvm.library)
    alias(libs.plugins.tasky.jvm.ktor)
}

dependencies {
    implementation(projects.core.domain)

    implementation(libs.ktor.client.mock)
    implementation(platform(libs.junit.bom))
    implementation(libs.bundles.test)
    runtimeOnly(libs.junit.jupiter.engine)
    runtimeOnly(libs.junit.vintage.engine)
}
