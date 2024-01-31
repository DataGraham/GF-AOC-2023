package day17

import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day17/Day17_test")
    check(part1(testInput).also { it.println() } == 102)
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("day17/Day17")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}

data class Node(
    val position: Position,
    val direction: Direction,
    val straightMoveCount: Int
)

infix fun Node.move(moveDirection: Direction) = Node(
    position = position move direction,
    direction = moveDirection,
    straightMoveCount = if (moveDirection == direction) straightMoveCount + 1 else 1
)

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