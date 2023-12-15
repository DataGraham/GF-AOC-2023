package day07

import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day07/Day07_test")
    check(part1(testInput) == 6440)
    check(part2(testInput) == 5905)

    val input = readInput("day07/Day07")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) = calculateWinnings(input, Comparator.naturalOrder())
fun part2(input: List<String>) = calculateWinnings(input, Hand::compareToWild)

fun calculateWinnings(input: List<String>, handRanker: Comparator<Hand>) =
    input
        .map { line ->
            line.split(' ').let { (cardsString, bidString) ->
                Hand(cardsString = cardsString, bid = bidString.toInt())
            }
        }
        .sortedWith(handRanker)
        .mapIndexed { index, hand -> hand.bid * (index + 1) }
        .sum()

data class Hand(val cardsString: String, val bid: Int) : Comparable<Hand> {
    private val naturalCardValues by lazy { cardsString.map(Char::naturalCardValue) }
    private val naturalFrequencies by lazy { naturalCardValues.groupingBy { it }.eachCount().values }
    private val naturalType by lazy {
        HandType.entries.last { handType -> handType.isFoundInFrequencies(naturalFrequencies) }
    }
    private val wildCardValues by lazy { cardsString.map(Char::wildCardValue) }

    /** Frequencies of cards if jokers act as the most populous non-joker rank */
    private val wildFrequencies by lazy {
        val nonJokerFrequencies = cardsString.filter { it != 'J' }.groupingBy { it }.eachCount().values
            .takeIf { it.isNotEmpty() } ?: listOf(0)
        val maxNonJokerFrequency = nonJokerFrequencies.max()
        val firstIndexOfMaxFrequency = nonJokerFrequencies.indexOf(maxNonJokerFrequency)
        val jokerCount = cardsString.count { it == 'J' }
        nonJokerFrequencies.mapIndexed { index, frequency ->
            if (index == firstIndexOfMaxFrequency) frequency + jokerCount
            else frequency
        }
    }
    private val wildType by lazy {
        HandType.entries.last { handType -> handType.isFoundInFrequencies(wildFrequencies) }
    }

    @OptIn(ExperimentalStdlibApi::class)
    val relativeValue: Int by lazy {
        naturalCardValues.joinToString(separator = "") { it.toHexString(hexNumberFormat) }.toInt(16)
    }

    override fun compareTo(other: Hand) =
        naturalType.compareTo(other.naturalType).takeIf { it != 0 }
        // ?: relativeValue.compareTo(other.relativeValue)
            ?: naturalCardValues.zip(other.naturalCardValues).firstNotNullOfOrNull { (thisCard, otherCard) ->
                thisCard.compareTo(otherCard).takeIf { cardComparison -> cardComparison != 0 }
            } ?: 0

    fun compareToWild(other: Hand) =
        wildType.compareTo(other.wildType).takeIf { it != 0 }
            ?: wildCardValues.zip(other.wildCardValues).firstNotNullOfOrNull { (thisCard, otherCard) ->
                thisCard.compareTo(otherCard).takeIf { cardComparison -> cardComparison != 0 }
            } ?: 0

    companion object {
        @OptIn(ExperimentalStdlibApi::class)
        val hexNumberFormat = HexFormat {
            number { removeLeadingZeros = true }
        }
    }
}

private fun Char.naturalCardValue() = digitToIntOrNull() ?: naturalFaceValues[this]!!
private fun Char.wildCardValue() = digitToIntOrNull() ?: wildFaceValues[this]!!

private val naturalFaceValues = mapOf(
    'T' to 10,
    'J' to 11,
    'Q' to 12,
    'K' to 13,
    'A' to 14
)

private val wildFaceValues = naturalFaceValues + ('J' to 1)

enum class HandType {
    HighCard {
        override fun isFoundInFrequencies(frequencies: Collection<Int>): Boolean = true
    },
    OnePair {
        override fun isFoundInFrequencies(frequencies: Collection<Int>): Boolean = frequencies.contains(2)
    },
    TwoPair {
        override fun isFoundInFrequencies(frequencies: Collection<Int>): Boolean = frequencies.count { it == 2 } == 2
    },
    ThreeOfAKind {
        override fun isFoundInFrequencies(frequencies: Collection<Int>): Boolean = frequencies.contains(3)
    },
    FullHouse {
        override fun isFoundInFrequencies(frequencies: Collection<Int>): Boolean = frequencies.toSet() == setOf(3, 2)
    },
    FourOfAKind {
        override fun isFoundInFrequencies(frequencies: Collection<Int>): Boolean = frequencies.contains(4)
    },
    FiveOfAKind {
        override fun isFoundInFrequencies(frequencies: Collection<Int>): Boolean = frequencies.contains(5)
    };

    abstract fun isFoundInFrequencies(frequencies: Collection<Int>): Boolean
}
