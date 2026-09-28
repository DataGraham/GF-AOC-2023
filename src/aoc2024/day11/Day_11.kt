package aoc2024.day11

import println
import readInput
import subString
import java.math.BigInteger

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day11/Day11_test")
    check(part1(testInput).also { it.println() } == 55312)
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("aoc2024/day11/Day11")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) = input.parseStoneNumbers().evolvedStoneCountRecursive(evolutionCount = 25)

fun part2(input: List<String>) = input.parseStoneNumbers().evolvedStoneCountRecursive(evolutionCount = 75)

private fun List<String>.evolvedStoneCount(evolutionCount: Int) =
    parseStoneNumbers()
        .stoneEvolution()
        .take(evolutionCount + 1)
        .last()
        .size

private fun List<BigInteger>.evolvedStoneCountRecursive(evolutionCount: Int): Int {
    return if (size == 1) {
        if (evolutionCount == 1)
            first().evolve().size
        else
            first().evolve().evolvedStoneCountRecursive(evolutionCount = evolutionCount - 1)
    } else listOf(first()).evolvedStoneCountRecursive(evolutionCount = evolutionCount) +
        drop(1).evolvedStoneCountRecursive(evolutionCount = evolutionCount)
}

fun List<String>.parseStoneNumbers() =
    first().split(' ').map { it.toBigInteger() }

fun List<BigInteger>.stoneEvolution() =
    generateSequence(this) { stones ->
        stones.flatMap { stone -> stone.evolve() }
    }

fun BigInteger.evolve(): List<BigInteger> =
    if (this == BigInteger.ZERO) listOf(BigInteger.ONE)
    else {
        val digits = toString()
        if (digits.length % 2 == 0) (digits.length / 2)
            .let { half ->
                listOf(0 to half, half to half)
                    .map { (start, length) ->
                        digits.subString(startIndex = start, length = length)
                    }
                    .map(String::toBigInteger)
            }
        else listOf(this * BigInteger.valueOf(2024L))
    }