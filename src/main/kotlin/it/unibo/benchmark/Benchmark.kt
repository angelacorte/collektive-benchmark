package it.unibo.benchmark

import it.unibo.alchemist.boundary.OutputMonitor
import it.unibo.alchemist.model.Actionable
import it.unibo.alchemist.model.Environment
import it.unibo.alchemist.model.positions.Euclidean2DPosition
import it.unibo.alchemist.model.terminators.AfterTime
import it.unibo.alchemist.model.times.DoubleTime
import it.unibo.alchemist.test.loadYamlSimulation
import it.unibo.alchemist.test.startSimulation
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption.APPEND
import java.nio.file.StandardOpenOption.CREATE
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import kotlin.io.path.Path

const val checkPoints = 1000

fun main() {
    val path = File("${Path("").toAbsolutePath()}/results")
    if (!path.exists()) path.mkdir()
    val filePath = Paths.get(path.toString(), "results.txt")
    val file = File(filePath.toString())
    if (!file.exists()) file.createNewFile()
    val incarnations = listOf("collektive", "scafi", "protelis")
    val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")
    val startedAt = LocalDateTime.now().format(formatter)
    val store: MutableMap<SimulationType, Results> = mutableMapOf()
    listOf("fieldEvolution", "neighborCounter", "gradient").forEach { testType ->  //"channelWithObstacles", "branching"
        for (i in 1 .. 3) { //16
            incarnations.map { it to loadYamlSimulation<Any?, Euclidean2DPosition>("yaml/$it/$testType.yml")}
                .forEach { (experiment, simulation) ->
                    simulation.environment.addTerminator(AfterTime(DoubleTime(5_000.0)))
                    Thread.sleep(1000)
                    val startTime = System.currentTimeMillis()
                    var nextCheckPoint = 0
                    simulation.addOutputMonitor(object : OutputMonitor<Any?, Euclidean2DPosition> {
                        override fun stepDone(
                            environment: Environment<Any?, Euclidean2DPosition>,
                            reaction: Actionable<Any?>?,
                            time: it.unibo.alchemist.model.Time,
                            step: Long,
                        ) {
                            val time = simulation.time.toDouble()
                            if (time > nextCheckPoint) {
                                println("$experiment for $testType #$i Simulation at $time after ${System.currentTimeMillis() - startTime}ms, performed $step steps")
                                nextCheckPoint += checkPoints
                            }
                        }
                    })
                    simulation.startSimulation(steps = Long.MAX_VALUE)
                    val endTime = System.currentTimeMillis()
                    val duration = endTime - startTime
                    println("Simulation $testType for $experiment took $duration ms")
                    store[SimulationType(experiment, testType, i, simulation.environment.nodes.size)] =
                        Results(duration, simulation.step)
                }
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
        ("Test started at: $startedAt - Finished at $finishedAt\nResults:${sortedStore.map { "\n$it" }}\nAverage:${averageStore.map { "\n$it" }}\n").toByteArray(),
        if (file.exists()) APPEND else CREATE,
    )
}
