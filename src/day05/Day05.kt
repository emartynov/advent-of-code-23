package day05

import println
import readInput
import kotlin.math.max

private data class AgricultureMap(
        val maps: List<Pair<LongRange, LongRange>> = emptyList()
) {
    fun mapKey(key: Long): Long {
        val conainingMapIndex = maps.indexOfFirst { key in it.first }
        return if (conainingMapIndex != -1) {
            val mapping = maps[conainingMapIndex]
            mapping.second.first + (key - mapping.first.first)
        } else {
            key
        }
    }
}

private enum class AgricultureMapType(val key: String) {
    Seed2Soil("seed-to-soil map:"),
    Solil2Fertilizer("soil-to-fertilizer map:"),
    FertilizerToWater("fertilizer-to-water map:"),
    Water2Light("water-to-light map:"),
    Light2Temperature("light-to-temperature map:"),
    Temperature2Humidity("temperature-to-humidity map:"),
    Humidity2Location("humidity-to-location map:")
}

fun main() {
    fun part1(input: List<String>): Long {
        return input
                .parseMaps()
                .let { maps ->
                    val seeds = input
                            .first()
                            .substringAfter(": ")
                            .split(" ")
                            .asSequence()
                            .filter { it.isNotBlank() }
                            .map { it.toLong() }
                            .toList()

                    seeds.map { seed ->
                        AgricultureMapType
                                .entries
                                .fold(seed) { key, type ->
                                    val map = maps.getValue(type)
                                    map.mapKey(key)
                                }
                    }
                }
                .min()
    }

    fun part2(input: List<String>): Long {
        return input
                .parseMaps()
                .let { maps ->
                    val seeds = input
                            .first()
                            .substringAfter(": ")
                            .split(" ")
                            .asSequence()
                            .filter { it.isNotBlank() }
                            .chunked(2)
                            .map { numberList ->
                                val start = numberList.first().toLong()
                                val range = numberList.last().toLong()
                                start..start + range
                            }
                            .toList()

                    seeds.map { seed ->
                        AgricultureMapType
                                .entries
                                .fold(listOf(seed)) { keyRanges, type ->
                                    val agriMap = maps.getValue(type)
                                }
                    }
                            .flatten()
                }
                .also(::println)
                .minOf { it.first }
                .also(::println)
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day05/Day05_test")
    check(part1(testInput) == 35L)
    check(part2(testInput) == 46L)

    val input = readInput("day05/Day05")
    part1(input).println()
//    part2(input).println()
}

private fun List<String>.parseMaps(): Map<AgricultureMapType, AgricultureMap> {
    var currentType = AgricultureMapType.Seed2Soil
    val typesToString = AgricultureMapType
            .entries
            .associateWith { mutableListOf<String>() }
    subList(1, size)
            .forEach { inputString: String ->
                if (inputString.isNotBlank()) {
                    if (inputString.contains("map:")) {
                        currentType = AgricultureMapType.entries.first { it.key == inputString }
                    } else {
                        typesToString.getValue(currentType).add(inputString)
                    }
                }
            }
    return typesToString
            .mapValues { (_, stringList) ->
                AgricultureMap(
                        maps = stringList.map { rangeString ->
                            val (destination, source, range) = rangeString
                                    .split(" ")
                                    .map { it.toLong() }
                            source..source + range to destination..destination + range
                        }
                )
            }
}

private fun LongRange.intersectWith(other: LongRange) =
        maxOf(first, other.first)..minOf(last, other.last)

private fun LongRange.substractWith(other: LongRange): List<LongRange> {
    val intersectRange = intersectWith(other)
    return when {
        intersectRange.isEmpty() -> {
            listOf(this)
        }

        contains(other.first) && contains(other.last) -> {
            listOf((first..<intersectRange.first), (intersectRange.last + 1..last))
        }

        intersectRange.last < last -> {
            listOf(intersectRange.last + 1..last)
        }

        else -> {
            listOf(first..<intersectRange.first)
        }
    }
            .filter { !it.isEmpty() }
}
