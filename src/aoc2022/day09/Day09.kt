package aoc2022.day09

import aoc2022.day09.Direction.*
import println
import readInput
import requireSubstringAfter

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day09/Day09_test")
    check(part1(testInput).also { it.println() } == 21)
    // check(part2(testInput).also { it.println() } == 8)

    //val input = readInput("aoc2022/day09/Day09")
    //println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    input.map { it.parseMoveInstruction() }.joinToString(separator = "\n").println()
    return input.size
}

fun part2(input: List<String>) = input.size

private fun String.parseMoveInstruction() = MoveInstruction(
    direction = this[0].toDirection(),
    count = this.requireSubstringAfter(' ').toInt()
)

private fun Char.toDirection() = when(this) {
    'U' -> Up
    'D' -> Down
    'R' -> Right
    'L' -> Left
    else -> throw IllegalArgumentException("Unrecognized direction character $this")
}

private data class MoveInstruction(val direction: Direction, val count: Int)

private data class Position(val row: Int, val col: Int)

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
