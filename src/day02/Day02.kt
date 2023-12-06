package day02

import println
import readInput

fun main() {

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day02/Day02_test")
    check(part1(testInput) == 8)

    val input = readInput("day02/Day02")
    part1(input).println()
    part2(input).println()
}

fun part1(input: List<String>) = input.indices
    .filter { gameIndex -> input[gameIndex].isPossibleGameString() }
    .sumOf { gameIndex -> gameIndex + 1 }

fun String.isPossibleGameString() =
    // TODO: substringAfter?
    substring(startIndex = indexOf(':').takeIf { it != -1 }!! + 1)
        .split(';')
        .all { handfulString -> handfulString.isPossibleHandfulString() }

fun String.isPossibleHandfulString() =
    split(',')
        .map(String::trim)
        .all { colourCountString -> colourCountString.isPossibleColourCountString() }

val maxByColour = mapOf("red" to 12, "green" to 13, "blue" to 14)

fun String.isPossibleColourCountString() =
    split(' ')
        .takeIf { it.size == 2 }!!
        .let { (countString, colourString) ->
            (countString.toInt() <= maxByColour[colourString]!!).also {
                println("Handful string $this with $countString $colourString is ${if (it) "possible" else "IMPOSSIBLE"}")
            }
        }

fun part2(input: List<String>): Int {
    return input.size
}
