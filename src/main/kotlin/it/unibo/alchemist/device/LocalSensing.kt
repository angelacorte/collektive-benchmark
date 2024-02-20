package it.unibo.alchemist.device

interface LocalSensing {
    fun <T> sense(name: String): T
}