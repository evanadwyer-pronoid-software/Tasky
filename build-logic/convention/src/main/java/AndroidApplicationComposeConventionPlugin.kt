import com.android.build.api.dsl.ApplicationExtension
import com.pronoidsoftware.convention.configureAndroidCompose
import com.pronoidsoftware.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.apply("tasky.android.application")
            pluginManager.apply("org.jetbrains.kotlin.plugin.serialization")

            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)
            dependencies {
                "implementation"(libs.findLibrary("navigation.compose").get())
                "implementation"(libs.findLibrary("kotlinx.serialization.json").get())
            }
        }
    }
}
