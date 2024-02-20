package it.unibo.benchmark

data class SimulationType(val incarnation: String, val testType: String, val cycle: Int, val nodes: Int)

data class Results(val duration: Long, val steps: Long)