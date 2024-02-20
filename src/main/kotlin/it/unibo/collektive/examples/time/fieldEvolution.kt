package it.unibo.collektive.examples.time

import it.unibo.collektive.aggregate.api.Aggregate
import it.unibo.collektive.aggregate.api.operators.neighboringViaExchange

fun Aggregate<Int>.stateChange(id: Int): Int = neighboringViaExchange(id * 2).localValue