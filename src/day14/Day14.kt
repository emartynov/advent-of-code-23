package day14

import println
import readInput


fun main() {
    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day14/Day14_test")
    check(part1(testInput) == 405)
    check(part2(testInput) == 400)

    val input = readInput("day14/Day14")
    part1(input).println()
    part2(input).println()
}
