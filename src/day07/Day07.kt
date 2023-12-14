package day07

import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day07/Day07_test")
    check(part1(testInput) == 6440)
    //check(part2(testInput) == 1)

    val input = readInput("day07/Day07")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val hands = input.map { line ->
        line.split(' ').let { (cardsString, bidString) ->
            Hand(
                cards = cardsString.map(Char::cardValue),
                bid = bidString.toInt()
            )
        }
    }.apply { joinToString(separator = "\n") { "$it of type ${it.cards.handType()}" }.println() }
    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}

data class Hand(val cards: List<Int>, val bid: Int)

private fun Char.cardValue() = digitToIntOrNull() ?: faceValues[this]!!

private val faceValues = mapOf(
    'T' to 10,
    'J' to 11,
    'Q' to 12,
    'K' to 13,
    'A' to 14
)

enum class HandType {
    FiveOfAKind {
        override fun isFoundInCards(cards: List<Int>) = cards.frequencies().contains(5)
    },
    FourOfAKind {
        override fun isFoundInCards(cards: List<Int>) = cards.frequencies().contains(4)
    },
    FullHouse {
        override fun isFoundInCards(cards: List<Int>) = cards.frequencies().toSet() == setOf(3, 2)
    },
    ThreeOfAKind {
        override fun isFoundInCards(cards: List<Int>) = cards.frequencies().contains(3)
    },
    TwoPair {
        override fun isFoundInCards(cards: List<Int>) = cards.frequencies().count { it == 2 } == 2
    },
    OnePair {
        override fun isFoundInCards(cards: List<Int>) = cards.frequencies().contains(2)
    },
    HighCard {
        override fun isFoundInCards(cards: List<Int>) = true
    };

    abstract fun isFoundInCards(cards: List<Int>): Boolean
}

// TODO: cache (lazy?) frequencies in a Hand object
private fun List<Int>.frequencies() = groupingBy { it }.eachCount().values

private fun List<Int>.handType() = HandType.entries.first { handType -> handType.isFoundInCards(this) }