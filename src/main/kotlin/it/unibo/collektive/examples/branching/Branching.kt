package it.unibo.collektive.examples.branching

import it.unibo.alchemist.device.LocalSensing
import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.aggregate.api.operators.neighboringViaExchange
import it.unibo.collektive.field.Field.Companion.hood

context(LocalSensing)
fun Aggregate<Int>.branching() =
    if (sense("source")) {
        neighboringViaExchange(1).hood(0) { acc, _ -> acc + 1 }
    } else {
        0
    }