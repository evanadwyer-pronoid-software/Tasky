plugins {
    alias(libs.plugins.tasky.jvm.library)
    alias(libs.plugins.tasky.jvm.ktor)
}

dependencies {
    implementation(projects.core.domain)

    implementation(libs.ktor.client.mock)
    implementation(libs.kotlinx.datetime)
    implementation(libs.coroutines.test)
}
