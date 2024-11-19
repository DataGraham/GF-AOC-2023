package aoc2022.day09

import aoc2022.day09.Direction.*
import aoc2022.day09.Position.Companion.ORIGIN
import println
import readInput
import requireSubstringAfter
import kotlin.math.abs
import kotlin.math.max

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day09/Day09_test")
    check(part1(testInput).also { it.println() } == 13)
    check(part2(testInput).also { it.println() } == 1)
    check(part2(readInput("aoc2022/day09/Day09_test2")).also { it.println() } == 36)

    val input = readInput("aoc2022/day09/Day09")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) = input.uniqueTailPositionCount(ropeLength = 2)

fun part2(input: List<String>) = input.uniqueTailPositionCount(ropeLength = 10)

private fun List<String>.uniqueTailPositionCount(ropeLength: Int) =
    map { line -> line.parseMoveInstruction() }
        .flatMap { moveInstruction -> moveInstruction.toDirections() }
        .scan(RopePosition.ofLength(ropeLength)) { rope, direction -> rope move direction }
        .map { ropePosition -> ropePosition.knotPositions.last() }
        .toSet()
        .size

private data class RopePosition(val knotPositions: List<Position>) {
    companion object {
        fun ofLength(length: Int) = RopePosition(List(length) { ORIGIN })
    }
}

private infix fun RopePosition.move(direction: Direction) =
    RopePosition(
        knotPositions = (listOf((knotPositions.first() move direction)) + knotPositions.drop(1))
            .runningReduce { leadingPosition, trailingPosition ->
                trailingPosition follow leadingPosition
            }
    )

private infix fun Position.follow(leadingPosition: Position) =
    (leadingPosition - this).let { deltaPosition ->
        when {
            deltaPosition.isAdjacent -> this
            else -> this + deltaPosition.unitized()
        }
    }

private fun String.parseMoveInstruction() = MoveInstruction(
    direction = this[0].toDirection(),
    count = this.requireSubstringAfter(' ').toInt()
)

private fun Char.toDirection() = when (this) {
    'U' -> Up
    'D' -> Down
    'R' -> Right
    'L' -> Left
    else -> throw IllegalArgumentException("Unrecognized direction character $this")
}

private data class MoveInstruction(val direction: Direction, val count: Int)

private fun MoveInstruction.toDirections() = List(count) { direction }

private data class Position(val row: Int, val col: Int) {
    companion object {
        val ORIGIN = Position(row = 0, col = 0)
    }
}

private data class DeltaPosition(val deltaRow: Int, val deltaCol: Int)

private operator fun Position.minus(otherPosition: Position) =
    DeltaPosition(deltaRow = row - otherPosition.row, deltaCol = col - otherPosition.col)

private operator fun Position.plus(deltaPosition: DeltaPosition) = Position(
    row = row + deltaPosition.deltaRow,
    col = col + deltaPosition.deltaCol
)

private val DeltaPosition.isAdjacent get() = max(abs(deltaRow), abs(deltaCol)) <= 1

private fun DeltaPosition.unitized() = copy(
    deltaRow = deltaRow.unitized(),
    deltaCol = deltaCol.unitized()
)

private fun Int.unitized() = this / abs(this).coerceAtLeast(1)

private enum class Direction(val rowDelta: Int, val colDelta: Int) {
    Up(rowDelta = -1, colDelta = 0),
    Down(rowDelta = 1, colDelta = 0),
    Right(rowDelta = 0, colDelta = 1),
    Left(rowDelta = 0, colDelta = -1);
}

private infix fun Position.move(direction: Direction) = Position(
    row = row + direction.rowDelta,
    col = col + direction.colDelta
)
