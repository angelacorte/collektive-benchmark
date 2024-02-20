package it.unibo.collektive.examples.branching

import it.unibo.alchemist.device.LocalSensing
import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.aggregate.api.operators.neighboringViaExchange
import it.unibo.collektive.field.Field.Companion.hood

fun Aggregate<Int>.branching(cond: Boolean) =
    if (cond) {
        neighboringViaExchange(1).hood(0) { acc, _ -> acc + 1 }
    } else {
        0
    }

context(LocalSensing)
fun Aggregate<Int>.branchingEntrypoint(): Int = branching(sense("sensor"))