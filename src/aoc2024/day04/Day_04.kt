package aoc2024.day04

import Direction
import Position
import allPositions
import get
import isPositionValid
import path
import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day04/Day04_test")
    check(part1(testInput).also { it.println() } == 18)
    //check(part2(testInput).also { it.println() } == 1)

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
                    Direction.entries.count { direction ->
                        grid.word(
                            startPosition = startPosition,
                            direction = direction,
                            length = targetWord.length
                        ) == targetWord
                    }
                }
        }
}

fun part2(input: List<String>): Int {
    return input.size
}

private fun List<List<Char>>.word(startPosition: Position, direction: Direction, length: Int) =
    startPosition
        .path(direction)
        .take(length)
        .mapNotNull { position -> if (isPositionValid(position)) this[position] else null }
        .let { chars -> String(chars.toList().toCharArray()) }
