package day09

import println
import readInput


fun main() {
    fun List<String>.toSequenceOfDifferences() =
            map { sequenceString ->
                val sequence = sequenceString
                        .split(" ")
                        .map { numberString ->
                            numberString.toInt()
                        }
                val sequences = mutableListOf(sequence)
                while (!sequences.last().all { it == sequences.last().first() }) {
                    sequences.add(
                            sequences
                                    .last()
                                    .windowed(2)
                                    .map { it.last() - it.first() }
                    )
                }
                sequences
            }


    fun part1(input: List<String>): Int {
        return input
                .toSequenceOfDifferences()
                .sumOf { sequences ->
                    val result = sequences
                            .foldRight(0) { seq, acc ->
                                seq.last() + acc
                            }
                    result
                }
    }

    fun part2(input: List<String>): Int {
        return input
                .toSequenceOfDifferences()
                .sumOf { sequences ->
                    val result = sequences
                            .foldRight(0) { seq, acc ->
                                seq.first() - acc
                            }
                    result
                }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day09/Day09_test")
    check(part1(testInput) == 114)
    check(part2(testInput) == 2)

    val input = readInput("day09/Day09")
    part1(input).println()
    part2(input).println()
}
