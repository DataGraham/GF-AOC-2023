package day11

import println
import readInput
import kotlin.math.abs

fun main() {
    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day11/Day11_test")).also { it.println() } == 374)
    //check(part2(testInput) == 1)

    //    val input = readInput("day11/Day11")
    //    println("Part 1 Answer: ${part1(input)}")
    //    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val galaxyLocations = galaxyLocations(expand(universe = input))
    return galaxyLocations.indices.sumOf { galaxyIndex ->
        val galaxy = galaxyLocations[galaxyIndex]
        galaxyLocations.indices.drop(galaxyIndex + 1).sumOf { otherGalaxyIndex ->
            val otherGalaxy = galaxyLocations[otherGalaxyIndex]
            abs(galaxy.first - otherGalaxy.first) +
                abs(galaxy.second - otherGalaxy.second)
            /*.also { distance ->
                println("$galaxyIndex ($galaxy) and $otherGalaxyIndex ($otherGalaxy) are $distance apart")
            }*/
        }
    }
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

private fun galaxyLocations(universe: List<String>) =
    universe.indices.flatMap { row ->
        universe[row].indices.mapNotNull { col ->
            if (universe[row][col] == GALAXY) (row to col) else null
        }
    }
