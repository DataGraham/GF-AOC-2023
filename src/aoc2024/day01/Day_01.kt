package aoc2024.day01

import RegexParser
import parseLines
import println
import readInput
import kotlin.math.abs

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day01/Day01_test")
    check(part1(testInput).also { it.println() } == 11)
    check(part2(testInput).also { it.println() } == 31)

    val input = readInput("aoc2024/day01/Day01")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) =
    input.parseLocationIdLists().let { (listA, listB) ->
        listA.sorted().zip(listB.sorted()) { a, b -> abs(a - b) }.sum()
    }

fun part2(input: List<String>): Int {
    val (listA, listB) = input.parseLocationIdLists()
    val frequenciesB = listB.groupingBy { it }.eachCount()
    return listA.sumOf { a ->
        a * (frequenciesB[a] ?: 0)
    }
}

private fun List<String>.parseLocationIdLists() =
    locationIdPairParser.parseLines(this).let { locationIdPairs ->
        locationIdPairs.map { it.first } to locationIdPairs.map { it.second }
    }

private val locationIdPairParser by lazy {
    RegexParser("""(\d+) +(\d+)""") { (locationIdA, locationIdB) ->
        locationIdA.toInt() to locationIdB.toInt()
    }
}