package aoc2022.day01

import println
import readInput
import split

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day01/Day01_test")
    check(part1(testInput).also { it.println() } == 24000 )
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("aoc2022/day01/Day01")
    println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val elves = input.split { it.isBlank() }
    return elves.maxOf { elfItems ->
        elfItems.sumOf { itemCaloriesString -> itemCaloriesString.toInt() }
    }
}

fun part2(input: List<String>): Int {
    return input.size
}
