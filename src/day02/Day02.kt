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

fun part1(input: List<String>): Int {
    // TODO: sum of filtered indices instead?
    return input.foldIndexed(0) { i, acc, line ->
        acc + if (line.isPossibleGameString()) i + 1 else 0
    }
}

fun String.isPossibleGameString() =
    substring(startIndex = indexOf(':').takeIf { it != -1 }!! + 1)
        .split(';')
        .flatMap { it.split(',') }
        .map { it.trim() }
        .all { handful -> handful.isPossibleHandfulString() }

val maxByColour = mapOf("red" to 12, "green" to 13, "blue" to 14)

fun String.isPossibleHandfulString(): Boolean {
    val (countString, colourString) = split(' ')
        .takeIf { it.size == 2 }!!
        .let { it.first() to it.last() }
    return (countString.toInt() <= maxByColour[colourString]!!).also {
        println("Handful string $this with $countString $colourString is ${if (it) "possible" else "IMPOSSIBLE"}")
    }
}

fun part2(input: List<String>): Int {
    return input.size
}
