package aoc2022.day09

import aoc2022.day09.Direction.*
import println
import readInput
import requireSubstringAfter
import kotlin.math.abs
import kotlin.math.max

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day09/Day09_test")
    check(part1(testInput).also { it.println() } == 13)
    // check(part2(testInput).also { it.println() } == 8)

    val input = readInput("aoc2022/day09/Day09")
    println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val movesInstructions = input
        .map { it.parseMoveInstruction() }
    //.joinToString(separator = "\n").println()
    return movesInstructions.fold(RopeState()) { state, moveInstruction ->
        state perform moveInstruction
    }.tailVisitedPositions.size
}

fun part2(input: List<String>) = input.size

private infix fun RopeState.perform(moveInstruction: MoveInstruction) = apply {
    repeat(moveInstruction.count) {
        headPosition = headPosition move moveInstruction.direction
        tailPosition = getNextTailPosition(
            headPosition = headPosition,
            tailPosition = tailPosition
        )
        tailVisitedPositions += tailPosition
    }
}

private data class RopeState(
    var headPosition: Position = Position(0, 0),
    var tailPosition: Position = Position(0, 0),
    val tailVisitedPositions: MutableSet<Position> = mutableSetOf(Position(0, 0))
)

private fun getNextTailPosition(headPosition: Position, tailPosition: Position) =
    (headPosition - tailPosition).let { deltaPosition ->
        when {
            deltaPosition.isAdjacent -> tailPosition
            else -> tailPosition + deltaPosition.unitized()
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

private data class Position(val row: Int, val col: Int)

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

private fun Int.unitized() = when (this) {
    0 -> 0
    else -> this / abs(this)
}

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
