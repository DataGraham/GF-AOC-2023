package aoc2022.day12

import Direction
import Edge
import Position
import allPositions
import aoc2022.day12.HeightMapEntry.Type.End
import aoc2022.day12.HeightMapEntry.Type.Start
import aoc2022.day12.HeightMapParser.parseHeightMap
import get
import isPositionValid
import leastPathCost
import move
import positionOf
import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day12/Day12_test")
    check(part1(testInput).also { it.println() } == 31)
    check(part2(testInput).also { it.println() } == 29)

    val input = readInput("aoc2022/day12/Day12")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) = parseHeightMap(input).let { heightMap ->
    shortestPath(
        heightMap = heightMap,
        startPosition = heightMap.positionOf { heightMapEntry ->
            heightMapEntry.type == Start
        }!!
    )
}

fun part2(input: List<String>) = parseHeightMap(input).let { heightMap ->
    heightMap
        .allPositions()
        .filter { position -> heightMap[position].height == 0 }
        .mapNotNull { startPosition ->
            try {
                shortestPath(
                    heightMap = heightMap,
                    startPosition = startPosition
                )
            } catch (e: Exception) {
                null // If end can't be reached
            }
        }
        .min()
}

private fun shortestPath(heightMap: List<List<HeightMapEntry>>, startPosition: Position) =
    leastPathCost(
        start = startPosition,
        edges = { position ->
            nextPositions(heightMap, position).map { adjacentPosition ->
                Edge(adjacentPosition, 1)
            }
        },
        isEnd = { position -> heightMap[position].type == End }
    )

private fun nextPositions(
    heightMap: List<List<HeightMapEntry>>,
    position: Position
) = Direction.orthogonal
    .map { direction -> position move direction }
    .filter { adjacentPosition -> heightMap.isPositionValid(adjacentPosition) }
    .filter { adjacentPosition -> heightMap[adjacentPosition].height <= heightMap[position].height + 1 }


