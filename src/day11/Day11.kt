package day11

import dropLast
import println
import readInput
import kotlin.math.abs
import kotlin.math.exp

fun main() {
    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day11/Day11_test")).also { it.println() } == 374)
    //check(part2(testInput) == 1)

    val input = readInput("day11/Day11")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val galaxyLocations = galaxyLocations(expand(universe = input))
    return galaxyLocations.indices.sumOf { galaxyIndex ->
        galaxyLocations[galaxyIndex].let { galaxy ->
            galaxyLocations.drop(galaxyIndex + 1).sumOf { otherGalaxy -> galaxy manhattanDistanceTo otherGalaxy }
        }
    }
}

fun part2(input: List<String>): Long {
    // TODO: Yeah, we're going to have to perform expansion on the galaxy locations,
    //  rather than the entire input.
    val emptyRowsBefore = input.dropLast(1).scan(0) { before, row ->
        if (row.isEmptyRow) before + 1 else before
    }
    val emptyColumnsBefore = input.first().indices.dropLast(1).scan(0) { before, col ->
        if (input.isColumnEmpty(col)) before + 1 else before
    }
    val galaxyLocations = galaxyLocations(input)
    val expandedGalaxyLocations = galaxyLocations.map { (row, col) ->
        // TODO: Reduce multiplications by including it above (as an offset rather than empty-before count)
        row + (emptyRowsBefore[row] * (EXPANSION_FACTOR - 1)) to
            col + (emptyColumnsBefore[col] * (EXPANSION_FACTOR - 1))
    }
    return expandedGalaxyLocations.indices.sumOf { galaxyIndex ->
        galaxyLocations[galaxyIndex].let { galaxy ->
            galaxyLocations.drop(galaxyIndex + 1).sumOf { otherGalaxy ->
                (galaxy manhattanDistanceTo otherGalaxy).toLong()
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
