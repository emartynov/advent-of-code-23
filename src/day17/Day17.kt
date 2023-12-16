package day17

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
    val testInput = readInput("day17/Day17_test")
    check(part1(testInput) == 1320)
    check(part2(testInput) == 145)

    val input = readInput("day17/Day17")
    part1(input).println()
    part2(input).println()
}
