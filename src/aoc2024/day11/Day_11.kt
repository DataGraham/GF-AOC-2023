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

fun part1(input: List<String>) = input.parseStoneNumbers().evolvedStoneCountIterativeRecursion(evolutionCount = 25)

fun part2(input: List<String>) = input.parseStoneNumbers().evolvedStoneCountIterativeRecursion(evolutionCount = 75)

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

private fun List<BigInteger>.evolvedStoneCountIterativeRecursion(evolutionCount: Int): Long =
    sumOf { it.evolvedStoneCount(evolutionCount = evolutionCount) }
//        if (evolutionCount == 1)
//    // TODO: In theory, I actually only need to know the count here, not the actual number(s)!
//        sumOf { it.evolve().size }
//    else
//        sumOf { it.evolve().evolvedStoneCountIterativeRecursion(evolutionCount = evolutionCount - 1) }

fun List<String>.parseStoneNumbers() =
    first().split(' ').map { it.toBigInteger() }

fun List<BigInteger>.stoneEvolution() =
    generateSequence(this) { stones ->
        stones.flatMap { stone -> stone.evolve() }
    }

// val hitCount = mutableMapOf<BigInteger, Int>()
// val evolutionCountRequestsByStone = mutableMapOf<BigInteger, MutableList<Int>>()
val memoizedEvolvedStoneCountsByStone = mutableMapOf<BigInteger, MutableMap<Int, Long>>()

fun BigInteger.evolvedStoneCount(evolutionCount: Int): Long {
    //    evolutionCountRequestsByStone
    //        .getOrPut(this) { mutableListOf() }
    //        .add(evolutionCount)
    return memoizedEvolvedStoneCountsByStone[this]?.get(evolutionCount) ?: (
        if (evolutionCount == 1)
        // TODO: In theory, I actually only need to know the count here, not the actual number(s)!
            evolve().size.toLong()
        else
            evolve().evolvedStoneCountIterativeRecursion(evolutionCount = evolutionCount - 1)
        ).also { result ->
            memoizedEvolvedStoneCountsByStone
                .getOrPut(this) { mutableMapOf() }
                .putIfAbsent(evolutionCount, result)
        }
}

fun BigInteger.evolve(): List<BigInteger> {
    // hitCount.merge(this, 1) { a, b -> a + b }
    // hitCount.compute(this) { _, count -> if (count == null) 1 else count + 1 }
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