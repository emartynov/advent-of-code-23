package day07

import println
import readInput

private enum class HandType {
    HIGH_CARD,
    ONE_PAIR,
    TWO_PAIRS,
    THREE_OF_KIND,
    FULL_HOUSE,
    FOUR_OF_KIND,
    FIVE_OF_KIND
}

fun main() {
    fun part1(input: List<String>): Int {
        return input
                .parseHandsWithBids()
                .map { (hand, bid) ->
                    Triple(hand.getType(), hand, bid)
                }
                .sortedWith { a, b ->
                    val firstDifferentCardDifference = a.second
                            .mapIndexed { index, c ->
                                c.asWeight() - b.second[index].asWeight()
                            }
                            .firstOrNull { it != 0 }
                            ?: 0
                    31 * (a.first.ordinal - b.first.ordinal) + firstDifferentCardDifference
                }
                .foldIndexed(0) { index, acc, tripple ->
                    acc + (index + 1) * tripple.third
                }
    }

    fun part2(input: List<String>): Int {
        return input
                .parseHandsWithBids()
                .map { (hand, bid) ->
                    Triple(hand.getTypeWithJoker(), hand, bid)
                }
                .sortedWith { a, b ->
                    val firstDifferentCardDifference = a.second
                            .mapIndexed { index, c ->
                                c.asWeightWithJoker() - b.second[index].asWeightWithJoker()
                            }
                            .firstOrNull { it != 0 }
                            ?: 0
                    31 * (a.first.ordinal - b.first.ordinal) + firstDifferentCardDifference
                }
                .foldIndexed(0) { index, acc, tripple ->
                    acc + (index + 1) * tripple.third
                }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day07/Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("day07/Day07")
    part1(input).println()
    part2(input).println()
}

private fun Char.asWeight(): Int = when {
    isDigit() -> this - '0'
    this == 'T' -> 10
    this == 'J' -> 11
    this == 'Q' -> 12
    this == 'K' -> 13
    this == 'A' -> 14
    else -> 0
}

private fun Char.asWeightWithJoker(): Int = if (this == 'J') 1 else asWeight()

private fun String.getType(): HandType = groupBy { it }
        .let { labels ->
            val maxSameCardsLength = labels.values.maxOfOrNull { it.size }
            when {
                labels.size == 1 -> HandType.FIVE_OF_KIND
                labels.size == 2 && maxSameCardsLength == 4 -> HandType.FOUR_OF_KIND
                labels.size == 2 && maxSameCardsLength == 3 -> HandType.FULL_HOUSE
                labels.size == 3 && maxSameCardsLength == 3 -> HandType.THREE_OF_KIND
                labels.size == 3 && maxSameCardsLength == 2 -> HandType.TWO_PAIRS
                labels.size == 4 -> HandType.ONE_PAIR
                else -> HandType.HIGH_CARD
            }
        }

private fun String.getTypeWithJoker(): HandType = groupBy { it }
        .let { labels ->
            val maxSameCardsLengthNotJoker = labels
                    .filter { it.key != 'J' }
                    .values
                    .maxOfOrNull { it.size } ?: 0
            val lengthOfJoker = labels['J']?.size ?: 0
            when {
                labels.size == 1 -> HandType.FIVE_OF_KIND
                lengthOfJoker == 0 -> {
                    when {
                        labels.size == 2 && maxSameCardsLengthNotJoker == 4 -> HandType.FOUR_OF_KIND
                        labels.size == 2 && maxSameCardsLengthNotJoker == 3 -> HandType.FULL_HOUSE
                        labels.size == 3 && maxSameCardsLengthNotJoker == 3 -> HandType.THREE_OF_KIND
                        labels.size == 3 && maxSameCardsLengthNotJoker == 2 -> HandType.TWO_PAIRS
                        labels.size == 4 -> HandType.ONE_PAIR
                        else -> HandType.HIGH_CARD
                    }
                }

                labels.size == 2 -> HandType.FIVE_OF_KIND
                labels.size == 3 && lengthOfJoker == 1 && maxSameCardsLengthNotJoker == 2 -> HandType.FULL_HOUSE
                labels.size == 3 -> HandType.FOUR_OF_KIND
                labels.size == 4 -> HandType.THREE_OF_KIND
                else -> HandType.ONE_PAIR
            }
        }

private fun List<String>.parseHandsWithBids(): List<Pair<String, Int>> {
    return map {
        val (hand, bidString) = it.split(" ")
        hand to bidString.trim().toInt()
    }
}
