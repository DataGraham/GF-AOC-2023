package aoc2024.day11

import bifurcate
import println
import readInput

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

fun List<String>.parseStoneNumbers() =
    first().split(' ').map { it.toLong() }

private fun List<Long>.evolvedStoneCount(evolutionCount: Int): Long =
    sumOf { stone -> stone.evolvedStoneCount(evolutionCount = evolutionCount) }

val memoizedEvolvedStoneCountsByStone = mutableMapOf<Long, MutableMap<Int, Long>>()

fun Long.evolvedStoneCount(evolutionCount: Int) =
    getMemoizedEvolvedStoneCount(evolutionCount = evolutionCount)
        ?: computeEvolvedStoneCount(evolutionCount = evolutionCount)
            .also { evolvedStoneCount ->
                memoizeEvolvedStoneCount(
                    evolutionCount = evolutionCount,
                    evolvedStoneCount = evolvedStoneCount
                )
            }

private fun Long.getMemoizedEvolvedStoneCount(evolutionCount: Int) =
    memoizedEvolvedStoneCountsByStone[this]?.get(evolutionCount)

private fun Long.memoizeEvolvedStoneCount(evolutionCount: Int, evolvedStoneCount: Long) {
    memoizedEvolvedStoneCountsByStone
        .getOrPut(this) { mutableMapOf() }
        .putIfAbsent(evolutionCount, evolvedStoneCount)
}

private fun Long.computeEvolvedStoneCount(evolutionCount: Int) = (
    if (evolutionCount == 1)
    // TODO: In theory, I actually only need to know the count here, not the actual number(s)!
        evolve().size.toLong()
    else
        evolve().evolvedStoneCount(evolutionCount = evolutionCount - 1)
    )

fun Long.evolve(): List<Long> =
    if (this == 0L) listOf(1L)
    else splitDigits() ?: listOf(this * 2024L)

private fun Long.splitDigits(): List<Long>? =
    toString()
        .takeIf { digits -> digits.length % 2 == 0 }
        ?.bifurcate()
        ?.map(String::toLong)
