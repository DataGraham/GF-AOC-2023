package aoc2022.day

import println
import readInput
import require

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day03/Day03_test")
    check(part1(testInput).also { it.println() } == 157)
    check(part2(testInput).also { it.println() } == 70)

    val input = readInput("aoc2022/day03/Day03")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

@JvmInline
value class Bag(val contents: String)

fun part1(input: List<String>) = input
    .map { contents -> Bag(contents) }
    .sumOf { bag -> bag.commonCharacter.priority }

@JvmInline
value class Group(val elves: List<String>)

fun part2(input: List<String>) =
    input
        .chunked(3)
        .map { groupElves -> Group(groupElves) }
        .sumOf { group -> group.commonCharacter.priority }

private val Group.commonCharacter
    get() = elves
        .map { elf -> elf.toSet() }
        .reduce { intersection, elf -> intersection intersect elf }
        .require { size == 1 }
        .first()

private val Bag.commonCharacter
    get() = compartments
        // TODO: The reduce-based Group.commonCharacter above is a generalized version of this!
        .run { first intersect second }
        .require { size == 1 }
        .first()

val Bag.compartments: Pair<Set<Char>, Set<Char>>
    get() {
        val (compartmentA, compartmentB) =
            contents.chunked(contents.length / 2)
                .require { size == 2 }
                .map { compartment -> compartment.toSet() }
        return compartmentA to compartmentB
    }

val Char.priority get() = if (isLowerCase()) this - 'a' + 1 else this - 'A' + 27

