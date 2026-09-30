package aoc2024.day12

import Direction
import Position
import allPositions
import get
import isPositionValid
import move
import println
import readInput
import set

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day12/Day12_test")
    check(part1(testInput).also { it.println() } == 1930)
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("aoc2024/day12/Day12")
    println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) = input.toPlots().fenceCost()

fun part2(input: List<String>): Int {
    return input.size
}

private fun List<String>.toPlots() =
    map { line -> line.toCharArray().toList() }

private fun List<List<Char>>.fenceCost() =
    RegionFinder
        .findRegions(this)
        .sumOf { region -> fenceCost(region) }

private class RegionFinder private constructor(
    private val plots: List<List<Char>>
) {
    companion object {
        fun findRegions(plots: List<List<Char>>) =
            RegionFinder(plots).findRegions()
    }

    private val visited =
        MutableList(plots.size) {
            MutableList(plots.first().size) { false }
        }

    fun findRegions(): List<Set<Position>> =
        plots
            .allPositions()
            .mapNotNull { position -> findNewRegionFrom(position) }

    private fun findNewRegionFrom(startPosition: Position): Set<Position>? {
        if (visited[startPosition]) return null
        visited[startPosition] = true
        val regionLabel = plots[startPosition]
        return Direction
            .orthogonal
            .asSequence()
            .map { direction -> startPosition move direction }
            .filter { nextPosition -> plots.isPositionValid(nextPosition) && plots[nextPosition] == regionLabel }
            .mapNotNull { validNextPosition -> findNewRegionFrom(validNextPosition) }
            .flatten()
            .toSet() + startPosition
    }
}

private fun List<List<Char>>.fenceCost(region: Set<Position>) =
    region.size * perimeter(region)

private fun List<List<Char>>.perimeter(region: Set<Position>) =
    region.sumOf { position ->
        Direction
            .orthogonal
            .map { direction -> position move direction }
            .count { nextPosition ->
                !isPositionValid(nextPosition) || this[nextPosition] != this[position]
            }
    }
