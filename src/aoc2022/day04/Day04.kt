package aoc2022.day04

import println
import readInput
import require

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day04/Day04_test")
    check(part1(testInput).also { it.println() } == 2)
    //check(part2(testInput).also { it.println() } == 1)

    //    val input = readInput("day04/Day04")
    //    println("Part 1 Answer: ${part1(input)}")
    //    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    return input
        .map { it.toRanges }
        .count {
            it.first in it.second || it.second in it.first
        }
}

infix operator fun IntRange.contains(other: IntRange) =
    first <= other.first && last >= other.last

val String.toRanges: Pair<IntRange, IntRange>
    get() {
        return split(',')
            .require { size == 2 }
            .map { elfRangeString ->
                elfRangeString
                    .split('-')
                    .require { size == 2 }
                    .map { it.toInt() }
                    .let { it[0]..it[1] }
            }
            .let { it[0] to it[1] }
    }

fun part2(input: List<String>): Int {
    return input.size
}
