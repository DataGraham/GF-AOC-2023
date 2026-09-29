package aoc2024.day11

import println
import readInput
import subString
import java.math.BigInteger

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day11/Day11_test")
    check(part1(testInput).also { it.println() } == 55312L)
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("aoc2024/day11/Day11")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) = input.parseStoneNumbers().evolvedStoneCount(evolutionCount = 25)

fun part2(input: List<String>) = input.parseStoneNumbers().evolvedStoneCount(evolutionCount = 75)

private fun List<BigInteger>.evolvedStoneCount(evolutionCount: Int): Long =
    sumOf { it.evolvedStoneCount(evolutionCount = evolutionCount) }

fun List<String>.parseStoneNumbers() =
    first().split(' ').map { it.toBigInteger() }

val memoizedEvolvedStoneCountsByStone = mutableMapOf<BigInteger, MutableMap<Int, Long>>()

fun BigInteger.evolvedStoneCount(evolutionCount: Int) =
    getMemoizedEvolvedStoneCount(evolutionCount = evolutionCount)
        ?: computeEvolvedStoneCount(evolutionCount = evolutionCount)
            .also { evolvedStoneCount ->
                memoizeEvolvedStoneCount(
                    evolutionCount = evolutionCount,
                    evolvedStoneCount = evolvedStoneCount
                )
            }

private fun BigInteger.getMemoizedEvolvedStoneCount(evolutionCount: Int) =
    memoizedEvolvedStoneCountsByStone[this]?.get(evolutionCount)

private fun BigInteger.memoizeEvolvedStoneCount(evolutionCount: Int, evolvedStoneCount: Long) {
    memoizedEvolvedStoneCountsByStone
        .getOrPut(this) { mutableMapOf() }
        .putIfAbsent(evolutionCount, evolvedStoneCount)
}

private fun BigInteger.computeEvolvedStoneCount(evolutionCount: Int) = (
    if (evolutionCount == 1)
    // TODO: In theory, I actually only need to know the count here, not the actual number(s)!
        evolve().size.toLong()
    else
        evolve().evolvedStoneCount(evolutionCount = evolutionCount - 1)
    )

fun BigInteger.evolve(): List<BigInteger> {
    return if (this == BigInteger.ZERO) listOf(BigInteger.ONE)
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
}