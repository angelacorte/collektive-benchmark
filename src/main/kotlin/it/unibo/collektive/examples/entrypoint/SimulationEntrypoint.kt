package it.unibo.collektive.examples.entrypoint

import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.alchemist.device.DistanceSensor
import it.unibo.collektive.examples.branching.branching
import it.unibo.collektive.examples.channel.channelWithObstacles
import it.unibo.collektive.examples.gradient.gradient
import it.unibo.collektive.examples.neighbors.neighborCounter
import it.unibo.collektive.examples.time.fieldEvolution

/**
 * The entrypoint of the simulation running a gradient.
 */
context(DistanceSensor)
fun Aggregate<Int>.gradientEntrypoint(): Double = gradient(localId == 0)

/**
 * The entrypoint of the simulation running a counter of neighbors.
 */
fun Aggregate<Int>.neighborCounterEntrypoint(): Int = neighborCounter()

/**
 * The entrypoint of the simulation running a state change.
 */
fun Aggregate<Int>.fieldEvolutionEntrypoint(): Int = fieldEvolution()

context(DistanceSensor)
fun Aggregate<Int>.channelEntrypoint() = channelWithObstacles(true, localId == 0, true)

fun Aggregate<Int>.branchingEntrypoint(): Int = branching(localId == 0)