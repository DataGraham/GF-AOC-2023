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

fun part1(input: List<String>): Int {
    val heights = input.map { line -> line.map { it.digitToInt() } }
    val trailheads = heights
        .allPositions()
        .filter { position -> heights[position] == 0 }
    return trailheads.sumOf { trailhead -> heights.trailScoreBySummitCount(trailhead = trailhead) }
}

fun part2(input: List<String>): Int {
    val heights = input.map { line -> line.map { it.digitToInt() } }
    val trailheads = heights
        .allPositions()
        .filter { position -> heights[position] == 0 }
    return trailheads.sumOf { trailhead -> heights.trailScoreByPathCount(trailhead = trailhead) }
}

fun List<List<Int>>.trailScoreBySummitCount(trailhead: Position): Int {
    require(this[trailhead] == 0)
    return reachableNines(trailhead)
        .toSet()
        .size
}

fun List<List<Int>>.trailScoreByPathCount(trailhead: Position): Int {
    require(this[trailhead] == 0)
    return reachableNines(trailhead).size
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