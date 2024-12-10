package aoc2022.day17

import DeltaPosition
import Direction
import Position
import move
import plus
import println
import readInput
import toInfiniteSequence

fun main() {
    // test()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day17/Day17_test")
    val input = readInput("aoc2022/day17/Day17")

    check(part1(testInput).also { it.println() } == 3068)
    println("Part 1 Answer: ${part1(input)}")

    //check(part2(testInput).also { it.println() } == 1)
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val rockToDrops = rockSequence.toInfiniteSequence().iterator()
    val jetDirections = parseDirections(input).toInfiniteSequence().iterator()
    return Chamber().apply {
        repeat(2022) {
            dropRock(rockToDrops.next()) { jetDirections.next() }
        }
    }.towerHeight
}

fun part2(input: List<String>): Int {
    return input.size
}

class Chamber {
    companion object {
        private const val MAX_COL = 6
        private const val MAX_ROW = 0
    }

    private val rockPositions = mutableSetOf<Position>()

    fun dropRock(rock: Rock, nextJetDirection: () -> Direction) {
        var movingRock = rock + DeltaPosition(deltaRow = -3 - towerHeight, deltaCol = 2)
        // TODO: implement rock falling/blowing as a sequence
        var done = false
        while (!done) {
            val jetDirection = nextJetDirection()
            if (movingRock canMove jetDirection) movingRock = movingRock move jetDirection
            if (movingRock canMove Direction.Down) movingRock = movingRock move Direction.Down
            else done = true
        }
        rockPositions += movingRock
    }

    // TODO: Is there a more readable way of calculating this?
    val towerHeight get() = -(rockPositions.minOfOrNull { it.row } ?: 1) + 1

    // TODO: Can we make a "conditional move" so we don't have to recalculate the movement
    //  once to check and then again to actually move?
    //  Maybe just check for the moved rock being valid and then return it or the original
    //  (or null I guess so we can tell? or _maybe_ throw an exception?)
    private infix fun Rock.canMove(direction: Direction) =
        all { position -> (position move direction).isAvailable }

    private val Position.isAvailable get() = isValid && this !in rockPositions

    private val Position.isValid get() = row <= MAX_ROW && col in (0..MAX_COL)
}

private typealias Rock = Set<Position>

private infix fun Rock.move(direction: Direction): Rock =
    map { position -> position move direction }.toSet()

private operator fun Rock.plus(deltaPosition: DeltaPosition) =
    map { position -> position + deltaPosition }.toSet()

// Note: All of these rocks are normalized such that the maximum row is zero and the minimum column is zero

private val HorizontalRock: Rock = setOf(
    Position(0, 0),
    Position(0, 1),
    Position(0, 2),
    Position(0, 3),
)

private val CrossRock: Rock = setOf(
    Position(-2, 1),
    Position(-1, 0),
    Position(-1, 1),
    Position(-1, 2),
    Position(0, 1)
)

private val LRock: Rock = setOf(
    Position(0, 0),
    Position(0, 1),
    Position(0, 2),
    Position(-1, 2),
    Position(-2, 2)
)

private val VerticalRock: Rock = setOf(
    Position(0, 0),
    Position(-1, 0),
    Position(-2, 0),
    Position(-3, 0),
)

private val SquareRock: Rock = setOf(
    Position(0, 0),
    Position(0, 1),
    Position(-1, 0),
    Position(-1, 1),
)

val rockSequence = listOf(HorizontalRock, CrossRock, LRock, VerticalRock, SquareRock)

private fun parseDirections(input: List<String>) =
    input.first().map { char -> parseDirection(char) }

private fun parseDirection(char: Char) = when (char) {
    '<' -> Direction.Left
    '>' -> Direction.Right
    else -> throw IllegalArgumentException()
}
