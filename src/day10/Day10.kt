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

        return mapOfDistances
                .values
                .max()
    }

    fun part2(input: List<String>): Int {
        val mapUnlocked = mutableMapOf<Pair<Int, Int>, Char>()
        val candidates = mutableListOf<Pair<Int, Int>>()

        input.first().mapIndexed { index, char ->
            val coordinates = 0 to index
            if (char == '.') {
                mapUnlocked[coordinates] = 'O'
                candidates.add(coordinates.first + 1 to coordinates.second)
            } else {
                mapUnlocked[coordinates] = 'X'
            }
        }

        input.last().mapIndexed { index, char ->
            val coordinates = input.size - 1 to index
            if (char == '.') {
                mapUnlocked[coordinates] = 'O'
                candidates.add(coordinates.first - 1 to coordinates.second)
            } else {
                mapUnlocked[coordinates] = 'X'
            }
        }

        input.forEachIndexed { index, row ->
            val coordinatesFirst = index to 0
            val coordinatesLast = index to row.length - 1
            if (row.first() == '.') {
                mapUnlocked[coordinatesFirst] = 'O'
                candidates.add(coordinatesFirst.first to coordinatesFirst.second + 1)
            } else {
                mapUnlocked[coordinatesFirst] = 'X'
            }
            if (row.last() == '.') {
                mapUnlocked[coordinatesLast] = 'O'
                candidates.add(coordinatesLast.first to coordinatesLast.second - 1)
            } else {
                mapUnlocked[coordinatesFirst] = 'X'
            }
        }

        while (candidates.isNotEmpty()) {
            val point = candidates.removeFirst()
            val char = input[point.first][point.second]
            if (char == '.') {
                mapUnlocked[point] = 'O'
                candidates.addAll(
                        listOf(
                                (point.first - 1 to point.second),
                                (point.first + 1 to point.second),
                                (point.first to point.second - 1),
                                (point.first to point.second + 1),
                        )
                                .filter {
                                    it.first > -1 &&
                                            it.first < input.size &&
                                            it.second > -1 &&
                                            it.second < input.first().length &&
                                            mapUnlocked[it] == null
                                }
                )
            } else {
                mapUnlocked[point] = 'X'
            }
        }

        return input.mapIndexed { rowIndex, row ->
            row.mapIndexed { columnIndex, char ->
                if (char == '.' && mapUnlocked[rowIndex to columnIndex] == null) 1 else 0
            }
        }
                .flatten()
                .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("day10/Day10_test1")
    check(part1(testInput1) == 4)
    val testInput2 = readInput("day10/Day10_test2")
    check(part1(testInput2) == 8)
    val testInput4 = readInput("day10/Day10_test4")
    check(part2(testInput4) == 4)
    val testInput3 = readInput("day10/Day10_test3")
    check(part2(testInput3) == 4)
    val testInput5 = readInput("day10/Day10_test5")
//    check(part2(testInput5) == 8)
    val testInput6 = readInput("day10/Day10_test6")
//    check(part2(testInput6) == 10)

    val input = readInput("day10/Day10")
    part1(input).println()
    part2(input).println()
}
