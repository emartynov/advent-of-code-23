package day13

import println
import readInput


fun main() {


    fun part1(input: List<String>): Int {
        return input.size
    }

    fun part2(input: List<String>): Int {
        return part1(input)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day13/Day13_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 525152)

    val input = readInput("day13/Day13")
    part1(input).println()
//    part2(input).println()
}
