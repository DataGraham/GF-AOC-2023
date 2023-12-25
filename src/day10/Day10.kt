package day10

import day10.Direction.*
import dropLast
import println
import readInput
import java.lang.IndexOutOfBoundsException
import kotlin.math.abs

fun main() {
    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day10/Day10_test")).also { it.println() } == 4)
    check(part1(readInput("day10/Day10_test2")).also { it.println() } == 8)
    check(part2(readInput("day10/Day10_test3")).also { it.println() } == 10)

    val input = readInput("day10/Day10")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val pipes = parsePipeShapes(input)
    val startPosition = findStartPosition(input)
    val startDirection = findStartDirection(startPosition, pipes)
    val loop = findLoop(startPosition, startDirection, pipes)
    return loop.size / 2
}

fun part2(input: List<String>): Int {
    // Compute incoming and outgoing direction for each position that is part of the loop.
    // Scan each row:
    //  Starting with outside, flip in/out when the position includes north or south
    //  and it's the opposite of the previous north/south.
    //  While we're inside, any position with NO direction (not on the loop) is inside.
    val pipes = parsePipeShapes(input)
    val startPosition = findStartPosition(input)
    val startDirection = findStartDirection(startPosition, pipes)
    val loop = findLoop(startPosition, startDirection, pipes)
    val positionDirections = loop.flatMap { (previous, current) ->
        val direction = previous - current
        listOf(
            previous to direction,
            current to direction
        )
    }.groupBy { it.first }
        .mapValues { it.value.map { (_, direction) -> direction } }

    val wouldBeInside = pipes.indices.map { row ->
        pipes[row].indices.dropLast(1).scan(
            (false to null as Direction?)
        ) { (inside, northSouthDirection), col ->
            val here = Position(row = row, col = col)
            val northSouthHere = positionDirections[here]?.firstOrNull { it in listOf(North, South) }
            // If we see a different north/south, then flip direction and inside-ness,
            if (northSouthHere != null && northSouthHere != northSouthDirection) !inside to northSouthHere
            else inside to northSouthDirection // otherwise no change
        }.map { (inside, _) -> inside }
    }

    val escape = "\u001b"
    val blueBackground = "$escape[106m"
    val grayBackground = "$escape[100m"
    val reset = "$escape[0m"
    pipes.indices.joinToString(separator = "\n") { row ->
        pipes[row].indices.joinToString(separator = "") { col ->
            val here = Position(row, col)
            val char = if (positionDirections[here] != null) pipes[here]?.niceCharacter ?: ' '
            else ' '
            if (wouldBeInside[row][col] && positionDirections[here] == null) "$blueBackground$char$reset" else "$char"
        }
    }.println()

    wouldBeInside.joinToString(separator = "\n") { row ->
        row.joinToString(separator = "") {
            val colour = if (it) blueBackground else grayBackground
            "$colour $reset"
        }
    }.println()

    return pipes.indices.sumOf { row ->
        // Count the columns in this row which "would be inside" and are not part of the loop.
        // These are the columns within this row that are actually enclosed by the loop.
        pipes[row].indices.count { col ->
            wouldBeInside[row][col] && positionDirections[Position(row, col)] == null
        }
    }
}

private fun findLoop(
    startPosition: Position,
    startDirection: Direction,
    pipes: List<List<PipeShape?>>
) =
    generateSequence(
        startPosition to (startPosition move startDirection)
    ) { (previous, current) ->
        current to (
            pipes[current]!!.connections
                .map { direction -> current move direction }
                .first { adjacent -> adjacent != previous }
            )
    }.takeWhile { (_, current) ->
        current != startPosition
    }.toList()
        .let { most -> most + (most.last().second to startPosition) }

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

enum class PipeShape(val character: Char, val niceCharacter: Char, val connections: Set<Direction>) {
    NorthSouth('|', '┃', setOf(North, South)),
    EastWest('-', '━', setOf(East, West)),
    NorthEast('L', '┗', setOf(North, East)),
    NorthWest('J', '┛', setOf(North, West)),
    SouthWest('7', '┓', setOf(South, West)),
    SouthEast('F', '┏', setOf(South, East))
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

operator fun Position.minus(other: Position): Direction {
    val rowDelta = other.row - row
    val colDelta = other.col - col
    return Direction.entries.first {
        it.rowDelta == rowDelta && it.colDelta == colDelta
    }
}

operator fun List<List<PipeShape?>>.get(position: Position) = try {
    this[position.row][position.col]
} catch (e: IndexOutOfBoundsException) {
    null
}