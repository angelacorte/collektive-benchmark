# Collektive Benchmark

This repository contains the code for a benchmark for [Collektive](https://github.com/Collektive/collektive)'s API, 
confronting it with the current aggregate computing's state of the art: [ScaFi](https://scafi.github.io/docs/) and [Protelis](https://protelis.github.io).

## How to run the benchmark

To run the benchmark, you need to clone this repository on your pc, moving into the root folder and running the following command:

```bash
./gradlew runBenchmark
```
It will run the benchmark with three different incarnations: Collektive, ScaFi and Protelis, over a set of five different simulations:
1. A simple state change of the device, to represent the variation over time of the field ([Field Evolution](https://github.com/angelacorte/collektive-benchmark/blob/main/src/main/kotlin/it/unibo/collektive/examples/fieldEvolution/FieldEvolution.kt));
2. A counter of the neighbours, to represent the variation of the space ([Neighbor Counter](https://github.com/angelacorte/collektive-benchmark/blob/main/src/main/kotlin/it/unibo/collektive/examples/neighbors/NeighborCounter.kt));
3. Simple branching operations, as it has been noticed that branching could be one of the most expensive operations in terms of execution time ([Branching](https://github.com/angelacorte/collektive-benchmark/blob/main/src/main/kotlin/it/unibo/collektive/examples/branching/Branching.kt));
4. A gradient, which is a particular case of space-time variation, in which the value of a node is a function of the distance from a node considered as a source ([Gradient](https://github.com/angelacorte/collektive-benchmark/blob/main/src/main/kotlin/it/unibo/collektive/examples/gradient/Gradient.kt));
5. A channel with obstacles, to represent the presence of obstacles in space that influence the communication between nodes ([Channel with Obstacles](https://github.com/angelacorte/collektive-benchmark/blob/main/src/main/kotlin/it/unibo/collektive/examples/channel/ChannelWithObstacles.kt)).

By default, the benchmark will run 10 times for each simulation, with the simulation interruption time set at 300 simulated seconds. 
The results will be printed on the console and saved as `.txt` inside the folder `results`, also the average time of execution will be saved in the same directory as a `.csv` file. 

If you want to change the parameters of the benchmark, you can do it by modifying the `Benchmark.kt` file.

If you want to change the parameters of the simulations, you can do it by modifying the `yaml` files for each simulation or incarnation, 
located in the `src/main/resources/yaml` folder.

## Running graphical simulations

It is possible to run also the graphical simulations with the [Alchemist simulator](https://alchemistsimulator.github.io).
You can list the available simulations by running the following command:
```bash
./gradlew tasks --all
```
And it will list all the available tasks, including the ones for the graphical simulations in the section "Run Alchemist tasks", or:
```bash
./gradlew run<Incarnation><SimulationType>Graphics
```
Where `<Incarnation>` is the incarnation you want to run (Collektive, ScaFi or Protelis) and `<SimulationType>` is the type of simulation you want to run (FieldEvolution, NeighborCounter, Branching, Gradient or ChannelWithObstacles).