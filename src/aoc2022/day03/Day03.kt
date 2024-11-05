package aoc2022.day

import println
import readInput
import require

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day03/Day03_test")
    check(part1(testInput).also { it.println() } == 157)
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("aoc2022/day03/Day03")
    println("Part 1 Answer: ${part1(input)}")
    //    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    return input.sumOf { bag ->
        val compartmentSize = bag.length / 2
        val compartment1 = bag.substring(0..<compartmentSize)
        val compartment2 = bag.substring(startIndex = compartmentSize)
        val common = compartment1.toSet() intersect compartment2.toSet()
        common.require { size == 1 }.first().priority
    }
}

val Char.priority get() = if (isLowerCase()) this - 'a' + 1 else this - 'A' + 27

fun part2(input: List<String>): Int {
    return input.size
}
