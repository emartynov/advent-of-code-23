package day17

import readInput

private enum class Direction {
    NORTH,
    EAST,
    WEST,
    SOUTH;


    fun isOpposite(direction: Direction): Boolean = when (this) {
        NORTH -> direction == Direction.SOUTH
        EAST -> direction == Direction.WEST
        WEST -> direction == Direction.EAST
        SOUTH -> direction == Direction.NORTH
    }
}

fun main() {
    fun Char.asInt() = this - '0'

    fun part1(input: List<String>): Int {
        var finalHeatLoses = Int.MAX_VALUE
        val numberOfVisits = mutableMapOf<Pair<Int, Int>, Int>()
        numberOfVisits[0 to 0] = 1
        val nextPotential = mutableListOf(
                Triple(0 to 1, listOf(Direction.EAST), 2),
                Triple(1 to 0, listOf(Direction.SOUTH), 2)
        )
        while (nextPotential.isNotEmpty()) {
            val point = nextPotential.removeFirst()
            val directionFrom = point.second.last()
            val pointHeatLose = input[point.first.first][point.first.second].asInt() + point.third
            if (
                    point.first.first == input.size - 1 &&
                    point.first.second == input.first().length - 1 &&
                    pointHeatLose < finalHeatLoses
            ) {
                finalHeatLoses = pointHeatLose
            }
            val numberOfPrevVisits = numberOfVisits.getOrDefault(point.first.first to point.first.second, 0)
            if (
                    pointHeatLose < finalHeatLoses &&
                    numberOfPrevVisits < 4
            ) {
                numberOfVisits[point.first.first to point.first.second] = numberOfPrevVisits + 1
                nextPotential.addAll(
                        listOf(
                                Triple(
                                        point.first.first - 1 to point.first.second,
                                        if (directionFrom == Direction.NORTH) point.second + Direction.NORTH else listOf(Direction.NORTH),
                                        pointHeatLose
                                ),
                                Triple(
                                        point.first.first + 1 to point.first.second,
                                        if (directionFrom == Direction.SOUTH) point.second + Direction.SOUTH else listOf(Direction.SOUTH),
                                        pointHeatLose
                                ),
                                Triple(
                                        point.first.first to point.first.second + 1,
                                        if (directionFrom == Direction.EAST) point.second + Direction.EAST else listOf(Direction.EAST),
                                        pointHeatLose
                                ),
                                Triple(
                                        point.first.first to point.first.second - 1,
                                        if (directionFrom == Direction.WEST) point.second + Direction.WEST else listOf(Direction.WEST),
                                        pointHeatLose
                                ),
                        ).filter { pointToFilter ->
                            !directionFrom.isOpposite(pointToFilter.second.last()) &&
                                    pointToFilter.second.size <= 3 &&
                                    pointToFilter.first.first != -1 &&
                                    pointToFilter.first.second != -1 &&
                                    pointToFilter.first.first != input.size &&
                                    pointToFilter.first.second != input.first().length
                        }
                )
            }
        }
        println(finalHeatLoses)
        return finalHeatLoses
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day17/Day17_test")
    check(part1(testInput) == 102)
//    check(part2(testInput) == 145)

    val input = readInput("day17/Day17")
//    part1(input).println()
//    part2(input).println()
}
