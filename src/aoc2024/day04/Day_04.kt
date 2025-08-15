package aoc2024.day04

import Direction
import Position
import allPositions
import get
import isEdgePosition
import isPositionValid
import move
import path
import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day04/Day04_test")
    check(part1(testInput).also { it.println() } == 18)
    check(part2(testInput).also { it.println() } == 9)

    val input = readInput("aoc2024/day04/Day04")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val targetWord = "XMAS"
    return input
        .map { line -> line.toList() }
        .let { grid ->
            grid
                .allPositions()
                .sumOf { startPosition ->
                    grid
                        .wordsStartingAt(
                            startPosition = startPosition,
                            length = targetWord.length
                        )
                        .count { it == targetWord }
                }
        }
}

fun part2(input: List<String>): Int {
    val grid = input.map { line -> line.toList() }
    val targetSet = setOf('M', 'S')
    return grid
        .allPositions()
        .filterNot { position -> grid.isEdgePosition(position) }
        .count { centerPosition ->
            grid[centerPosition] == 'A' &&
                listOf(
                    listOf(Direction.UpLeft, Direction.DownRight),
                    listOf(Direction.UpRight, Direction.DownLeft)
                ).all { diagonal ->
                    diagonal
                        .map { direction -> grid[centerPosition move direction] }
                        .toSet() == targetSet
                }
        }
}

private fun List<List<Char>>.wordsStartingAt(startPosition: Position, length: Int) =
    Direction.all.map { direction ->
        word(
            startPosition = startPosition,
            direction = direction,
            length = length
        )
    }

private fun List<List<Char>>.word(startPosition: Position, direction: Direction, length: Int) =
    startPosition
        .path(direction)
        .take(length)
        .mapNotNull { position -> if (isPositionValid(position)) this[position] else null }
        .let { chars -> String(chars.toList().toCharArray()) }
