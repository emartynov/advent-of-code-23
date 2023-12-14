package day14

import println
import readInput


fun main() {
    fun tiltToNorth(strings: List<String>): List<String> =
            strings.foldIndexed(mutableListOf<String>()) { index, acc, _ ->
                if (index < strings.size - 1) {
                    val destination = StringBuilder(acc.removeLastOrNull() ?: strings.first())
                    val source = StringBuilder(strings[index + 1])
                    destination.forEachIndexed { charIndex, c ->
                        if (c == '.' && source[charIndex] == 'O') {
                            destination.replace(charIndex, charIndex + 1, "O")
                            source.replace(charIndex, charIndex + 1, ".")
                        }
                    }
                    acc.add(destination.toString())
                    acc.add(source.toString())
                }
                acc
            }

    fun tiltToSouth(strings: List<String>): List<String> =
            strings.foldRightIndexed(mutableListOf<String>()) { index, _, acc ->
                if (index > 0) {
                    val destination = StringBuilder(acc.removeFirstOrNull() ?: strings.last())
                    val source = StringBuilder(strings[index - 1])
                    destination.forEachIndexed { charIndex, c ->
                        if (c == '.' && source[charIndex] == 'O') {
                            destination.replace(charIndex, charIndex + 1, "O")
                            source.replace(charIndex, charIndex + 1, ".")
                        }
                    }
                    acc.add(0, destination.toString())
                    acc.add(0, source.toString())
                }
                acc
            }

    fun tiltToWest(strings: List<String>): List<String> = strings.map {
        val builder = StringBuilder(it)
        builder.forEachIndexed { index, c ->
            if (
                    index < it.length - 1 &&
                    c == '.' &&
                    builder[index + 1] == 'O'
            ) {
                builder.replace(index, index + 2, "O.")
            }
        }
        builder.toString()
    }

    fun tiltToEast(strings: List<String>): List<String> = strings.map {
        val builder = StringBuilder(it)
        builder
                .reverse()
                .forEachIndexed { index, c ->
                    if (
                            index < it.length - 1 &&
                            c == '.' &&
                            builder[index + 1] == 'O'
                    ) {
                        builder.replace(index, index + 2, "O.")
                    }
                }
        builder
                .reverse()
                .toString()
    }

    fun List<String>.tiltiToSide(tiltFunction: (List<String>) -> List<String>): List<String> {
        var prevResult = this
        var result: List<String> = tiltFunction(this)
        while (result != prevResult) {
            prevResult = result
            result = tiltFunction(result)
        }
        return result
    }

    fun part1(input: List<String>): Int {
        return input
                .tiltiToSide(::tiltToNorth)
                .foldRightIndexed(0) { index, stringValue, acc ->
                    acc + (input.size - index) * stringValue.count { it == 'O' }
                }
    }

    fun part2(input: List<String>): Int {
        return input
                .let {
                    val prevResults = mutableListOf<List<String>>()
                    prevResults.add(it)
                    var result: List<String> = it
                            .tiltiToSide(::tiltToNorth)
                            .tiltiToSide(::tiltToWest)
                            .tiltiToSide(::tiltToSouth)
                            .tiltiToSide(::tiltToEast)
                    for (i in 0..<1000000000) {
                        val index = prevResults.indexOf(result)
                        if (index > -1) {
                            result = prevResults[(1000000000 - i - 1) % (prevResults.size - index) + index]
                            break
                        }

                        prevResults.add(result)
                        result = result
                                .tiltiToSide(::tiltToNorth)
                                .tiltiToSide(::tiltToWest)
                                .tiltiToSide(::tiltToSouth)
                                .tiltiToSide(::tiltToEast)
                    }
                    result
                }
                .foldRightIndexed(0) { index, stringValue, acc ->
                    acc + (input.size - index) * stringValue.count { it == 'O' }
                }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day14/Day14_test")
    check(part1(testInput) == 136)
    check(part2(testInput) == 64)

    val input = readInput("day14/Day14")
    part1(input).println()
    part2(input).println()
}
