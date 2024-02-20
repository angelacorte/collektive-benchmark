package it.unibo.collektive.examples.time

import it.unibo.collektive.aggregate.api.Aggregate

fun Aggregate<Int>.fieldEvolution(): Int = repeat(0) { it + 1 }