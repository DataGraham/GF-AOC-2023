package aoc2024.day11

import println
import readInput
import subString

private const val EVOLUTION_COUNT = 25

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day11/Day11_test")
    check(part1(testInput).also { it.println() } == 55312)
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("aoc2024/day11/Day11")
    println("Part 1 Answer: ${part1(input)}")
    // println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) =
    input
        .parseStoneNumbers()
        .stoneEvolution()
        .take(EVOLUTION_COUNT + 1)
        .last()
        .size

fun part2(input: List<String>): Int {
    return input.size
}

fun List<String>.parseStoneNumbers() =
    first().split(' ').map { it.toInt() }

fun List<Int>.stoneEvolution() =
    generateSequence(this) { stones ->
        stones.flatMap { stone -> stone.evolve() }
    }

fun Int.evolve(): List<Int> =
    if (this == 0) listOf(1)
    else {
        val digits = toString()
        if (digits.length % 2 == 0) (digits.length / 2)
            .let { half ->
                listOf(0 to half, half to half)
                    .map { (start, length) ->
                        digits.subString(startIndex = start, length = length)
                    }
                    .map(String::toInt)
            }
        else listOf(this * 2024)
    }