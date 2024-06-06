plugins {
    alias(libs.plugins.tasky.jvm.library)
}

dependencies {
    implementation(projects.core.domain)
}
