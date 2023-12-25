package day11

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
    val expandedUniverse = expand(universe = input)
    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}

const val EMPTY = '.'
const val GALAXY = '#'

fun expand(universe: List<String>): List<String> {
    // Expand the galaxy
    // 1. Replace each empty row with two empty rows
    val expandedRows = universe.flatMap { row ->
        List(if (row.all { it == EMPTY }) 2 else 1) { row }
    }
    // 2. Detect empty columns, then, in each row, replace characters in those columns with two copies.
    val emptyColumns = universe.first().indices.filter { col ->
        universe.indices.all { row -> universe[row][col] == EMPTY }
    }.toSet()
    return expandedRows.map { row ->
        row.mapIndexed { col, char ->
            "$char".repeat(if (col in emptyColumns) 2 else 1)
        }.joinToString(separator = "")
    }
}