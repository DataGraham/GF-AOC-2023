package day07

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
            Hand(cardsString = cardsString, bid = bidString.toInt())
        }
    }
    //.apply { joinToString(separator = "\n") { "$it of type ${it.type}" }.println() }
    .sorted()
    //.apply { joinToString(separator = "\n").println() }
    .mapIndexed { index, hand -> hand.bid * (index + 1) }
    .sum()

data class Hand(val cardsString: String, val bid: Int) : Comparable<Hand> {
    val naturalCardValues by lazy { cardsString.map(Char::naturalCardValue) }
    val naturalFrequencies by lazy { naturalCardValues.groupingBy { it }.eachCount().values }
    val naturalType by lazy { HandType.entries.last { handType -> handType.isFoundInFrequencies(naturalFrequencies) } }

    val wildCardValues by lazy { cardsString.map(Char::wildCardValue) }
    /** Frequencies of cards if jokers act as the most populous non-joker rank */
    val wildFrequencies by lazy {
        val nonJokerFrequencies = cardsString.filter { it != 'J' }.groupingBy { it }.eachCount().values
        // TODO: I don't think this would handle a hand of all jokers
        val maxNonJokerFrequency = nonJokerFrequencies.max()
        val firstIndexOfMaxFrequency = nonJokerFrequencies.indexOf(maxNonJokerFrequency)
        val jokerCount = cardsString.count { it == 'J' }
        nonJokerFrequencies.mapIndexed { index, frequency ->
            if (index == firstIndexOfMaxFrequency) frequency + jokerCount
            else frequency
        }
    }
    val wildType by lazy { HandType.entries.last { handType -> handType.isFoundInFrequencies(wildFrequencies) } }

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
        // ?: relativeValue.compareTo(other.relativeValue)
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

fun part2(input: List<String>) = input
    .map { line ->
        line.split(' ').let { (cardsString, bidString) ->
            Hand(cardsString = cardsString, bid = bidString.toInt())
        }
    }
    //.apply { joinToString(separator = "\n") { "$it of type ${it.type}" }.println() }
    .sortedWith(Hand::compareToWild)
    //.apply { joinToString(separator = "\n").println() }
    .mapIndexed { index, hand -> hand.bid * (index + 1) }
    .sum()
