package aoc2024.day06

import Direction
import Direction.*
import Position
import allPositions
import findCycle
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
    check(part2(testInput).also { it.println() } == 6)

    val input = readInput("aoc2024/day06/Day06")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) = Lab.fromInput(input).getGuardPositionCount()

fun part2(input: List<String>) = Lab.fromInput(input).getLoopCausingAddedObstaclePositionCount()

class Lab(
    // TODO: Use a sealed class instead of char
    private val grid: List<List<Char>>
) {
    fun getGuardPositionCount() = getGuardPath().map { it.position }.toSet().size

    fun getLoopCausingAddedObstaclePositionCount() =
        grid
            .allPositions()
            .filter { !it.isObstacle }
            .filter { it != guardStartPosition }
            .filter { it != guardStartPosition move guardStartDirection }
            .count { addedObstaclePosition ->
                findCycle(
                    initial = initialGuardState,
                    next = {
                        nextGuardState(
                            guardState = this,
                            addedObstaclePosition = addedObstaclePosition
                        )
                    }
                ) != null
            }

    private val guardStartPosition = grid
        .allPositions()
        .first { position -> grid[position] in guardDirections.keys }
    private val guardStartDirection = guardDirections[grid[guardStartPosition]]!!
    private val initialGuardState = GuardState(position = guardStartPosition, direction = guardStartDirection)

    private val Position.isObstacle get() = grid[this] == OBSTACLE

    private fun getGuardPath() =
        generateSequence(seed = initialGuardState) { guardState ->
            nextGuardState(guardState = guardState)
        }

    private fun nextGuardState(
        guardState: GuardState,
        addedObstaclePosition: Position? = null
    ): GuardState? {
        val nextPosition = guardState.position move guardState.direction
        return when {
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
