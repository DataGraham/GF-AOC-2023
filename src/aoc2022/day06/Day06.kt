package aoc2022.day06

import measurePerformance
import println
import readInput
import subString

private const val START_MARKER_LENGTH = 4

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day06/Day06_test")
    check(part1SubstringSets(testInput.first()).also { it.println() } == 7)
    check(part1Optimized(testInput.first()).also { it.println() } == 7)
    // check(part2(testInput).also { it.println() } == "MCD")

    val input = readInput("aoc2022/day06/Day06")

    println("Part 1 Answer: ${part1SubstringSets(input.first())}")
    println("Part 1 Answer (optimized): ${part1Optimized(input.first())}")
    //println("Part 2 Answer: ${part2(input)}")

    measurePerformance("Substring Sets", 10000) {
        part1SubstringSets(input.first())
    }
    measurePerformance("Optimized", 10000) {
        part1Optimized(input.first())
    }
}

fun part1SubstringSets(input: String) =
    input.indices.first { startIndex ->
        input
            .subString(
                startIndex = startIndex,
                length = START_MARKER_LENGTH
            )
            .toSet()
            .size == START_MARKER_LENGTH
    } + START_MARKER_LENGTH

fun part1Optimized(input: String): Int {
    val charFreq = HashMap<Char, Int>()
    return input.indices.first { endIndex ->
        // Remove START_MARKER_LENGTH characters ago
        val charToRemove = (endIndex - START_MARKER_LENGTH)
            .takeIf { it >= 0 }
            ?.let { input[it] }
        if (charToRemove != null) {
            charFreq[charToRemove] = charFreq[charToRemove]!! - 1
            if (charFreq[charToRemove] == 0) {
                charFreq.remove(charToRemove)
            }
        }

        // Add this "end" character
        val charToAdd = input[endIndex]
        charFreq[charToAdd] = charFreq.getOrDefault(charToAdd, 0) + 1

        // Accept this as the end of a start marker if the frequency map over the last
        // START_MARKER_LENGTH characters has a size of START_MARKER_LENGTH unique characters.
        charFreq.size == START_MARKER_LENGTH
    } + 1 // Index of first character AFTER the start marker
}

fun part2(input: String): Int {
    return input.length
}
