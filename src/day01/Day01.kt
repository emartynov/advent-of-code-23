package day01

import println
import readInput

private val wordDigits = listOf(
    "one", "two", "three", "four", "five", "six", "seven", "eight", "nine"
)

fun main() {
    fun part1(input: List<String>): Int {
        return input
            .asSequence()
            .map { calibrationValue ->
                val firstDigitIndex = calibrationValue.indexOfFirst { it.isDigit() }
                val lastDigitIndex = calibrationValue.indexOfLast { it.isDigit() }
                "${calibrationValue[firstDigitIndex]}${calibrationValue[lastDigitIndex]}"
            }
            .map { it.toInt() }
            .sum()
    }

    fun part2(input: List<String>): Int {
        return input
            .asSequence()
            .map {
                var result = it
                wordDigits.forEachIndexed { index, wordAsDigit ->
                    val digitString = (index + 1).toString()
                    result = result.replace(wordAsDigit, digitString)
                }
                result
            }
            .map { calibrationValue ->
                val firstDigitIndex = calibrationValue.indexOfFirst { it.isDigit() }
                val lastDigitIndex = calibrationValue.indexOfLast { it.isDigit() }
                "${calibrationValue[firstDigitIndex]}${calibrationValue[lastDigitIndex]}"
            }
            .map { it.toInt() }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day01/Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("day01/Day01")
    part1(input).println()
    part2(input).println()
}
