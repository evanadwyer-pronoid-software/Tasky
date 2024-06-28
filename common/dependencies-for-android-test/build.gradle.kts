plugins {
    alias(libs.plugins.tasky.android.library)
}

android {
    namespace = "com.pronoidsoftware.common.dependencies_for_android_test"
}

dependencies {
    api(projects.common.dependenciesForTest)
}
