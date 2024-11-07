package aoc2022.day04

import println
import readInput
import require

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day04/Day04_test")
    check(part1(testInput).also { it.println() } == 2)
    check(part2(testInput).also { it.println() } == 4)

    val input = readInput("aoc2022/day04/Day04")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) =
    input
        .map { line -> line.toSectionIdRanges() }
        .count { sectionIdRangePair -> sectionIdRangePair.oneContainsTheOther() }

fun part2(input: List<String>) =
    input
        .map { line -> line.toSectionIdRanges() }
        .count { sectionIdRangePair -> sectionIdRangePair.first overlaps sectionIdRangePair.second }

fun String.toSectionIdRanges(): Pair<IntRange, IntRange> =
    split(',')
        .require { size == 2 }
        .map { elfRangeString -> elfRangeString.toSectionIdRange() }
        .let { sectionIdRanges -> sectionIdRanges[0] to sectionIdRanges[1] }

private fun String.toSectionIdRange() =
    split('-')
        .require { size == 2 }
        .map { sectionIdString -> sectionIdString.toInt() }
        .let { sectionIds -> sectionIds[0]..sectionIds[1] }

private fun Pair<IntRange, IntRange>.oneContainsTheOther() =
    first in second || second in first

private infix fun IntRange.overlaps(other: IntRange) =
    first <= other.last && last >= other.first

operator fun IntRange.contains(other: IntRange) =
    first <= other.first && last >= other.last
