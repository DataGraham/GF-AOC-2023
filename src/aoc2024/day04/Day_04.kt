package aoc2024.day04

import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day04/Day04_test")
    check(part1(testInput).also { it.println() } == 161)
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("aoc2024/day04/Day04")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}
