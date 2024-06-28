import com.pronoidsoftware.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.testing.Test
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.withType

class JvmJunitConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        target.run {
            dependencies {
                "testImplementation"(platform(libs.findLibrary("junit.bom").get()))
                "testImplementation"(libs.findBundle("test").get())
                "testRuntimeOnly"(libs.findLibrary("junit.jupiter.engine").get())
                "testRuntimeOnly"(libs.findLibrary("junit.vintage.engine").get())
            }

            tasks.withType<Test> {
                useJUnitPlatform {
//                    exclude("**/AuthRepositoryImplementationTest.*")
                }
            }
        }
    }
}