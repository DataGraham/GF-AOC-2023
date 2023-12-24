package day10

import day10.Direction.*
import println
import readInput
import java.lang.IndexOutOfBoundsException
import kotlin.math.abs

fun main() {
    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day10/Day10_test")).also { it.println() } == 4)
    check(part1(readInput("day10/Day10_test2")).also { it.println() } == 8)
    //check(part2(testInput) == 1)

    val input = readInput("day10/Day10")
    println("Part 1 Answer: ${part1(input)}")
    //    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val pipes = parsePipeShapes(input)
    val startPosition = findStartPosition(input)
    val startDirection = findStartDirection(startPosition, pipes)
    val loop = loopSequence(startPosition, startDirection, pipes)
    return (loop.toList().size + 1) / 2
}

private fun loopSequence(
    startPosition: Position,
    startDirection: Direction,
    pipes: List<List<PipeShape?>>
) = generateSequence(
    startPosition to (startPosition move startDirection)
) { (previous, current) ->
    current to (
        pipes[current]!!.connections
            .map { direction -> current move direction }
            .first { adjacent -> adjacent != previous }
        )
}.takeWhile { (_, current) -> current != startPosition }

private fun findStartDirection(
    startPosition: Position,
    pipes: List<List<PipeShape?>>
) = entries.first { direction ->
    (startPosition move direction).let { adjacentPosition ->
        pipes[adjacentPosition]?.connections?.any { backDirection ->
            adjacentPosition move backDirection == startPosition
        } == true
    }
}

private fun findStartPosition(input: List<String>) =
    input.indices.firstNotNullOf { row ->
        input[row].indices.firstNotNullOfOrNull { col ->
            if (input[row][col] == 'S') Position(row = row, col = col)
            else null
        }
    }

private fun parsePipeShapes(input: List<String>) = input.map { line ->
    line.map { char ->
        PipeShape.entries.firstOrNull { it.character == char }
    }
}

fun part2(input: List<String>): Int {
    return input.size
}

enum class PipeShape(val character: Char, val connections: Set<Direction>) {
    NorthSouth('|', setOf(North, South)),
    EastWest('-', setOf(East, West)),
    NorthEast('L', setOf(North, East)),
    NorthWest('J', setOf(North, West)),
    SouthWest('7', setOf(South, West)),
    SouthEast('F', setOf(South, East))
}

enum class Direction(val rowDelta: Int, val colDelta: Int) {
    North(rowDelta = -1, colDelta = 0),
    South(rowDelta = 1, colDelta = 0),
    East(rowDelta = 0, colDelta = 1),
    West(rowDelta = 0, colDelta = -1);
}

data class Position(val row: Int, val col: Int)

infix fun Position.move(direction: Direction) = Position(
    row = row + direction.rowDelta,
    col = col + direction.colDelta
)

val adjacentDeltas = setOf(0, 1)
infix fun Position.isAdjacentTo(other: Position) =
    setOf(abs(row - other.row), abs(col - other.col)) == adjacentDeltas

operator fun List<List<PipeShape?>>.get(position: Position) = try {
    this[position.row][position.col]
} catch (e: IndexOutOfBoundsException) {
    null
}