package aoc2024.day08

import Position
import allPairs
import allPositions
import println
import readInput
import get
import isPositionValid
import minus
import path
import plus
import unaryMinus
import withMinimumMagnitude

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day08/Day08_test")
    check(part1(testInput).also { it.println() } == 14)
    check(part2(testInput).also { it.println() } == 34)

    val input = readInput("aoc2024/day08/Day08")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

private const val EMPTY = '.'

fun part1(input: List<String>) =
    input.antiNodeCount { antennaPositions -> antiNodes(antennaPositions) }

fun part2(input: List<String>) =
    input.antiNodeCount { antennaPositions -> antiNodesWithResonance(antennaPositions) }

private fun List<String>.antiNodeCount(findAntiNodes: List<List<Char>>.(List<Position>) -> List<Position>): Int {
    val grid = map { it.toCharArray().toList() }
    val frequencyPositions = grid.allPositions().groupBy { pos -> grid[pos] } - EMPTY
    return frequencyPositions
        .values
        .flatMap { antennaPositions -> grid.findAntiNodes(antennaPositions) }
        .toSet()
        .size
}

private fun List<List<Char>>.antiNodes(antennaPositions: List<Position>) =
    antennaPositions.allPairs().flatMap { (first, second) ->
        val delta = second - first
        listOf(
            second + delta,
            first - delta
        ).filter { isPositionValid(it) }
    }

private fun List<List<Char>>.antiNodesWithResonance(antennaPositions: List<Position>) =
    antennaPositions.allPairs().flatMap { (first, second) ->
        val deltaMinimized = (second - first).withMinimumMagnitude()
        val forwards = first.path(deltaPosition = deltaMinimized).takeWhile { isPositionValid(it) }
        val backwards = first.path(deltaPosition = -deltaMinimized).drop(1).takeWhile { isPositionValid(it) }
        (forwards + backwards).toList()
    }
