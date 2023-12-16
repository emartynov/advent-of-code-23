package day15

import println
import readInput

private data class Lens(
        val label: String,
        val focus: Int
)

fun main() {
    fun hash(sequence: String): Int = sequence.fold(0) { acc, c ->
        ((acc + c.code) * 17) % 256
    }

    fun part1(input: List<String>): Int {
        return input
                .first()
                .split(",")
                .sumOf { sequence ->
                    hash(sequence)
                }
    }

    fun part2(input: List<String>): Int {
        return input
                .first()
                .split(",")
                .fold(mutableMapOf<Int, MutableList<Lens>>()) { acc, sequence ->
                    val operation = if (sequence.indexOf("=") > -1) "=" else "-"
                    if (operation == "=") {
                        val label = sequence.substring(0..sequence.length - 3)
                        val boxNumber = hash(label)
                        val listInBox = acc[boxNumber] ?: mutableListOf()
                        val focus = sequence.last().toString().toInt()
                        val indexOfLens = listInBox.indexOfFirst { it.label == label }
                        if (indexOfLens != -1) {
                            listInBox[indexOfLens] = Lens(label, focus)
                        } else {
                            listInBox.add(Lens(label, focus))
                        }
                        acc[boxNumber] = listInBox
                    } else {
                        val label = sequence.substring(0..sequence.length - 2)
                        acc.values.forEach { it.removeAll { lens -> lens.label == label } }
                    }
                    acc
                }
                .entries
                .sumOf { entry ->
                    entry
                            .value
                            .foldIndexed(0) { index, acc, lens ->
                                acc + (entry.key + 1) * (index + 1) * lens.focus
                            } as Int
                }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day15/Day15_test")
    check(part1(testInput) == 1320)
    check(part2(testInput) == 145)

    val input = readInput("day15/Day15")
    part1(input).println()
    part2(input).println()
}
