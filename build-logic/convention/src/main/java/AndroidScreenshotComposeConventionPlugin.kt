import com.android.build.api.dsl.LibraryExtension
import com.pronoidsoftware.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidScreenshotComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.apply("com.android.compose.screenshot")

            extensions.configure<LibraryExtension> {
                defaultConfig {
                    experimentalProperties["android.experimental.enableScreenshotTest"] = true
                }
            }

            dependencies {
                "screenshotTestImplementation"(
                    libs.findLibrary("androidx.compose.ui.tooling").get(),
                )
            }
        }
    }
}