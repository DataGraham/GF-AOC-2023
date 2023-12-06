package day02

import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day02/Day02_test")
    check(part1(testInput) == 8)

    val input = readInput("day02/Day02")
    println("Part 1 Answer: ${part1(input)}")
    // println("Part 2 Answer: ${part2(input)}")
}

// TODO: Meassure performance difference for sequences?
fun part1(input: List<String>) = input.indices
    .filter { gameIndex -> input[gameIndex].isPossibleGameString() }
    .sumOf { gameIndex -> gameIndex + 1 }

fun String.isPossibleGameString() = handfulsFromGame().all { handfulString -> handfulString.isPossibleHandfulString() }

// TODO: substringAfter?
private fun String.handfulsFromGame() =
    substring(startIndex = indexOf(':').takeIf { it != -1 }!! + 1).split(';')

fun String.isPossibleHandfulString() =
    colourCountsFromHandful().all { colourCountString -> colourCountString.isPossibleColourCountString() }

private fun String.colourCountsFromHandful() = split(',').map(String::trim)

val maxByColour = mapOf("red" to 12, "green" to 13, "blue" to 14)

fun String.isPossibleColourCountString() =
    split(' ')
        .takeIf { it.size == 2 }!!
        .let { (countString, colourString) -> countString.toInt() <= maxByColour[colourString]!! }

fun part2(input: List<String>): Int {
    return input.size
}
