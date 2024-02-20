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
import kotlin.io.path.Path

const val checkPoints = 1000

fun main() {
    val path = File("${Path("").toAbsolutePath()}/results")
    if (!path.exists()) path.mkdir()
    val filePath = Paths.get(path.toString(), "results.txt")
    val file = File(filePath.toString())
    if (!file.exists()) file.createNewFile()
    val incarnations = listOf("collektive", "scafi", "protelis")
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val startedAt = LocalDateTime.now().format(formatter)
    val store: MutableMap<SimulationType, Results> = mutableMapOf()
    listOf("fieldEvolution", "neighborCounter", "gradient").forEach { testType ->  //"channelWithObstacles", "branching"
        for (i in 0 until 2) { //16
            incarnations.map { it to loadYamlSimulation<Any?, Euclidean2DPosition>("yaml/$it/$testType.yml")}
                .forEach { (experiment, simulation) ->
                    simulation.environment.addTerminator(AfterTime(DoubleTime(10_000.0)))
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
                                println("$experiment gradient Simulation at $time after ${System.currentTimeMillis() - startTime}ms, performed $step steps")
                                nextCheckPoint += checkPoints
                            }
                        }
                    })
                    simulation.startSimulation(steps = Long.MAX_VALUE)
                    val endTime = System.currentTimeMillis()
                    val duration = endTime - startTime
                    println("Simulation $testType for $experiment took $duration ms")
                    store[SimulationType(experiment, testType, i, simulation.environment.nodes.size)] =
                        Results(simulation.step, duration)
                }
        }
    }
    val finishedAt = LocalDateTime.now().format(formatter)
    Files.write(
        Paths.get(filePath.toString()),
        ("Test started at: $startedAt\n${store.map { "\n$it" }}\nFinished at: $finishedAt\n").toByteArray(),
        if (file.exists()) APPEND else CREATE,
    )
    store.toCSV(startedAt, finishedAt, Paths.get(path.toString(), "results.csv").toString())
}