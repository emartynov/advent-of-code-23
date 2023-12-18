package day18

import println
import readInput
import java.lang.IllegalArgumentException

private enum class Direction {
    NORTH,
    EAST,
    WEST,
    SOUTH
}

private data class Dig(
        val direction: Direction,
        val meters: Int,
        val color: String
)

fun main() {
    fun List<String>.parseInstructions(): List<Dig> =
            map { stringValue ->
                val (directionString, metersString, colorString) = stringValue.split(" ")
                Dig(
                        direction = directionString.toDirection(),
                        meters = metersString.toInt(),
                        color = colorString.substring(1..colorString.length - 2)
                )
            }

    fun List<String>.markOutbound(): List<String> {
        val result = map { StringBuilder(it) }
        val nextOutbounds = mutableListOf<Pair<Int, Int>>()
        first().forEachIndexed { index, c ->
            if (c == '.') nextOutbounds.add(0 to index)
        }
        last().forEachIndexed { index, c ->
            if (c == '.') nextOutbounds.add(size - 1 to index)
        }
        forEachIndexed { index, s ->
            if (s.first() == '.') nextOutbounds.add(index to 0)
            if (s.last() == '.') nextOutbounds.add(index to s.length - 1)
        }
        while (nextOutbounds.isNotEmpty()) {
            val pointToCheck = nextOutbounds.removeFirst()
            val char = result[pointToCheck.first][pointToCheck.second]
            if (char == '.') {
                result[pointToCheck.first][pointToCheck.second] = '$'
                nextOutbounds.addAll(
                        listOf(
                                pointToCheck.first - 1 to pointToCheck.second,
                                pointToCheck.first + 1 to pointToCheck.second,
                                pointToCheck.first to pointToCheck.second - 1,
                                pointToCheck.first to pointToCheck.second + 1,
                        )
                                .filter {
                                    it.first > -1 &&
                                            it.second > -1 &&
                                            it.second < result.first().length &&
                                            it.first < result.size
                                }
                )
            }
        }
        return result.map { it.toString() }
    }

    fun part1(input: List<String>): Int {
        return input
                .parseInstructions()
                .toPlot()
                .markOutbound()
                .sumOf { row ->
                    row.count { it == '.' || it == '#' }
                }
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day18/Day18_test")
    check(part1(testInput) == 62)
//    check(part2(testInput) == 145)

    val input = readInput("day18/Day18")
    part1(input).println()
//    part2(input).println()
}

private fun String.toDirection(): Direction = when (this) {
    "R" -> Direction.EAST
    "U" -> Direction.NORTH
    "D" -> Direction.SOUTH
    "L" -> Direction.WEST
    else -> throw IllegalArgumentException("Unknoiwn direction string $this")
}

private fun List<Dig>.toPlot(): List<String> {
    val currentPosition = 0 to 0
    val filledSpots = this.fold(mutableListOf(currentPosition)) { acc, dig ->
        val last = acc.last()
        (1..dig.meters).forEach { meter ->
            acc.add(
                    when (dig.direction) {
                        Direction.NORTH -> last.first - meter to last.second
                        Direction.EAST -> last.first to last.second + meter
                        Direction.WEST -> last.first to last.second - meter
                        Direction.SOUTH -> last.first + meter to last.second
                    }
            )
        }
        acc
    }
    val (verticals, horizontals) = filledSpots.fold((0 to 0) to (0 to 0)) { acc, spot ->
        val minH = if (acc.first.first > spot.first) spot.first else acc.first.first
        val maxH = if (acc.first.second < spot.first) spot.first else acc.first.second
        val minV = if (acc.second.first > spot.second) spot.second else acc.second.first
        val maxV = if (acc.second.second < spot.second) spot.second else acc.second.second
        (minH to maxH) to (minV to maxV)
    }
    val field = List(verticals.second - verticals.first + 1) { StringBuilder(horizontals.second - horizontals.first + 1) }
    field.forEach { builder ->
        (0..<builder.capacity()).forEach { _ ->
            builder.append(".")
        }
    }
    filledSpots.forEach { spot ->
        val x = spot.first - verticals.first
        val y = spot.second - horizontals.first
        field[x][y] = '#'
    }
    return field.map { it.toString() }
}
