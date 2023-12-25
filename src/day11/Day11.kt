package day11

import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day11/Day11_test")
    check(part1(testInput) == 1)
    //check(part2(testInput) == 1)

    val input = readInput("day11/Day11")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}
