package aoc2022.day09

import Direction
import Direction.*
import Position
import Position.Companion.ORIGIN
import isAdjacent
import minus
import move
import plus
import println
import readInput
import requireSubstringAfter
import unitized

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

