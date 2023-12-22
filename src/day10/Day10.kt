package day10

import day10.Direction.*
import readInput
import java.lang.IndexOutOfBoundsException
import kotlin.math.abs

fun main() {
    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day10/Day10_test")) == 4)
    check(part1(readInput("day10/Day10_test2")) == 8)
    //check(part2(testInput) == 1)

    val input = readInput("day10/Day10")
    println("Part 1 Answer: ${part1(input)}")
    //    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val pipes = input.map { line ->
        line.map { char ->
            PipeShape.entries.firstOrNull { it.character == char }
        }
    }

    val startPosition = input.indices.firstNotNullOf { row ->
        input[row].indices.firstNotNullOfOrNull { col ->
            if (input[row][col] == 'S') Position(row = row, col = col)
            else null
        }
    }

    val startDirections = Direction.entries.filter { direction ->
        (startPosition move direction).let { adjacentPosition ->
            pipes[adjacentPosition]?.connections?.any { backDirection ->
                adjacentPosition move backDirection == startPosition
            } == true
        }
    }

    // TODO: Or just pick a direction and go until you get back to the start,
    //  and then just like divide by 2?
    val (firstPath, secondPath) = startDirections.map { direction ->
        generateSequence(
            startPosition to
                (startPosition move direction)
        ) { (previous, current) ->
            current to (
                pipes[current]!!.connections
                    .map { direction -> current move direction }
                    .first { adjacent -> adjacent != previous }
                )
        }
    }

    return firstPath.zip(secondPath).takeWhile { (firstPathState, secondPathState) ->
        firstPathState.second != secondPathState.second &&
            !(firstPathState.second isAdjacentTo secondPathState.second)
    }.toList().size + 1
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