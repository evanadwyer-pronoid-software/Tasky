
import com.android.build.api.dsl.LibraryExtension
import com.pronoidsoftware.convention.ExtensionType
import com.pronoidsoftware.convention.configureBuildTypes
import com.pronoidsoftware.convention.configureKotlinAndroid
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin

class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.android")
                apply("tasky.android.junit")
            }

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)
                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.LIBRARY
                )

                defaultConfig {
                    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                    consumerProguardFiles("consumer-rules.pro")
                    packaging {
                        resources {
                            excludes += "META-INF/LICENSE.md"
                            excludes += "META-INF/LICENSE-notice.md"
                        }
                    }
                }
            }

            dependencies {
                "testImplementation"(kotlin("test"))
            }
        }
    }
}