import com.pronoidsoftware.convention.configureKotlinJvm
import org.gradle.api.Plugin
import org.gradle.api.Project

class JvmLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            pluginManager.apply("org.jetbrains.kotlin.jvm")
            pluginManager.apply("tasky.junit")

            configureKotlinJvm()
        }
    }
}