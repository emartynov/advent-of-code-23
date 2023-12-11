package day10

import println
import readInput


fun main() {
    fun List<String>.findStart(): Pair<Int, Int> {
        forEachIndexed { rowIndex, row ->
            val index = row.indexOf("S")
            if (index > -1) {
                return rowIndex to index
            }
        }
        return -1 to -1
    }

    fun List<String>.calculateDistances(start: Pair<Int, Int>, distances: MutableMap<Pair<Int, Int>, Int>) {
        val candidates = mutableListOf(start)
        while (candidates.isNotEmpty()) {
            val point = candidates.removeFirst()
            val pipeSymbol = get(point.second)[point.first]
            val nextDistance = distances.getValue(point) + 1
            val nextPoints = when (pipeSymbol) {
                'S' -> {
                    val pointsFromStart = mutableListOf<Pair<Int, Int>>()
                    if (point.first > 0) {
                        val symbol = get(point.second)[point.first - 1]
                        if (symbol in listOf('-', 'F')) {
                            pointsFromStart.add(point.first - 1 to point.second)
                        }
                    }
                    if (point.first < first().length - 1) {
                        val symbol = get(point.second)[point.first + 1]
                        if (symbol in listOf('-', '7')) {
                            pointsFromStart.add(point.first + 1 to point.second)
                        }
                    }
                    if (point.second > 0) {
                        val symbol = get(point.second - 1)[point.first]
                        if (symbol in listOf('|', '7', 'F')) {
                            pointsFromStart.add(point.first to point.second - 1)
                        }
                    }
                    if (point.second < size - 1) {
                        val symbol = get(point.second + 1)[point.first]
                        if (symbol in listOf('|', 'L', 'J')) {
                            pointsFromStart.add(point.first to point.second + 1)
                        }
                    }
                    pointsFromStart
                }

                'F' -> {
                    listOf((point.first + 1 to point.second), (point.first to point.second + 1))
                }

                'L' -> {
                    listOf((point.first + 1 to point.second), (point.first to point.second - 1))
                }

                '7' -> {
                    listOf((point.first - 1 to point.second), (point.first to point.second + 1))
                }

                'J' -> {
                    listOf((point.first - 1 to point.second), (point.first to point.second - 1))
                }

                '|' -> {
                    listOf((point.first to point.second - 1), (point.first to point.second + 1))
                }

                '-' -> {
                    listOf((point.first - 1 to point.second), (point.first + 1 to point.second))
                }

                else -> emptyList()
            }
                    .filter {
                        it.first > -1 &&
                                it.first < first().length &&
                                it.second > -1 &&
                                it.second < size
                    }
            nextPoints.forEach { nextPoint ->
                if (distances[nextPoint] == null) {
                    candidates.add(nextPoint)
                    distances[nextPoint] = nextDistance
                }
            }
        }
    }

    fun part1(input: List<String>): Int {
        val start = input.findStart()

        val mapOfDistances = mutableMapOf<Pair<Int, Int>, Int>()
        mapOfDistances[start] = 0

        input.calculateDistances(start, mapOfDistances)

//        mapOfDistances.also(::println)

        return mapOfDistances
                .values
                .max()
    }

    fun part2(input: List<String>): Int {
        return input.size
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("day10/Day10_test1")
    check(part1(testInput1) == 4)
    val testInput2 = readInput("day10/Day10_test2")
    check(part1(testInput2) == 8)
//    check(part2(testInput) == 2)

    val input = readInput("day10/Day10")
    part1(input).println()
//    part2(input).println()
}
