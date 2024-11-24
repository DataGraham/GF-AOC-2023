package aoc2022.day12

import Direction
import Edge
import aoc2022.day12.HeightMapEntry.Type.*
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
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("aoc2022/day12/Day12")
    println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val heightMap = input.map { line ->
        line.map { character -> character.toHeightMapEntry() }
    }
    return leastPathCost(
        start = heightMap.positionOf { heightMapEntry2 -> heightMapEntry2.type == Start }!!,
        edges = { position ->
            Direction.entries
                .map { direction -> position move direction }
                .filter { adjacentPosition -> heightMap.isPositionValid(adjacentPosition) }
                .filter { adjacentPosition ->
                    heightMap[adjacentPosition].height <= heightMap[position].height + 1
                }
                .map { adjacentPosition -> Edge(adjacentPosition, 1) }
        },
        isEnd = { position -> heightMap[position].type == End }
    )
}

fun part2(input: List<String>) = input.size

data class HeightMapEntry(val height: Int, val type: Type) {
    enum class Type {
        Normal,
        Start,
        End
    }
}

private fun Char.toHeightMapEntry() = HeightMapEntry(
    height = toHeight(),
    type = toHeightMapEntryType()
)

private fun Char.toHeightMapEntryType() = when (this) {
    'S' -> Start
    'E' -> End
    in 'a'..'z' -> Normal
    else -> throw IllegalArgumentException("Invalid height map character '$this'")
}

private fun Char.toHeight(): Int = when (this) {
    'S' -> 'a'.toHeight()
    'E' -> 'z'.toHeight()
    in 'a'..'z' -> this - 'a'
    else -> throw IllegalArgumentException("Invalid height map character '$this'")
}