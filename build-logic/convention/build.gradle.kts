plugins {
    `kotlin-dsl`
}

group = "com.pronoidsoftware.tasky.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.room.gradlePlugin)
    compileOnly(libs.hilt.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "tasky.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "tasky.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("androidLibrary") {
            id = "tasky.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("androidLibraryCompose") {
            id = "tasky.android.library.compose"
            implementationClass = "AndroidLibraryComposeConventionPlugin"
        }
        register("androidFeatureUi") {
            id = "tasky.android.feature.ui"
            implementationClass = "AndroidFeatureUiConventionPlugin"
        }
        register("androidRoom") {
            id = "tasky.android.room"
            implementationClass = "AndroidRoomConventionPlugin"
        }
        register("androidHilt") {
            id = "tasky.android.hilt"
            implementationClass = "AndroidHiltConventionPlugin"
        }
        register("androidHiltCompose") {
            id = "tasky.android.hilt.compose"
            implementationClass = "AndroidHiltComposeConventionPlugin"
        }
        register("androidJunit") {
            id = "tasky.android.junit"
            implementationClass = "AndroidJunitConventionPlugin"
        }
        register("jvmLibrary") {
            id = "tasky.jvm.library"
            implementationClass = "JvmLibraryConventionPlugin"
        }
        register("jvmKtor") {
            id = "tasky.jvm.ktor"
            implementationClass = "JvmKtorConventionPlugin"
        }
        register("jvmJunit") {
            id = "tasky.jvm.junit"
            implementationClass = "JvmJunitConventionPlugin"
        }
    }
}