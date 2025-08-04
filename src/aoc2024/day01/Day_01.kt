package aoc2024.day01

import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day01/Day01_test")
    check(part1(testInput).also { it.println() } == 11)
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("day01/Day01")
    println("Part 1 Answer: ${day02.part1(input)}")
    println("Part 2 Answer: ${day02.part2(input)}")
}

fun part1(input: List<String>): Int {
    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}
