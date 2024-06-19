import com.pronoidsoftware.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidHiltComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.apply("tasky.android.hilt")

            dependencies {
                "implementation"(libs.findLibrary("androidx.hilt.navigation.compose").get())
                "androidTestImplementation"(libs.findLibrary("androidx.test.runner").get())
            }
        }
    }
}