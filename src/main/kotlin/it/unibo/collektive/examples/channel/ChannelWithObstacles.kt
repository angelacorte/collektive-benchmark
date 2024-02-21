package it.unibo.collektive.examples.channel

import it.unibo.alchemist.device.LocalSensing
import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.aggregate.api.operators.share
import it.unibo.collektive.alchemist.device.DistanceSensor
import it.unibo.collektive.examples.gradient.gradient
import it.unibo.collektive.field.Field.Companion.hood
import it.unibo.collektive.field.plus
import kotlin.Double.Companion.POSITIVE_INFINITY
import kotlin.math.min

context(LocalSensing, DistanceSensor)
fun Aggregate<Int>.channelWithObstacles(): Any =
    if (sense("obstacle")) {
        false
    } else {
        channel(sense("source"), sense("target"), channelWidth = 0.3)
    }

context(DistanceSensor)
fun Aggregate<Int>.channel(source: Boolean, target: Boolean, channelWidth: Double): Boolean =
    gradient(source) + gradient(target) <= distanceBetween(source, target) + channelWidth

context(DistanceSensor)
fun Aggregate<Int>.distanceBetween(source: Boolean, target: Boolean): Double = broadcast(source, gradient(target))

context(DistanceSensor)
fun <A>Aggregate<Int>.broadcast(source: Boolean, value: A): A = gradientCast(source, value) { it }

context(DistanceSensor)
fun <A>Aggregate<Int>.gradientCast(source: Boolean, initial: A, accumulate: (A) -> A): A =
    share(POSITIVE_INFINITY to initial) {
        val gradient = distances() + it.map { it.first }
        val accumulated = it.map { accumulate(it.second) }
        val combined = gradient.alignedMap(accumulated) { g, a -> g to a }
        val minTuple = combined.toMap().minBy { it.value.first }
        if (source) {
            0.0 to initial
        } else {
            if (minTuple.value.first == POSITIVE_INFINITY) {
                POSITIVE_INFINITY to initial
            } else {
                minTuple.value
            }
        }
    }.second
