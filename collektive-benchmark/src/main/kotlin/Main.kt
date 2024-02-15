import it.unibo.alchemist.boundary.OutputMonitor
import it.unibo.alchemist.model.Actionable
import it.unibo.alchemist.model.Environment
import it.unibo.alchemist.model.positions.Euclidean2DPosition
import it.unibo.alchemist.model.terminators.AfterTime
import it.unibo.alchemist.model.times.DoubleTime
import it.unibo.alchemist.test.loadYamlSimulation
import it.unibo.alchemist.test.startSimulation
import java.sql.Time
import javax.swing.text.Position

val checkPoints = 1000

fun main() {

    listOf("collektive", "scafi").map { it to loadYamlSimulation<Any?, Euclidean2DPosition>("yaml/$it/gradient.yml")}
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
                    step: Long
                ) {
                    val time = simulation.time.toDouble()
                    if (time > nextCheckPoint) {
                        println("Simulation at $time after ${System.currentTimeMillis() - startTime}ms, performed $step steps")
                        nextCheckPoint += checkPoints
                    }
                }
            })
            simulation.startSimulation(steps = Long.MAX_VALUE)
            val endTime = System.currentTimeMillis()
            println("Simulation for $experiment took ${endTime - startTime} ms")
        }
}