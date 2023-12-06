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
    val colourCounts = handfulsFromGame().flatMap { handful -> handful.colourCountsFromHandful() }
    return maxByColour.keys.map { colour ->
        colourCounts.filter { it.colour == colour }.maxOfOrNull { it.count }!!
    }.reduce { product, count -> product * count }
}

fun String.isPossibleGameString() = handfulsFromGame().all { handfulString -> handfulString.isPossibleHandfulString() }

// TODO: substringAfter?
private fun String.handfulsFromGame() =
    substring(startIndex = indexOf(':').takeIf { it != -1 }!! + 1).split(';')

fun String.isPossibleHandfulString() = colourCountsFromHandful().all { it.isPossible() }

private fun String.colourCountsFromHandful() =
    split(',')
        .map { rawColourCountString -> rawColourCountString.trim().split(' ').takeIf { it.size == 2 }!! }
        .map { (countString, colourString) -> ColourCount(colour = colourString, count = countString.toInt()) }

val maxByColour = mapOf("red" to 12, "green" to 13, "blue" to 14)

data class ColourCount(val colour: String, val count: Int)

fun ColourCount.isPossible() = count <= maxByColour[colour]!!
