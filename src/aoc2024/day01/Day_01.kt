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
    //check(part2(testInput).also { it.println() } == 1)

    //val input = readInput("day01/Day01")
    //println("Part 1 Answer: ${day02.part1(input)}")
    //println("Part 2 Answer: ${day02.part2(input)}")
}

fun part1(input: List<String>): Int {
    val locationIdPairs = locationIdPairParser.parseLines(input)
    val listA = locationIdPairs.map { it.first }.sorted()
    val listB = locationIdPairs.map { it.second }.sorted()
    return listA.zip(listB) { a, b -> abs(a - b) }.sum()
}

fun part2(input: List<String>): Int {
    return input.size
}

private val locationIdPairParser by lazy {
    RegexParser("""(\d+) +(\d+)""") { (locationIdA, locationIdB) ->
        locationIdA.toInt() to locationIdB.toInt()
    }
}