package aoc2022.day05

import aoc2022.day05.CrateFinder.crateLabels
import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day05/Day05_test")
    check(part1(testInput).also { it.println() } == "CMZ")
    //check(part2(testInput).also { it.println() } == 4)

    //val input = readInput("aoc2022/day05/Day05")
    //println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): String {
    val crateColumns = input
        .crateRows.also { println("Crate rows: $it") }
        .transposed()
        .map { crateStack -> crateStack.filterNotNull() }
        .also { println("Crate columns: $it") }
    return ""
}

private val List<String>.crateRows
    get() = map { line -> line.crateLabels() }
        .takeWhile { crateLabels -> crateLabels.any { it != null } }

private object CrateFinder {
    private val crateSpaceRegex by lazy {
        Regex(""" {3}|\[(\p{Upper})]""")
    }

    fun String.crateLabels() =
        crateSpaceRegex
            .findAll(this)
            .map { it.groups[1]?.value }
            .toList()
}

fun <T> List<List<T?>>.transposed() =
    List(maxOf { it.size }) { i -> map { it.getOrNull(i) } }

fun part2(input: List<String>): String {
    return ""
}