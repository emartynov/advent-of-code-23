package day13

import println
import readInput


fun main() {
    fun List<String>.toPatterns(): List<List<String>> {
        val result = mutableListOf<MutableList<String>>()
        result.add(mutableListOf())

        forEach {
            if (it.isEmpty()) {
                result.add(mutableListOf())
            } else {
                result.last().add(it)
            }
        }

        if (result.lastOrNull()?.isEmpty() == true) {
            result.dropLast(1)
        }

        return result
    }

    fun List<String>.mirrowRowNumber(): Int {
        (1..<size).forEach { row ->
            var index = 1
            var matched = 0
            while (row - index > -1 && row + index - 1 < size) {
                if (get(row - index) != get(row + index - 1)) {
                    break
                } else {
                    matched += 1
                }
                index += 1
            }
            if (matched > 0 && (matched == row || matched == size - row)) {
                return row
            }
        }
        return 0
    }

    fun List<String>.columnAsRows(): List<String> {
        val result = List(first().length) { StringBuilder() }

        (0..<first().length).forEach { columnIndex ->
            forEach { row ->
                result[columnIndex].append(row[columnIndex])
            }
        }

        return result.map { it.toString() }
    }

    fun part1(input: List<String>): Int {
        return input
                .toPatterns()
                .sumOf { pattern ->
                    100 * pattern.mirrowRowNumber() + pattern.columnAsRows().mirrowRowNumber()
                }
    }

    fun part2(input: List<String>): Int {
        return part1(input)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day13/Day13_test")
    check(part1(testInput) == 405)
//    check(part2(testInput) == 525152)

    val input = readInput("day13/Day13")
    part1(input).println()
//    part2(input).println()
}
