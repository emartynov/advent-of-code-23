package day08

import println
import readInput

private data class Node(
        val map: Map<Direction, String>
)

private enum class Direction {
    LEFT,
    RIGHT
}

fun main() {
    fun List<String>.parseRouteAndMap(): Pair<List<Direction>, Map<String, Map<Direction, String>>> {
        val path = first()
                .trim()
                .map { if (it == 'L') Direction.LEFT else Direction.RIGHT }

        val nodes = subList(2, size)
                .associate { value ->
                    val (node, mapString) = value
                            .split(" = ")
                    val map = mapString
                            .substring(1, mapString.length - 1)
                            .split(",")
                            .map { it.trim() }
                            .mapIndexed { index, nodeValue ->
                                val key = if (index == 0) Direction.LEFT else Direction.RIGHT
                                key to nodeValue
                            }.toMap()
                    node to map
                }

        return path to nodes
    }

    fun getNumberOfSteps(startNode: String, route: List<Direction>, map: Map<String, Map<Direction, String>>, isEndNode: (String) -> Boolean): Int {
        var currentNode = startNode

        var numberOfSteps = 0
        var currentDirectionIndex = 0

        while (!isEndNode(currentNode)) {
            val mapNode = map.getValue(currentNode)
            currentNode = mapNode.getValue(route[currentDirectionIndex])
            currentDirectionIndex += 1
            if (currentDirectionIndex == route.size) {
                currentDirectionIndex = 0
            }
            numberOfSteps += 1
        }

        return numberOfSteps
    }

    fun part1(input: List<String>): Int {
        val (route, map) = input.parseRouteAndMap()

        return getNumberOfSteps("AAA", route, map) { it == "ZZZ" }
    }

    fun part2(input: List<String>): Long {
        val (route, map) = input.parseRouteAndMap()

        var currentNodes = map.keys
                .filter { it.endsWith('A') }

        val numberOfStepsForEach = currentNodes.map { startNode ->
            getNumberOfSteps(startNode, route, map) { it.endsWith('Z') }
        }

        return numberOfStepsForEach.fold(1L) { acc, i -> lcm(acc, i.toLong()) }
    }

    // test if implementation meets criteria from the description, like:
    val testInput1 = readInput("day08/Day08_test_1")
    check(part1(testInput1) == 2)
    val testInput2 = readInput("day08/Day08_test_2")
    check(part1(testInput2) == 6)
    val testInput3 = readInput("day08/Day08_test_3")
    check(part2(testInput3) == 6L)

    val input = readInput("day08/Day08")
    part1(input).println()
    part2(input).println()
}

private fun gcd(a: Long, b: Long): Long {
    if (b == 0L) return a
    return gcd(b, a % b)
}

private fun lcm(a: Long, b: Long): Long {
    return a * (b / gcd(a, b))
}
