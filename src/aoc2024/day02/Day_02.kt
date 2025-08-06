package aoc2024.day02

import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day02/Day02_test")
    check(part1(testInput).also { it.println() } == 2)
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("aoc2024/day02/Day02")
    println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) = input.parseReports().count { report -> report.isSafeReport }

fun part2(input: List<String>): Int {
    return input.size
}

private fun List<String>.parseReports() = map { line ->
    line.split(" ").map { level -> level.toInt() }
}

private val List<Int>.isSafeReport get() = deltas.isSafeDeltas

private val List<Int>.deltas get() = zipWithNext { a, b -> b - a }

private val List<Int>.isSafeDeltas get() =
    safeRanges.any { safeRange -> safeRange.containsAll(this) }

private val smallIncrease = 1..3
private val smallDecrease = -3..-1
private val safeRanges = listOf(smallIncrease, smallDecrease)

private fun IntRange.containsAll(values: List<Int>) = values.all { it in this }
