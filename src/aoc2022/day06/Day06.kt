package aoc2022.day06

import measurePerformance
import println
import readInput
import subString

private const val PACKET_START_MARKER_LENGTH = 4
private const val MESSAGE_START_MARKER_LENGTH = 14

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day06/Day06_test")
    check(part1SubstringSets(testInput.first()).also { it.println() } == 7)
    check(part1Optimized(testInput.first()).also { it.println() } == 7)
    check(part2(testInput.first()).also { it.println() } == 19)

    val input = readInput("aoc2022/day06/Day06")

    println("Part 1 Answer: ${part1SubstringSets(input.first())}")
    println("Part 1 Answer (optimized): ${part1Optimized(input.first())}")

    measurePerformance("Substring Sets", 10000) {
        part1SubstringSets(input.first())
    }
    measurePerformance("Optimized", 10000) {
        part1Optimized(input.first())
    }

    println("Part 2 Answer: ${part2(input.first())}")
}

fun part1SubstringSets(input: String) =
    input.indices.first { startIndex ->
        input
            .subString(
                startIndex = startIndex,
                length = PACKET_START_MARKER_LENGTH
            )
            .toSet()
            .size == PACKET_START_MARKER_LENGTH
    } + PACKET_START_MARKER_LENGTH

fun part1Optimized(input: String) = indexAfterStartMarker(
    input,
    markerLength = PACKET_START_MARKER_LENGTH
)

fun part2(input: String) = indexAfterStartMarker(
    input,
    markerLength  = MESSAGE_START_MARKER_LENGTH
)

private fun indexAfterStartMarker(input: String, markerLength: Int) =
    with(CharacterFrequencyTracker()) {
        input.indices.first { endIndex ->
            // Remove markerLength characters ago
            if (endIndex >= markerLength)
                remove(input[endIndex - markerLength])
            // Add this "end" character
            add(input[endIndex])
            // Accept this as the end of a start marker if the frequency map over the last
            // START_MARKER_LENGTH characters has a size of START_MARKER_LENGTH unique characters.
            uniqueCharacterCount == markerLength
        } + 1 // Index of first character AFTER the start marker
    }

class CharacterFrequencyTracker {
    private val charFreq = HashMap<Char, Int>()

    fun add(charToAdd: Char) {
        charFreq[charToAdd] = charFreq.getOrDefault(charToAdd, 0) + 1
    }

    fun remove(charToRemove: Char) {
        charFreq[charToRemove] = charFreq[charToRemove]!! - 1
        if (charFreq[charToRemove] == 0) {
            charFreq.remove(charToRemove)
        }
    }

    val uniqueCharacterCount get() = charFreq.size
}