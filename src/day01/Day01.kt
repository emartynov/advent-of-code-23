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
            .map { calibrationValue ->
                val firstDigitWord = wordDigits
                    .mapIndexed { index, digitWord ->
                        calibrationValue.indexOf(digitWord) to (index + 1)
                    }
                    .filter { it.first > -1 }
                    .minByOrNull { it.first }
                val lastDigitWord = wordDigits
                    .mapIndexed { index, digitWord ->
                        calibrationValue.lastIndexOf(digitWord) to (index + 1)
                    }
                    .filter { it.first > -1 }
                    .maxByOrNull { it.first }
                val firstDigit = with(calibrationValue) {
                    val index = indexOfFirst { it.isDigit() }
                    if (index > -1) (index to substring(index, index + 1).toInt()) else null
                }
                val lastDigit = with(calibrationValue) {
                    val index = indexOfLast { it.isDigit() }
                    if (index > -1) (index to substring(index, index + 1).toInt()) else null
                }
                val digitPairs = listOfNotNull(firstDigitWord, lastDigitWord, firstDigit, lastDigit)
                    .sortedBy { it.first }
                10 * digitPairs.first().second + digitPairs.last().second
            }
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day01/Day01_test")
    check(part1(testInput) == 142)

    val input = readInput("day01/Day01")
    part1(input).println()
    part2(input).println()
}
