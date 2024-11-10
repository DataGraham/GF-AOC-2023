package aoc2022.day06

import println
import readInput
import subString

private const val START_MARKER_LENGTH = 4

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day06/Day06_test")
    check(part1(testInput.first()).also { it.println() } == 7)
    // check(part2(testInput).also { it.println() } == "MCD")

    val input = readInput("aoc2022/day06/Day06")
    println("Part 1 Answer: ${part1(input.first())}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: String) =
    input.indices.first { startIndex ->
        input
            .subString(
                startIndex = startIndex,
                length = START_MARKER_LENGTH
            )
            .toSet()
            .size == START_MARKER_LENGTH
    } + START_MARKER_LENGTH

fun part2(input: String): Int {
    return input.length
}
