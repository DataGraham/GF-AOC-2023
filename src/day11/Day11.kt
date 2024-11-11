package day11

import dropLast
import measurePerformance
import println
import readInput
import kotlin.math.abs
import kotlin.time.measureTimedValue

fun main() {
    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day11/Day11_test")).also { it.println() } == 374)
    //check(aoc2022.day07.part2(testInput) == 1)

    val input = readInput("day11/Day11")
    println("Part 1 Answer: ${part1(input)}")
    // Part 2 (counts) Computed 635832237682 in average of 874 microseconds
    // Part 2 (offsets) Computed 635832237682 in average of 848 microseconds
    measurePerformance("Part 2 (offsets)", 10000) { part2(input) }
}

fun part1(input: List<String>): Int {
    val galaxyLocations = galaxyLocations(expand(universe = input))
    return galaxyLocations.indices.sumOf { galaxyIndex ->
        galaxyLocations[galaxyIndex].let { galaxy ->
            galaxyLocations.drop(galaxyIndex + 1).sumOf { otherGalaxy -> galaxy manhattanDistanceTo otherGalaxy }
        }
    }
}

private fun <T> reportTime(label: String, block: () -> T) = measureTimedValue(block).run {
    println("$label takes $duration")
    value
}

fun part2(input: List<String>): Long {
    val rowOffsets = reportTime("Row offsets") {
        input.dropLast(1).scan(0) { before, row ->
            if (row.isEmptyRow) before + (EXPANSION_FACTOR - 1) else before
        }
    }
    val columnOffsets = reportTime("Column offsets") {
        input.first().indices.dropLast(1).scan(0) { before, col ->
            if (input.isColumnEmpty(col)) before + (EXPANSION_FACTOR - 1) else before
        }
    }
    val expandedGalaxyLocations = reportTime("Expanding") {
        galaxyLocations(input).map { (row, col) ->
            row + rowOffsets[row] to col + columnOffsets[col]
        }
    }
    return reportTime("Distances") {
        expandedGalaxyLocations.indices.sumOf { galaxyIndex ->
            expandedGalaxyLocations[galaxyIndex].let { galaxy ->
                expandedGalaxyLocations.drop(galaxyIndex + 1).sumOf { otherGalaxy ->
                    (galaxy manhattanDistanceTo otherGalaxy).toLong()
                }
            }
        }
    }
}

const val EMPTY_CHARACTER = '.'
const val GALAXY_CHARACTER = '#'
const val EXPANSION_FACTOR = 1000000

fun expand(universe: List<String>): List<String> {
    // Expand the galaxy
    // 1. Replace each empty row with two empty rows
    val expandedRows = universe.flatMap { row ->
        List(if (row.isEmptyRow) 2 else 1) { row }
    }
    // 2. Detect empty columns, then, in each row, replace characters in those columns with two copies.
    val emptyColumns = universe.first().indices.filter { col -> universe.isColumnEmpty(col) }.toSet()
    return expandedRows.map { row ->
        row.mapIndexed { col, char ->
            "$char".repeat(if (col in emptyColumns) 2 else 1)
        }.joinToString(separator = "")
    }
}

val String.isEmptyRow get() = all { it == EMPTY_CHARACTER }

fun List<String>.isColumnEmpty(col: Int) = all { row -> row[col] == EMPTY_CHARACTER }

private fun galaxyLocations(universe: List<String>) =
    universe.indices.flatMap { row ->
        universe[row].indices.mapNotNull { col ->
            if (universe[row][col] == GALAXY_CHARACTER) (row to col) else null
        }
    }

private infix fun Pair<Int, Int>.manhattanDistanceTo(other: Pair<Int, Int>) =
    abs(first - other.first) + abs(second - other.second)
