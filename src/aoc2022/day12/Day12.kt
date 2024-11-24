package aoc2022.day12

import Direction
import aoc2022.day12.HeightMapEntry.EndEntry
import aoc2022.day12.HeightMapEntry.StartEntry
import day17.Edge
import day17.leastPathCost
import get
import isPositionValid
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

sealed class HeightMapEntry {
    data class HeightEntry(override val height: Int) : HeightMapEntry()

    data object StartEntry : HeightMapEntry() {
        override val height: Int
            get() = 0 // equivalent to 'a' (call it zero)
    }

    data object EndEntry : HeightMapEntry() {
        override val height: Int
            get() = 25 // equivalent to 'z' (25 more than 'a')
    }

    abstract val height: Int
}

fun part1(input: List<String>): Int {
    val heightMap = input.map { line ->
        line.map { character ->
            when (character) {
                'S' -> StartEntry
                'E' -> EndEntry
                in 'a'..'z' -> HeightMapEntry.HeightEntry(character - 'a')
                else -> throw IllegalArgumentException("Invalid height map character '$character'")
            }
        }
    }
    return leastPathCost(
        start = heightMap.positionOf(StartEntry)!!,
        edges = {
            Direction.entries
                .map { direction -> this move direction }
                .filter { adjacentPosition -> heightMap.isPositionValid(adjacentPosition) }
                .filter { adjacentPosition ->
                    heightMap[adjacentPosition].height <= heightMap[this].height + 1
                }
                .map { adjacentPosition -> Edge(adjacentPosition, 1) }
        },
        isEnd = { heightMap[this] == EndEntry }
    )
}

fun part2(input: List<String>) = input.size
