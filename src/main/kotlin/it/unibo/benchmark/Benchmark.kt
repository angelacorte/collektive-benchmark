package it.unibo.benchmark

import it.unibo.alchemist.boundary.OutputMonitor
import it.unibo.alchemist.model.Actionable
import it.unibo.alchemist.model.Environment
import it.unibo.alchemist.model.positions.Euclidean2DPosition
import it.unibo.alchemist.model.terminators.AfterTime
import it.unibo.alchemist.model.times.DoubleTime
import it.unibo.alchemist.test.loadYamlSimulation
import it.unibo.alchemist.test.startSimulation
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption.APPEND
import java.nio.file.StandardOpenOption.CREATE
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import kotlin.io.path.Path

const val checkPoints = 1000

fun main() {
    val path = File("${Path("").toAbsolutePath()}/results")
    if (!path.exists()) path.mkdir()
    val filePath = Paths.get(path.toString(), "results.txt")
    val file = File(filePath.toString())
    if (!file.exists()) file.createNewFile()
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
    val startedAt = LocalDateTime.now().format(formatter)
    val store: MutableMap<SimulationType, Results> = mutableMapOf()
    val incarnations = listOf("collektive", "scafi", "protelis")
    val tests = listOf("fieldEvolution", "neighborCounter", "branching", "gradient", "channelWithObstacles")
        .flatMap { t -> incarnations.map { i -> i to t } }

    repeat(10) { i ->
        tests.map { (incarnation, testType) ->
            val experiment = incarnation to testType
            val simulation = loadYamlSimulation<Any?, Euclidean2DPosition>("yaml/$incarnation/$testType.yml")

            simulation.environment.addTerminator(AfterTime(DoubleTime(100.0)))
            Thread.sleep(1000)

            val startTime = System.currentTimeMillis()
            simulation.startSimulation(steps = Long.MAX_VALUE)
            val endTime = System.currentTimeMillis()
            val duration = endTime - startTime
            println("Simulation $experiment took $duration ms")
            store[SimulationType(experiment.first, experiment.second, i, simulation.environment.nodes.size)] =
                Results(duration, simulation.step)
        }
    }
    val finishedAt = LocalDateTime.now().format(formatter)
    val sortedStore = store.toSortedMap(
        compareBy<SimulationType> { it.incarnation }
            .thenBy { it.testType }
            .thenBy { it.nodes }
            .thenBy { it.cycle },
    )
    val averageStore = store.entries.groupBy { it.key.incarnation to it.key.testType }.mapValues { (_, res) ->
        (res.sumOf { it.value.duration } / res.size).toDouble()
    }
    Files.write(
        Paths.get(filePath.toString()),
        ("Test started at: $startedAt - Finished at $finishedAt\nResults:${sortedStore.map { "\n$it" }}\n" +
                "Average:${averageStore.map { "\n$it" }}\n").toByteArray(),
        if (file.exists()) APPEND else CREATE,
    )
    averageStore.toCSV(Paths.get(path.toString(), "results.csv").toString(), startedAt, finishedAt)
}

private fun Map<Pair<String,String>, Double>.toCSV(path: String, started: String, finished: String) {
    val file = File(path)
    val writer = BufferedWriter(FileWriter(file, file.exists()))

    if(!file.exists()) writer.write("Incarnation,TestType,Average,StartTime,EndTime\n")
    this.forEach { (key, entry) ->
        writer.write("${key.first},${key.second},$entry,$started,$finished\n")
    }
    writer.close()
}