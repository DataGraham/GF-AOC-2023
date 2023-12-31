package day13

import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day13/Day13_test")
    check(part1(testInput) == 1)
    //check(part2(testInput) == 1)

    val input = readInput("day13/Day13")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}
