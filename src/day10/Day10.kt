package day10

import day10.Direction.*
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day10/Day10_test")
    check(part1(testInput) == 1)
    //check(part2(testInput) == 1)

    val input = readInput("day10/Day10")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}

enum class PipeShape(val character: Char, connections: Set<Direction>) {
    NorthSouth('|', setOf(North, South)),
    EastWest('-', setOf(East, West)),
    NorthEast('L', setOf(North, East)),
    NorthWest('J', setOf(North, West)),
    SouthWest('7', setOf(South, West)),
    SouthEast('F', setOf(South, East))
}

enum class Direction(val rowDelta: Int, colDelta: Int) {
    North(rowDelta = -1, colDelta = 0),
    South(rowDelta = 1, colDelta = 0),
    East(rowDelta = 0, colDelta = 1),
    West(rowDelta = 0, colDelta = -1);
}