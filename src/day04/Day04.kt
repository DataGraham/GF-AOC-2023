package day04

import readInput
import requireSubstringAfter

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day04/Day04_test")
    check(part1(testInput) == 13)
    //check(part2(testInput) == 1)

    val input = readInput("day04/Day04")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    return input.sumOf { line ->
        line.winnerCount().let { winnerCount ->
            if (winnerCount == 0) 0
            else 1 shl (winnerCount - 1)
        }
    }
}

fun part2(input: List<String>): Int {
    val copies = IntArray(input.size) { 1 }
    input.forEachIndexed { index, line ->
        (index + 1 ..< index + 1 + line.winnerCount()).forEach { copies[it] += copies[index] }
    }
    return copies.sum()
}

private fun String.winnerCount() =
    requireSubstringAfter(':')
        .split('|')
        .takeIf { it.size == 2 }!!
        .map { numbersString ->
            numbersString
                .trim()
                .split(' ')
                .filter(String::isNotEmpty) // empty strings can result from consecutive delimiters (spaces)
                .map(String::toInt)
        }.let { (winners, mine) -> mine.intersect(winners.toSet()).size }
