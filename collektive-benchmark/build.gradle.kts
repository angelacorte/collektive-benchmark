import java.io.ByteArrayOutputStream
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.util.Locale

plugins {
    application
    alias(libs.plugins.taskTree) // Helps debugging dependencies among gradle tasks
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.collektive)
    scala
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.bundles.alchemist)
    implementation(libs.bundles.collektive)
    implementation(libs.bundles.protelis)
    implementation(libs.scala)
}

// Heap size estimation for batches
val maxHeap: Long? by project
val heap: Long = maxHeap ?: if (System.getProperty("os.name").lowercase().contains("linux")) {
    ByteArrayOutputStream().use { output ->
        exec {
            executable = "bash"
            args = listOf("-c", "cat /proc/meminfo | grep MemAvailable | grep -o '[0-9]*'")
            standardOutput = output
        }
        output.toString().trim().toLong() / 1024
    }.also { println("Detected ${it}MB RAM available.") } * 9 / 10
} else {
    // Guess 16GB RAM of which 2 used by the OS
    14 * 1024L
}
val taskSizeFromProject: Int? by project
val taskSize = taskSizeFromProject ?: 512
val threadCount = maxOf(1, minOf(Runtime.getRuntime().availableProcessors(), heap.toInt() / taskSize))

val runAllBatch by tasks.register<DefaultTask>("runAllBatch") {
    group = alchemistGroup
    description = "Launches all experiments"
}

val alchemistGroup = "Run Alchemist"
fun String.capitalizeString(): String =
    this.replaceFirstChar {
        if (it.isLowerCase()) {
            it.titlecase(
                Locale.getDefault(),
            )
        } else {
            it.toString()
        }
    }

val incarnations = listOf("collektive", "protelis", "scafi")
incarnations.forEach { incarnation ->
    File(rootProject.rootDir.path + "/src/main/resources/yaml/${incarnation}").listFiles()
        ?.filter { it.extension == "yml" }
        ?.sortedBy { it.nameWithoutExtension }
        ?.forEach {
            fun basetask(name: String, additionalConfiguration: JavaExec.() -> Unit = {}) = tasks.register<JavaExec>(name) {
                group = alchemistGroup
                description = "Launches graphic simulation ${it.nameWithoutExtension} with Collektive incarnation"
                mainClass.set("it.unibo.alchemist.Alchemist")
                classpath = sourceSets["main"].runtimeClasspath
                args("run", it.absolutePath)
                if (System.getenv("CI") == "true") {
                    args("--override", "terminate: { type: AfterTime, parameters: [2] } ")
                } else {
                    this.additionalConfiguration()
                }
            }
            val capitalizedName = (incarnation + it.nameWithoutExtension.capitalizeString()).capitalizeString()
            val batch by basetask("run${capitalizedName}Batch") {
                description = "Launches batch experiments for $capitalizedName"
                maxHeapSize = "${minOf(heap.toInt(), Runtime.getRuntime().availableProcessors() * taskSize)}m"
                File("data").mkdirs()
            }
            runAllBatch.dependsOn(batch)
        }
}

tasks.withType(KotlinCompile::class).all {
    kotlinOptions.freeCompilerArgs = listOf("-Xcontext-receivers")
}
