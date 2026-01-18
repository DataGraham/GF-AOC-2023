package aoc2024.day09

import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day09/Day09_test")
    check(part1(testInput).also { it.println() } == 1928)
    //check(part2(testInput).also { it.println() } == 1)

    //val input = readInput("aoc2024/day09/Day09")
    //println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val codes = input.first().toCharArray().map { it.digitToInt() }
    val disk = codes.flatMapIndexed { i, c ->
        if (i % 2 == 0) {
            // start with a file
            val fileId = i / 2
            val fileSize = c
            List(fileSize) { fileId }
        } else {
            List(c) { null }
        }
    }
    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}
