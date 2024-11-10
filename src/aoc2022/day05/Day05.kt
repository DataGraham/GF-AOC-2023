package aoc2022.day05

import aoc2022.day05.CratesParser.parseCrates
import aoc2022.day05.MoveParser.parseMoves
import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day05/Day05_test")
    check(part1(testInput).also { it.println() } == "CMZ")
    check(part2(testInput).also { it.println() } == "MCD")

    val input = readInput("aoc2022/day05/Day05")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) = finalCrateStackTops(input) { move ->
    repeat(move.count) {
        perform(move.copy(count = 1))
    }
}

fun part2(input: List<String>) = finalCrateStackTops(input) { move -> perform(move) }

private fun finalCrateStackTops(input: List<String>, performMove: Crates.(Move) -> Unit) =
    parseCrates(input)
        .apply { parseMoves(input).forEach { move -> performMove(move) } }
        .stackTops
