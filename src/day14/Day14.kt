package day14

import println
import readInput


fun main() {
    fun List<String>.tiltToNorth(): List<String> {
        return foldIndexed(mutableListOf<String>()) { index, acc, _ ->
            if (index < size - 1) {
                val destination = StringBuilder(acc.removeLastOrNull() ?: first())
                val source = StringBuilder(get(index + 1))
                destination.forEachIndexed { charIndex, c ->
                    if (c == '.' && source[charIndex] == 'O') {
                        destination.replace(charIndex, charIndex + 1, "O")
                        source.replace(charIndex, charIndex + 1, ".")
                    }
                }
                acc.add(destination.toString())
                acc.add(source.toString())
            }
            acc
        }
    }

    fun part1(input: List<String>): Int {
        return input
                .let {
                    var prevResult = it
                    var result: List<String> = it.tiltToNorth()
                    while (result != prevResult) {
                        prevResult = result
                        result = result.tiltToNorth()
                    }
                    result
                }
                .foldRightIndexed(0) { index, stringValue, acc ->
                    acc + (input.size - index) * stringValue.count { it == 'O' }
                }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day14/Day14_test")
    check(part1(testInput) == 136)
//      check(part2(testInput) == 400)

    val input = readInput("day14/Day14")
    part1(input).println()
//    part2(input).println()
}
