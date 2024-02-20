package it.unibo.collektive.examples.channel

import it.unibo.alchemist.device.LocalSensing
import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.alchemist.device.DistanceSensor

context(DistanceSensor)
fun Aggregate<Int>.channelWithObstacles(obstacle: Boolean, source: Boolean, target: Boolean) {
    if (obstacle) {
        false
    } else {
        distances().map { it }
    }

//    fun channel(obstacle: Boolean, source: Boolean, target: Boolean) = TODO()

    //distances().map { it == }
}

fun Aggregate<Int>.channel() = TODO()

context(LocalSensing, DistanceSensor)
fun Aggregate<Int>.channelEntrypoint() = channelWithObstacles(sense("obstacle"), localId == 0, sense("target"))
