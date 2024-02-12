package day18

import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day18/Day18_test")
    check(part1(testInput).also { it.println() } == 62)
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("day18/Day18")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val movements = input.map { it.parseMovement() }
    val dugBorderPositions = movements.fold(
        listOf(Position(0, 0))
    ) { dugPositions, movement ->
        dugPositions +
            generateSequence(dugPositions.last()) { it move movement.direction }
                .drop(1)
                .take(movement.moveCount)
    }.toSet()
    val width = dugBorderPositions.maxOf { it.col } + 1
    val height = dugBorderPositions.maxOf { it.row } + 1
    val dug = List(height) { row ->
        List(width) { col ->
            Position(row = row, col = col) in dugBorderPositions
        }
    }
    dug.joinToString(separator = "\n") { line ->
        line.map { if (it) '#' else '.' }.joinToString(separator = "")
    }.println()
    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}

data class Movement(val direction: Direction, val moveCount: Int)

val movementRegex = Regex("^([UDLR]) ([0-9])")

fun String.parseMovement() = movementRegex
    .find(this)!!
    .let { matchResult ->
        Movement(
            direction = when(matchResult.groups[1]!!.value) {
                "U" -> Direction.North
                "D" -> Direction.South
                "L" -> Direction.West
                "R" -> Direction.East
                else -> throw IllegalArgumentException()
            },
            moveCount = matchResult.groups[2]!!.value.toInt()
        )
    }

data class Position(val row: Int, val col: Int)

enum class Direction(val rowDelta: Int, val colDelta: Int) {
    North(rowDelta = -1, colDelta = 0),
    South(rowDelta = 1, colDelta = 0),
    East(rowDelta = 0, colDelta = 1),
    West(rowDelta = 0, colDelta = -1);
}

infix fun Position.move(direction: Direction) = Position(
    row = row + direction.rowDelta,
    col = col + direction.colDelta
)
