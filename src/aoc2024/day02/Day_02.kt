package aoc2024.day02

import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day02/Day02_test")
    check(part1(testInput).also { it.println() } == 1)
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("aoc2024/day02/Day02")
    println("Part 1 Answer: ${day02.part1(input)}")
    println("Part 2 Answer: ${day02.part2(input)}")
}

fun part1(input: List<String>): Int {
    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}
