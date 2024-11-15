package aoc2022.day08

import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day08/Day08_test")
    check(part1(testInput).also { it.println() } == 21)
    //check(part2(testInput).also { it.println() } == 24933642)

    //val input = readInput("aoc2022/day08/Day08")
    //println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val treeRows = input.map { line ->
        line.map { it.digitToInt() }
    }

    return input.size
}

fun part2(input: List<String>) = input.size