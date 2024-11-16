package aoc2022.day08

import all
import kotlinx.coroutines.flow.count
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.takeWhile
import kotlinx.coroutines.runBlocking
import max
import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day08/Day08_test")
    check(part1(testInput).also { it.println() } == 21)
    check(part2(testInput).also { it.println() } == 8)

    val input = readInput("aoc2022/day08/Day08")
    println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) = runBlocking {
    input.toForest().run {
        allPositions.count { position -> isTreeVisible(position) }
    }
}

fun part2(input: List<String>) = runBlocking {
    input.toForest().run {
        allPositions
            .map { position ->
                viewScore(position).also {
                    println("view score at $position is $it")
                }
            }
            .max()
    }
}

typealias Forest = List<List<Int>>

private fun List<String>.toForest() = map { line -> line.map { it.digitToInt() } }

private suspend fun Forest.viewScore(position: Position) =
    Direction.entries
        .map { direction -> treesVisible(position, direction) }
        .reduce { score, treesVisibleInDirection -> score * treesVisibleInDirection }

private suspend fun Forest.treesVisible(from: Position, direction: Direction) =
    this[from].let { heightAtPosition ->
        positions(from, direction)
            .map { otherPosition -> this[otherPosition] }
            .takeWhile { otherHeight -> otherHeight <= heightAtPosition }
            .count()
    }

private suspend fun Forest.isTreeVisible(position: Position) =
    Direction.entries.any { direction ->
        isTreeVisibleFromDirection(position, direction)
    }

private suspend fun Forest.isTreeVisibleFromDirection(
    position: Position,
    direction: Direction
) = positions(from = position, inDirection = direction)
    .all { otherPosition -> this[otherPosition] < this[position] }

private fun Forest.positions(
    from: Position,
    inDirection: Direction
) = flow {
    var here = from move inDirection
    while (isPositionValid(here)) {
        emit(here)
        here = here move inDirection
    }
}

private val Forest.allPositions
    get() = flow {
        forEachIndexed { rowIndex, row ->
            row.indices.forEach { columnIndex ->
                emit(Position(row = rowIndex, col = columnIndex))
            }
        }
    }

private fun Forest.isPositionValid(position: Position) =
    with(position) { row in indices && col in this@isPositionValid[row].indices }

private operator fun Forest.get(position: Position) = this[position.row][position.col]

private data class Position(val row: Int, val col: Int)

private enum class Direction(val rowDelta: Int, val colDelta: Int) {
    North(rowDelta = -1, colDelta = 0),
    South(rowDelta = 1, colDelta = 0),
    East(rowDelta = 0, colDelta = 1),
    West(rowDelta = 0, colDelta = -1);
}

private infix fun Position.move(direction: Direction) = Position(
    row = row + direction.rowDelta,
    col = col + direction.colDelta
)
