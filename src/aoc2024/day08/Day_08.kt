package aoc2024.day08

import Position
import allPositions
import println
import readInput
import get

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day08/Day08_test")
    check(part1(testInput).also { it.println() } == 14)
    //check(part2(testInput).also { it.println() } == 1)

    //val input = readInput("aoc2024/day08/Day08")
    //println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

private const val EMPTY = '.'

fun part1(input: List<String>): Int {
    val grid = input.map { it.toCharArray().toList() }
    val frequencyPositions = grid.allPositions().groupBy { pos -> grid[pos] } - EMPTY
    return frequencyPositions.values.sumOf { antennaPositions ->
        grid.antiNodes(antennaPositions = antennaPositions).size
    }
}

fun part2(input: List<String>): Int {
    return input.size
}

fun List<List<Char>>.antiNodes(antennaPositions: List<Position>): List<Position> {
    // TODO: Actually find them
    return emptyList()
}