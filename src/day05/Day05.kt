package day05

import readInput
import requireSubstringAfter
import split

fun main() {

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day05/Day05_test")
    check(part1(testInput) == 35)
    //check(part2(testInput) == 1)

    val input = readInput("day05/Day05")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val seeds = input.first()
        .requireSubstringAfter(':')
        .trim()
        .split(' ')
        .map(String::toInt)
    val mapStringSets = input.drop(1).split { it.isBlank() }
    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}
