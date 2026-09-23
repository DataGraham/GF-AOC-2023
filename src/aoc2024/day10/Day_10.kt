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
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("aoc2024/day10/Day10")
    println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val heights = input.map { line -> line.map { it.digitToInt() } }
    val trailheads = heights
        .allPositions()
        .filter { position -> heights[position] == 0 }
    return trailheads.sumOf { trailhead -> heights.trailScore(trailhead = trailhead) }
}

fun part2(input: List<String>): Int {
    return input.size
}

fun List<List<Int>>.trailScore(trailhead: Position): Int {
    require(this[trailhead] == 0)
    return reachableNines(trailhead)
        //        .also {
        //            println("Reachable 9s from $trailhead are $it")
        //        }
        .size
}

fun List<List<Int>>.reachableNines(startPosition: Position): Set<Position> {
    val heightHere = this[startPosition]
    val nextHeight = heightHere + 1
    val validNextPositions = Direction
        .orthogonal
        .map { direction -> startPosition move direction }
        .filter { nextPosition -> isPositionValid(nextPosition) && this[nextPosition] == nextHeight }
    return if (heightHere == 8)
        validNextPositions.toSet()
    else validNextPositions
        .flatMap { validNextPosition -> reachableNines(startPosition = validNextPosition) }
        .toSet()
    //        .also {
    //            println("Reachable 9s from $startPosition are $it")
    //        }
}