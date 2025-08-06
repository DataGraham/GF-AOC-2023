package aoc2024.day03

import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day03/Day03_test")
    check(part1(testInput).also { it.println() } == 161)
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("aoc2024/day03/Day03")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}
