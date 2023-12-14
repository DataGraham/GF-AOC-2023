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

fun part1(input: List<String>) = input
    .map { line ->
        line.split(' ').let { (cardsString, bidString) ->
            Hand(
                cards = cardsString.map(Char::cardValue),
                bid = bidString.toInt()
            )
        }
    }
    //.apply { joinToString(separator = "\n") { "$it of type ${it.type}" }.println() }
    .sorted()
    //.apply { joinToString(separator = "\n").println() }
    .mapIndexed { index, hand -> hand.bid * (index + 1) }
    .sum()

data class Hand(val cards: List<Int>, val bid: Int) : Comparable<Hand> {
    val cardFrequencies by lazy { cards.groupingBy { it }.eachCount().values }
    val type by lazy { HandType.entries.last { handType -> handType.isFoundInHand(this) } }
    @OptIn(ExperimentalStdlibApi::class)
    val relativeValue: Int by lazy {
        cards.joinToString(separator = "") { it.toHexString(hexNumberFormat) }.toInt(16)
    }

    override fun compareTo(other: Hand) =
        type.compareTo(other.type).takeIf { it != 0 }
            // TODO: Or zip the cards of the two hands and compare the first non-equal pair?
            ?: relativeValue.compareTo(other.relativeValue)

    companion object {
        @OptIn(ExperimentalStdlibApi::class)
        val hexNumberFormat = HexFormat {
            number { removeLeadingZeros = true }
        }
    }

}

private fun Char.cardValue() = digitToIntOrNull() ?: faceValues[this]!!

private val faceValues = mapOf(
    'T' to 10,
    'J' to 11,
    'Q' to 12,
    'K' to 13,
    'A' to 14
)

enum class HandType {
    HighCard {
        override fun isFoundInHand(hand: Hand) = true
    },
    OnePair {
        override fun isFoundInHand(hand: Hand) = hand.cardFrequencies.contains(2)
    },
    TwoPair {
        override fun isFoundInHand(hand: Hand) = hand.cardFrequencies.count { it == 2 } == 2
    },
    ThreeOfAKind {
        override fun isFoundInHand(hand: Hand) = hand.cardFrequencies.contains(3)
    },
    FullHouse {
        override fun isFoundInHand(hand: Hand) = hand.cardFrequencies.toSet() == setOf(3, 2)
    },
    FourOfAKind {
        override fun isFoundInHand(hand: Hand) = hand.cardFrequencies.contains(4)
    },
    FiveOfAKind {
        override fun isFoundInHand(hand: Hand) = hand.cardFrequencies.contains(5)
    };
    abstract fun isFoundInHand(hand: Hand): Boolean

}

fun part2(input: List<String>): Int {
    return input.size
}
