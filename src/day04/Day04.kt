package day04

import println
import readInput
import kotlin.math.pow

fun main() {
    fun List<String>.parseCards() = map { cardString ->
        val (cardNumberString, numbersString) = cardString.split(":")
        val cardNumber = cardNumberString
            .substringAfter("Card")
            .trim()
            .toInt()
        val (winNumberString, scratchedNumbersString) = numbersString
            .split("|")
        val winNumbers = winNumberString
            .windowed(3, 3)
            .mapNotNull { it.trim().toIntOrNull() }
            .toSet()
        val scratchedNumbers = scratchedNumbersString
            .windowed(3, 3)
            .mapNotNull { it.trim().toIntOrNull() }
            .toSet()
        Triple(cardNumber, winNumbers, scratchedNumbers)
    }

    fun part1(input: List<String>): Int {
        return input
            .parseCards()
            .sumOf { (_, winNumbers, scratchNumbers) ->
                val guessedCorrectlyNumbers = scratchNumbers.intersect(winNumbers)
                if (guessedCorrectlyNumbers.isEmpty()) 0 else 2.0.pow(guessedCorrectlyNumbers.size - 1.0).toInt()
            }
    }

    fun part2(input: List<String>): Int {
        val parsedCards = input.parseCards()
        val accumulatedMap = parsedCards
            .associate {
                it.first to 1
            }.toMutableMap()
        return parsedCards
            .fold(accumulatedMap) { map, card ->
                val (cardNumber, winNumbers, scratchNumbers) = card
                val guessedCorrectlyNumbers = scratchNumbers.intersect(winNumbers).size
                (cardNumber + 1..cardNumber + guessedCorrectlyNumbers)
                    .filter { accumulatedMap.contains(it) }
                    .forEach { wonCardNumber ->
                        map[wonCardNumber] = map[wonCardNumber]!! + map[cardNumber]!!
                    }
                map
            }
            .values
            .sum()
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day04/Day04_test")
    check(part1(testInput) == 13)
    check(part2(testInput) == 30)

    val input = readInput("day04/Day04")
    part1(input).println()
    part2(input).println()
}
