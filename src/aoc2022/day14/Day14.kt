package aoc2022.day14

import Position
import minus
import println
import readInput
import kotlin.math.max
import kotlin.math.min

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day14/Day14_test")
    check(part1(testInput).also { it.println() } == 24)
    //check(part2(testInput).also { it.println() } == 140)

    //val input = readInput("aoc2022/day14/Day14")
    //println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    // Read in each line as a path with a series of coordinate pairs.
    val regex = Regex("""(\d+),(\d+)""")
    val rockFormations = input.map { line ->
        regex.findAll(line)
            .map { match -> match.groupValues.drop(1).map { capture -> capture.toInt() } }
            .map { (x, y) -> Position(row = y, col = x) }
            .toList()
    }

    // Find the min-max range (including the assumed 500, 0 sand start position).
    val sandStartPosition = Position(row = 0, col = 500)
    val allPositions = rockFormations.flatten() + sandStartPosition
    val origin = Position(
        row = allPositions.minOf { it.row },
        col = allPositions.minOf { it.col }
    )
    val max = Position(
        row = allPositions.maxOf { it.row },
        col = allPositions.maxOf { it.col }
    )

    val numRows = max.row - origin.row + 1
    val numCols = max.col - origin.col + 1

    // Create a grid and initialize, subtracting the minimum from each x and y.
    val isPositionFilled = List(numRows) { MutableList(numCols) { false } }
    rockFormations.forEach { rockFormation ->
        rockFormation
            .map { position ->
                (position - origin).let { (relativeRow, relativeCol) ->
                    Position(row = relativeRow, col = relativeCol)
                }
            }
            .windowed(size = 2, step = 1)
            .forEach { (start, end) ->
                positions(from = start, to = end).forEach { position ->
                    isPositionFilled[position.row][position.col] = true
                }
            }
    }

    isPositionFilled
        .joinToString(separator = "\n") { row ->
            String(row.map { if (it) '#' else '.' }.toCharArray())
        }
        .println()

    // Iterate each piece of sand until it rests,
    // Finally stopping just before the first piece that falls outside of the min/max grid.

    return input.size
}

fun part2(input: List<String>) = input.size

private fun positions(from: Position, to: Position) = when {
    from.col == to.col ->
        (min(from.row, to.row)..max(from.row, to.row))
            .map { row -> Position(row = row, col = from.col) }

    from.row == to.row -> (min(from.col, to.col)..max(from.col, to.col))
        .map { col -> Position(row = from.row, col = col) }

    else -> throw IllegalArgumentException("Neither row nor column values match (from: $from, to: $to)")
}