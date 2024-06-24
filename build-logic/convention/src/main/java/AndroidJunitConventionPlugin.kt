import com.pronoidsoftware.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidJunitConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.run {
                apply("tasky.jvm.junit")
            }

            dependencies {
                "androidTestImplementation"(platform(libs.findLibrary("junit.bom").get()))
                "androidTestImplementation"(libs.findBundle("test").get())
                "androidTestImplementation"(libs.findLibrary("androidx.test.runner").get())
                "androidTestRuntimeOnly"(libs.findLibrary("junit.jupiter.engine").get())
                "androidTestRuntimeOnly"(libs.findLibrary("junit.vintage.engine").get())
            }
        }
    }
}