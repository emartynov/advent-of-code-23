package day12

import println
import readInput


fun main() {

    fun List<String>.parseSprings(): List<Pair<String, List<Int>>> {
        return map { lineString ->
            val (springsString, numbersString) = lineString.split(" ")
            springsString to numbersString
                    .split(",")
                    .map { it.toInt() }
        }
    }

    fun String.validate(springs: List<Int>): Boolean {
        val count = foldIndexed(mutableListOf<IntRange>()) { index, acc, char ->
            val last = acc.removeLastOrNull()
            val lastItems =
                    if (char == '#') {
                        if (last == null) {
                            listOf(index..-1)
                        } else if (!last.isEmpty()) {
                            listOf(last, index..-1)
                        } else {
                            listOf(last)
                        }
                    } else {
                        if (last == null) {
                            listOf(null)
                        } else if (last.isEmpty()) {
                            listOf(last.first..<index)
                        } else {
                            listOf(last)
                        }
                    }
            acc.addAll(lastItems.filterNotNull())
            acc
        }
                .map { range ->
                    val last = if (range.last < 0) length - 1 else range.last
                    last - range.first + 1
                }
        return count == springs
    }

    fun countPossibilities(row: Pair<String, List<Int>>): Int {
        val indexOfPossible = row.first.indexOfFirst { it == '?' }
        return if (indexOfPossible == -1) {
            if (row.first.validate(row.second)) 1 else 0
        } else {
            countPossibilities(row.first.replaceFirst('?', '#') to row.second) +
                    countPossibilities(row.first.replaceFirst('?', '.') to row.second)
        }
    }

    fun List<String>.unfold(): List<String> {
        return map { lineString ->
            val (springsString, numbersString) = lineString.split(" ")
            List(5) { springsString }.joinToString("?") +
                    " " +
                    List(5) { numbersString }.joinToString(",")
        }
    }

    fun part1(input: List<String>): Int {
        return input
                .parseSprings()
                .sumOf { row ->
                    countPossibilities(row)
                }
    }

    fun part2(input: List<String>): Int {
        return part1(input.unfold())
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day12/Day12_test")
    check(part1(testInput) == 21)
    check(part2(testInput) == 525152)

    val input = readInput("day12/Day12")
    part1(input).println()
//    part2(input).println()
}
