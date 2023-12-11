package day11

import println
import readInput
import kotlin.math.abs


fun main() {
    fun List<String>.parseGalaxy(): List<Pair<Int, Int>> {
        return foldIndexed(mutableListOf<Pair<Int, Int>>()) { x, acc, string ->
            acc.addAll(
                    string.mapIndexedNotNull { y, char ->
                        if (char == '#') {
                            x to y
                        } else {
                            null
                        }
                    }
            )
            acc
        }
    }

    fun List<Pair<Int, Int>>.space(multiplier: Long): List<Pair<Long, Long>> {
        val rows = map { it.first }
                .distinct()
                .sorted()
                .scan(Pair(-1, -1L)) { acc, index ->
                    val distance = index - acc.first - 1
                    index to acc.first + (acc.second - acc.first) + multiplier * distance + 1
                }
                .drop(1)
                .toMap()

        val columns = map { it.second }
                .distinct()
                .sorted()
                .scan(Pair(-1, -1L)) { acc, index ->
                    val distance = index - acc.first - 1
                    index to acc.first + (acc.second - acc.first) + multiplier * distance + 1
                }
                .drop(1)
                .toMap()

        return map { (x, y) ->
            rows.getValue(x) to columns.getValue(y)
        }
    }

    fun part1(input: List<String>, multiplier: Long = 2L): Long {
        val space = input
                .parseGalaxy()
                .space(multiplier)
        return space
                .mapIndexed { index, currentGalaxy ->
                    if (index == space.size - 1) {
                        emptyList()
                    } else {
                        space
                                .subList(index + 1, space.size)
                                .map { galaxy ->
                                    galaxy.first - currentGalaxy.first + abs(galaxy.second - currentGalaxy.second)
                                }
                    }
                }
                .flatten()
                .sum()
    }

    fun part2(input: List<String>, multiplier: Long): Long {
        return part1(input, multiplier)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day11/Day11_test")
    check(part1(testInput) == 374L)
    check(part2(testInput, 10L) == 1030L)
    check(part2(testInput, 100L) == 8410L)

    val input = readInput("day11/Day11")
    part1(input).println()
    part2(input, 1000000L).println()
}
