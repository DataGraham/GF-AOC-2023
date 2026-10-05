package aoc2024.day12

import Direction
import Position
import allPositions
import get
import isPositionValid
import move
import path
import println
import readInput
import set
import turnLeft90Degrees
import turnRight90Degrees

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day12/Day12_test")
    check(part1(testInput).also { it.println() } == 1930)
    check(part2(testInput).also { it.println() } == 1206)

    val input = readInput("aoc2024/day12/Day12")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) = input.toPlots().fenceCost { region -> region.fenceCost() }

fun part2(input: List<String>) = input.toPlots().fenceCost { region -> region.bulkFenceCost() }

private fun List<String>.toPlots() =
    map { line -> line.toCharArray().toList() }

private fun List<List<Char>>.fenceCost(regionCost: (Set<Position>) -> Int) =
    RegionFinder
        .findRegions(this)
        .sumOf(regionCost)

private class RegionFinder private constructor(
    private val plots: List<List<Char>>
) {
    companion object {
        fun findRegions(plots: List<List<Char>>) =
            RegionFinder(plots).findRegions()
    }

    private val visited =
        List(plots.size) {
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

private fun Set<Position>.fenceCost() = size * perimeter()

private fun Set<Position>.perimeter() =
    sumOf { position ->
        Direction
            .orthogonal
            .map { direction -> position move direction }
            .count { adjacentPosition -> adjacentPosition !in this }
    }

private fun Set<Position>.bulkFenceCost() = size * SideFinder.sideCount(this)

private class SideFinder private constructor(private val region: Set<Position>) {
    companion object {
        fun sideCount(region: Set<Position>) = SideFinder(region).sideCount()
    }

    // region visitation
    private val visited = mutableMapOf<Direction, MutableSet<Position>>()

    private fun visited(direction: Direction) =
        visited.getOrPut(direction) { mutableSetOf() }

    private fun Position.visit(direction: Direction) {
        visited(direction) += this
    }

    private fun Position.isVisited(sideDirection: Direction) = this in visited(sideDirection)
    // endregion

    private infix fun Position.hasEdge(sideDirection: Direction) =
        this move sideDirection !in region

    fun sideCount() = region.sumOf { position -> newSideCount(position) }

    private fun newSideCount(position: Position) =
        Direction.orthogonal.count { sideDirection ->
            position.hasNewSide(sideDirection)
        }

    private fun Position.hasNewSide(sideDirection: Direction) =
        (this hasEdge sideDirection && !isVisited(sideDirection))
            .also { hasNewSide -> if (hasNewSide) visitSide(sideDirection) }

    private fun Position.visitSide(sideDirection: Direction) {
        visit(sideDirection)
        listOf(
            sideDirection.turnLeft90Degrees,
            sideDirection.turnRight90Degrees
        ).forEach { traverseDirection ->
            remainderOfSide(
                sideDirection = sideDirection,
                traverseDirection = traverseDirection
            ).forEach { discoveredSidePosition ->
                discoveredSidePosition.visit(sideDirection)
            }
        }
    }

    private fun Position.remainderOfSide(sideDirection: Direction, traverseDirection: Direction) =
        path(traverseDirection)
            .drop(1)
            .takeWhile { traversedPosition ->
                traversedPosition in region && traversedPosition hasEdge sideDirection
            }
}
