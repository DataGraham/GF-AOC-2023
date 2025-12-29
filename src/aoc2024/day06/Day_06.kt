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

fun part1(input: List<String>) = parseLab(input).getGuardPositionCount()

fun part2(input: List<String>) = parseLab(input).getLoopCausingAddedObstaclePositionCount()

fun parseLab(input: List<String>) = Lab(
    grid = input.map { line ->
        line.map { char ->
            char.toLabState()
        }
    }
)

sealed class LabState {
    data object Empty : LabState()
    data object Obstacle : LabState()
    data class Guard(val direction: Direction) : LabState()
}

private fun Char.toLabState() = when (this) {
    '.' -> LabState.Empty
    '#' -> LabState.Obstacle
    else -> LabState.Guard(direction = guardDirections[this]!!)
}

private val guardDirections = mapOf(
    '^' to Up,
    'v' to Down,
    '<' to Left,
    '>' to Right
)

class Lab(private val grid: List<List<LabState>>) {
    fun getGuardPositionCount() = getGuardPath().map { it.position }.toSet().size

    fun getLoopCausingAddedObstaclePositionCount() =
        possibleAddedObstacleLocations.count { addedObstaclePosition ->
            addedObstaclePosition.addingObstacleCausesGuardLoop()
        }

    private fun Position.addingObstacleCausesGuardLoop() = findCycle(
        initial = initialGuardState,
        next = {
            nextGuardState(
                guardState = this,
                addedObstaclePosition = this@addingObstacleCausesGuardLoop
            )
        }
    ) != null

    private val initialGuardState = grid
        .allPositions()
        .firstNotNullOf { position ->
            (grid[position] as? LabState.Guard)?.let { guard ->
                GuardState(
                    position = position,
                    direction = guard.direction
                )
            }
        }

    private val possibleAddedObstacleLocations by lazy {
        grid
            .allPositions()
            .filter { !it.isObstacle }
            .filter { it != initialGuardState.position }
            .filter { it != initialGuardState.position move initialGuardState.direction }
    }

    private val Position.isObstacle get() = grid[this] is LabState.Obstacle

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

    private data class GuardState(
        val position: Position,
        val direction: Direction
    )
}
