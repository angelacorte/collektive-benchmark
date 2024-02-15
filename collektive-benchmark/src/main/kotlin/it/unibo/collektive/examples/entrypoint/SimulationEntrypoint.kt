package it.unibo.collektive.examples.entrypoint

import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.alchemist.device.DistanceSensor
import it.unibo.collektive.examples.gradient.gradient
import it.unibo.collektive.examples.neighbors.neighborCounter
import it.unibo.collektive.examples.time.stateChange

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
fun Aggregate<Int>.stateChangeEntrypoint(): Int = stateChange(localId)
