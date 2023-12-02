package day02

import println
import readInput

data class Game(
    val id: Int,
    val subsets: List<Subset>
)

data class Subset(
    val red: Int,
    val blue: Int,
    val green: Int
) {
    fun isPossible() = red <= bag.red && green <= bag.green && blue <= bag.blue
}

val bag = Subset(
    red = 12,
    green = 13,
    blue = 14
)

fun main() {
    fun List<String>.parseGames() = map { gameString ->
        val (gamePart, subsetsPart) = gameString.split(":")
        val gameId = gamePart
            .split(" ")
            .last()
            .toInt()
        val parts = subsetsPart
            .split(";")
            .map { subsetString ->
                val colorMap = subsetString
                    .split(",")
                    .associate { colorString ->
                        val (numberSting, colorPartString) = colorString
                            .trim()
                            .split(" ")
                        colorPartString to numberSting.toInt()
                    }
                Subset(
                    red = colorMap.getOrDefault("red", 0),
                    green = colorMap.getOrDefault("green", 0),
                    blue = colorMap.getOrDefault("blue", 0)
                )
            }
        Game(
            id = gameId,
            subsets = parts
        )
    }

    fun part1(input: List<String>): Int {
        return input
            .parseGames()
            .sumOf { game ->
                if (game.subsets.all { it.isPossible() }) game.id else 0
            }
    }

    fun part2(input: List<String>): Int {
        return input
            .parseGames()
            .map { game ->
                game.subsets
                    .fold(Subset(0, 0, 0)) { acc, subset ->
                        Subset(
                            red = maxOf(acc.red, subset.red),
                            green = maxOf(acc.green, subset.green),
                            blue = maxOf(acc.blue, subset.blue),
                        )
                    }
            }.sumOf { subset ->
                subset.red * subset.blue * subset.green
            }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day02/Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("day02/Day02")
    part1(input).println()
    part2(input).println()
}
