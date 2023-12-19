package day19

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
    val testInput = readInput("day19/Day19_test")
    check(part1(testInput) == 62)
//    check(part2(testInput) == 145)

    val input = readInput("day19/Day19")
    part1(input).println()
//    part2(input).println()
}
