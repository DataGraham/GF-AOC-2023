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

val List<Int>.isSafeReport: Boolean get() {
    val deltas = zipWithNext { a, b -> b - a }
    return deltas.all { it in (1..3) } || deltas.all { it in (-3 .. -1) }
}
