package aoc2024.day06

import Direction
import Direction.*
import Position
import allPositions
import get
import isPositionValid
import move
import println
import readInput
import turnRight90Degrees

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day06/Day06_test")
    check(part1(testInput).also { it.println() } == 41)
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("aoc2024/day06/Day06")
    println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) = Lab.fromInput(input).getGuardPositionCount()

class Lab(
    // TODO: Use a sealed class instead of char
    private val grid: List<List<Char>>
) {
    fun getGuardPositionCount() = getGuardPath().map { it.position }.toSet().size

    private val guardStarPosition = grid
        .allPositions()
        .first { position -> grid[position] in guardDirections.keys }
    private val guardStartDirection = guardDirections[grid[guardStarPosition]]!!

    private val Position.isObstacle get() = grid[this] == OBSTACLE

    private fun getGuardPath(addedObstaclePosition: Position? = null) =
        generateSequence(
            seed = GuardState(position = guardStarPosition, direction = guardStartDirection)
        ) { guardState ->
            val nextPosition = guardState.position move guardState.direction
            when {
                !grid.isPositionValid(nextPosition) -> null
                nextPosition.isObstacle || nextPosition == addedObstaclePosition ->
                    guardState.copy(direction = guardState.direction.turnRight90Degrees)
                else -> guardState.copy(position = nextPosition)
            }
        }

    data class GuardState(
        val position: Position,
        val direction: Direction
    )

    companion object {
        private const val OBSTACLE = '#'

        private val guardDirections = mapOf(
            '^' to Up,
            'v' to Down,
            '<' to Left,
            '>' to Right
        )
    }
}

fun Lab.Companion.fromInput(input: List<String>) = Lab(
    grid = input.map { line -> line.toCharArray().toList() }
)

fun part2(input: List<String>): Int {
    return input.size
}
