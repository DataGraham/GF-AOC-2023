package aoc2024.day10

import Direction
import Position
import allPositions
import get
import isPositionValid
import move
import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day10/Day10_test")
    check(part1(testInput).also { it.println() } == 36)
    check(part2(testInput).also { it.println() } == 81)

    val input = readInput("aoc2024/day10/Day10")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) =
    input.sumTrailheadsBy { trailhead -> reachableNines(startPosition = trailhead).toSet().size }

fun part2(input: List<String>) =
    input.sumTrailheadsBy { trailhead -> reachableNines(startPosition = trailhead).size }

private fun List<String>.sumTrailheadsBy(score: List<List<Int>>.(Position) -> Int) =
    parseHeights().run {
        trailheads().sumOf { trailhead -> score(trailhead) }
    }

private fun List<String>.parseHeights() =
    map { line -> line.map { it.digitToInt() } }

private fun List<List<Int>>.trailheads(): List<Position> {
    val trailheads = allPositions()
        .filter { position -> this[position] == 0 }
    return trailheads
}

fun List<List<Int>>.reachableNines(startPosition: Position): List<Position> {
    val heightHere = this[startPosition]
    val nextHeight = heightHere + 1
    val validNextPositions = Direction
        .orthogonal
        .map { direction -> startPosition move direction }
        .filter { nextPosition -> isPositionValid(nextPosition) && this[nextPosition] == nextHeight }
    return if (heightHere == 8)
        validNextPositions
    else validNextPositions
        .flatMap { validNextPosition -> reachableNines(startPosition = validNextPosition) }
}