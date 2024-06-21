import com.android.build.api.dsl.ApplicationExtension
import com.pronoidsoftware.convention.ExtensionType
import com.pronoidsoftware.convention.configureBuildTypes
import com.pronoidsoftware.convention.configureKotlinAndroid
import com.pronoidsoftware.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("com.android.application")
                apply("org.jetbrains.kotlin.android")
                apply("de.mannodermaus.android-junit5")
                apply("tasky.android.junit")
            }

            extensions.configure<ApplicationExtension> {
                defaultConfig {
                    applicationId = libs.findVersion("projectApplicationId").get().toString()
                    targetSdk = libs.findVersion("projectTargetSdkVersion").get().toString().toInt()

                    versionCode = libs.findVersion("projectVersionCode").get().toString().toInt()
                    versionName = libs.findVersion("projectVersionName").get().toString()

                    testInstrumentationRunner = "com.pronoidsoftware.core.HiltTestRunner"

                    vectorDrawables {
                        useSupportLibrary = true
                    }

                    packaging {
                        resources {
                            excludes += "/META-INF/{AL2.0,LGPL2.1}"
                            excludes += "META-INF/LICENSE.md"
                            excludes += "META-INF/LICENSE-notice.md"
                        }
                    }
                }

                configureKotlinAndroid(this)

                configureBuildTypes(
                    commonExtension = this,
                    extensionType = ExtensionType.APPLICATION
                )
            }
        }
    }
}