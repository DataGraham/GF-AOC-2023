package day02

import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day02/Day02_test")
    check(part1(testInput) == 8)
    check(part2(testInput) == 2286)

    val input = readInput("day02/Day02")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

// TODO: Measure performance difference for sequences?
fun part1(input: List<String>) = input.indices
    .filter { gameIndex -> input[gameIndex].isPossibleGameString() }
    .sumOf { gameIndex -> gameIndex + 1 }

fun part2(input: List<String>): Int = input.sumOf { gameString -> gameString.gamePower() }

private fun String.gamePower(): Int {
    val handfulColourCounts = handfulsFromGame().flatMap { handful -> handful.colourCountsFromHandful() }

    val requiredColourCounts: List<Int> =
    // maxByColour.keys.map { colour -> handfulColourCounts.filter { it.colour == colour }.maxOfOrNull { it.count }!! }
        // Don't assume what the set of possible colours is?
        handfulColourCounts.groupBy(
            keySelector = { colourCount -> colourCount.colour },
            valueTransform = { it.count }
        ).map { countsForThisColour -> countsForThisColour.value.max() }
    return requiredColourCounts.reduce { product, count -> product * count }
}

fun String.isPossibleGameString() = handfulsFromGame().all { handfulString -> handfulString.isPossibleHandfulString() }

// TODO: substringAfter?
private fun String.handfulsFromGame() =
    substring(startIndex = indexOf(':').takeIf { it != -1 }!! + 1).split(';')

fun String.isPossibleHandfulString() = colourCountsFromHandful().all { it.isPossible() }

private fun String.colourCountsFromHandful() =
    split(',')
        .map(String::trim)
        .map { rawColourCountString -> colourCountFromString(rawColourCountString) }

private fun colourCountFromString(rawColourCountString: String) =
    rawColourCountString.split(' ')
        .takeIf { it.size == 2 }!!
        .let { (countString, colourString) -> ColourCount(colour = colourString, count = countString.toInt()) }

val maxByColour = mapOf("red" to 12, "green" to 13, "blue" to 14)

data class ColourCount(val colour: String, val count: Int)

fun ColourCount.isPossible() = count <= maxByColour[colour]!!
