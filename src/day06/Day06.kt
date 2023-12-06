package day06

import println
import readInput
import kotlin.math.max


fun main() {
    fun part1(input: List<String>): Int {
        return input
                .parseRaces()
                .map { race ->
                    (1..race.first)
                            .map { secondsAndSpeed ->
                                (race.first - secondsAndSpeed) * secondsAndSpeed
                            }
                            .fold(0) { acc, distance ->
                                acc + if (distance > race.second) 1 else 0
                            }
                }.let {
                    it.fold(1) { acc, i -> acc * i }
                }
    }

    fun part2(input: List<String>): Long {
        val indexOfColument = max(input.first().indexOf(":"), input.last().indexOf(":"))
        return input
                .chunked(2)
                .first()
                .let {
                    val time = it.first()
                        .drop(indexOfColument + 1)
                        .replace(" ", "")
                        .toLong()
                    val distance = it.last()
                        .drop(indexOfColument + 1)
                        .replace(" ", "")
                        .toLong()
                    (1..time)
                            .map { secondsAndSpeed ->
                                (time - secondsAndSpeed) * secondsAndSpeed
                            }
                            .fold(0L) { acc, distanceCalculated ->
                                acc + if (distanceCalculated > distance) 1L else 0L
                            }
                }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day06/Day06_test")
    check(part1(testInput) == 288)
    check(part2(testInput) == 71503L)

    val input = readInput("day06/Day06")
    part1(input).println()
    part2(input).println()
}

private fun List<String>.parseRaces(): List<Pair<Int, Int>> {
    val indexOfColument = max(first().indexOf(":"), last().indexOf(":"))
    return listOf(first(), last())
            .map { stringValue ->
                stringValue.drop(indexOfColument + 1)
                        .fold(mutableListOf<String>()) { acc, char ->
                            val addition = if (char == ' ' && "" != acc.lastOrNull()) {
                                ""
                            } else if (char != ' ') {
                                val last = acc.removeLast()
                                last + char
                            } else {
                                null
                            }
                            if (addition != null) {
                                acc.add(addition)
                            }
                            acc
                        }
                        .map { it.trim().toInt() }
            }
            .chunked(2)
            .first()
            .let {
                val distances = it.last()
                val times = it.first()
                times.mapIndexed { index, time ->
                    time to distances[index]
                }
            }
}
