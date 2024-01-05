package day16

import day16.Direction.*
import println
import readInput
import java.lang.IllegalArgumentException
import kotlin.math.E

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day16/Day16_test")
    check(part1(testInput).also { it.println() } == 46)
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("day16/Day16")
    println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val entryDirections = Array(input.size) {
        Array(input.first().length) {
            HashSet<Direction>()
        }
    }
    val initialBeams = listOf(Beam(Position(0, -1), East))
    val beamsSequence = generateSequence(initialBeams) { beams ->
        beams.flatMap<Beam, Beam> { beam ->
            val nextPosition = beam.nextPosition
            if (nextPosition.row !in entryDirections.indices ||
                nextPosition.col !in entryDirections[nextPosition.row].indices
            ) return@flatMap emptyList<Beam>()
            val entryDirectionsHere = entryDirections[nextPosition.row][nextPosition.col]
            if (beam.direction in entryDirectionsHere) return@flatMap emptyList<Beam>()
            entryDirectionsHere += beam.direction

            // Outgoing beams
            when (input[nextPosition.row][nextPosition.col]) {
                '.' -> listOf(Beam(nextPosition, beam.direction))

                '\\' -> listOf(
                    Beam(
                        nextPosition,
                        when (beam.direction) {
                            North -> West
                            South -> East
                            East -> South
                            West -> North
                        }
                    )
                )

                '/' -> listOf(
                    Beam(
                        nextPosition,
                        when (beam.direction) {
                            North -> East
                            South -> West
                            East -> North
                            West -> South
                        }
                    )
                )

                '|' -> when (beam.direction) {
                    North, South -> listOf(Beam(nextPosition, beam.direction))
                    East, West -> listOf(
                        Beam(nextPosition, North),
                        Beam(nextPosition, South)
                    )
                }

                '-' -> when (beam.direction) {
                    East, West -> listOf(Beam(nextPosition, beam.direction))
                    North, South -> listOf(
                        Beam(nextPosition, East),
                        Beam(nextPosition, West)
                    )
                }

                else -> throw IllegalArgumentException()
            }
        }
    }
    beamsSequence.takeWhile { beams -> beams.isNotEmpty() }.last()
    return entryDirections.sumOf { row ->
        row.count { directions -> directions.isNotEmpty() }
    }
}

fun part2(input: List<String>): Int {
    return input.size
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

data class Beam(
    var position: Position,
    var direction: Direction
)

val Beam.nextPosition get() = position move direction
