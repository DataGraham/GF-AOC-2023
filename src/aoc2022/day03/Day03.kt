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

fun part1(input: List<String>) = input.sumOf { bag -> bag.commonCharacter.priority }

private val String.commonCharacter
    get() = compartments
        .run { first intersect second }
        .require { size == 1 }
        .first()

val String.compartments: Pair<Set<Char>, Set<Char>>
    get() {
        val (compartmentA, compartmentB) =
            chunked(length / 2)
                .require { size == 2 }
                .map { compartment -> compartment.toSet() }
        return compartmentA to compartmentB
    }

val Char.priority get() = if (isLowerCase()) this - 'a' + 1 else this - 'A' + 27

fun part2(input: List<String>): Int {
    return input.size
}
