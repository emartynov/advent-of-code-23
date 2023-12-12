package day12

import println
import readInput
import kotlin.math.abs


fun main() {

    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return part1(input)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day12/Day12_test")
    check(part1(testInput) == 10)
    check(part2(testInput) == 10)

    val input = readInput("day12/Day12")
//    part1(input).println()
//    part2(input).println()
}
