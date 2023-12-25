package day11

import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day11/Day11_test")
    check(part1(testInput) == 1)
    //check(part2(testInput) == 1)

//    val input = readInput("day11/Day11")
//    println("Part 1 Answer: ${part1(input)}")
//    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    // Expand the galaxy
    // 1. Replace each empty row with two empty rows
    // 2. Detect empty columns, then, in each row, replace characters in those columns with two copies.

    val expandedRows = input.flatMap { row ->
        List(if (row.all { it == EMPTY }) 2 else 1) { row }
    }

    expandedRows.joinToString(separator = "\n").println()

    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}

const val EMPTY = '.'
const val GALAXY = '#'
