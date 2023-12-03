package day03

import println
import readInput
import kotlin.math.max
import kotlin.math.min

private data class NumberPlace(
    val number: Int,
    val row: Int,
    val range: IntRange
)

private fun NumberPlace.isAdjusted(lines: List<String>): Boolean {
    val upAndDownCharsList = listOf(row - 1, row + 1)
        .filter { it > -1 && it < lines.size }
        .map { lineIndex ->
            lines[lineIndex]
                .substring(max(0, range.first - 1), min(lines.first().length, range.last + 2))
                .toList()
        }
        .flatten()

    val charList = (
            upAndDownCharsList +
                    (if (range.first > 0) lines[row][range.first - 1] else null) +
                    (if (range.last < lines[row].length - 1) lines[row][range.last + 1] else null)
            ).filterNotNull()

    return charList.any { !it.isDigit() && it != '.' }
}

fun main() {
    fun List<String>.parseNumbers(): List<NumberPlace> {
        val numberRegex = Regex("\\d+")
        return mapIndexed { rowIndex, string ->
            numberRegex
                .findAll(string)
                .map {
                    NumberPlace(
                        number = it.value.toInt(),
                        row = rowIndex,
                        range = it.range
                    )
                }.toList()
        }
            .flatten()
    }

    fun List<String>.parseStars(): List<Pair<Int, Int>> {
        return this.mapIndexed { rowIndex, s ->
            s.foldIndexed(emptyList<Pair<Int, Int>>()) { index, acc, char ->
                if (char == '*') {
                    acc + (rowIndex to index)
                } else {
                    acc
                }
            }
        }.flatten()
    }

    fun part1(input: List<String>): Int {
        return input
            .parseNumbers()
            .sumOf { numberPlace ->
                if (numberPlace.isAdjusted(input)) numberPlace.number else 0
            }
    }

    fun part2(input: List<String>): Long {
        val numbers = input.parseNumbers()
        val stars = input.parseStars()
        return stars
            .map { starCoordinates ->
                numbers
                    .filter { numberPlace ->
                        (numberPlace.row in starCoordinates.first - 1..starCoordinates.first + 1) &&
                                starCoordinates.second in (numberPlace.range.first - 1..numberPlace.range.last + 1)
                    }

            }
            .filter { it.size == 2 }
            .sumOf { gearNumbers ->
                gearNumbers.first().number.toLong() * gearNumbers.last().number
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day03/Day03_test")
    check(part1(testInput) == 4361)
    check(part2(testInput) == 467835L)

    val input = readInput("day03/Day03")
    part1(input).println()
    part2(input).println()
}
