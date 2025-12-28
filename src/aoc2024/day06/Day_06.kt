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
import java.security.Guard

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day06/Day06_test")
    check(part1(testInput).also { it.println() } == 41)
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("aoc2024/day06/Day06")
    println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

const val OBSTACLE = '#'

fun part1(input: List<String>): Int {
    val grid = input.map { it.toCharArray().toList() }

    //    val obstaclePositions = grid
    //        .allPositions()
    //        .filter { position -> grid[position] == OBSTACLE }
    //        .toSet()
    fun Position.isObstacle() = grid[this] == OBSTACLE
    val guardDirections = mapOf(
        '^' to Up,
        'v' to Down,
        '<' to Left,
        '>' to Right
    )
    val guardStarPosition = grid
        .allPositions()
        .first { position -> grid[position] in guardDirections.keys }
    val guardStartDirection = guardDirections[grid[guardStarPosition]]!!

    val guardPath = generateSequence(
        GuardState(
            position = guardStarPosition,
            direction = guardStartDirection
        )
    ) { guardState ->
        val nextPosition = guardState.position move guardState.direction
        when {
            !grid.isPositionValid(nextPosition) -> null
            nextPosition.isObstacle() -> guardState.copy(direction = guardState.direction.turnRight90Degrees)
            else -> guardState.copy(position = nextPosition)
        }
    }

    return guardPath.map { it.position }.toSet().size
}

data class GuardState(
    val position: Position,
    val direction: Direction
)

fun part2(input: List<String>): Int {
    return input.size
}
