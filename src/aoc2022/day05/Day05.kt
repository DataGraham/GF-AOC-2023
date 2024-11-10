package aoc2022.day05

import aoc2022.day05.CrateFinder.crateLabels
import aoc2022.day05.MoveFinder.move
import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day05/Day05_test")
    check(part1(testInput).also { it.println() } == "CMZ")
    check(part2(testInput).also { it.println() } == "MCD")

    val input = readInput("aoc2022/day05/Day05")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): String {
    /** Stacks from left ("1") to right,
    each with crates from top to bottom. */
    val crateStacks = input.crateStacks
    val moves = input.mapNotNull { it.move }
    moves.forEach { move ->
        with(move) {
            repeat(count) {
                crateStacks[destinationIndex].addFirst(
                    crateStacks[sourceIndex].removeFirst()
                )
            }
        }
    }
    return crateStacks.crateStackTops
}

fun part2(input: List<String>): String {
    val crateStacks = input.crateStacks
    val moves = input.mapNotNull { it.move }
    moves.forEach { move ->
        with(move) {
            val cratesToMove = List(count) { crateStacks[sourceIndex].removeFirst() }
            crateStacks[destinationIndex].addAll(0, cratesToMove)
        }
    }
    return crateStacks.crateStackTops
}

private val List<String>.crateStacks
    get() = crateRows
        .also { println("Crate rows: $it") }
        .transposed()
        .map { crateStack ->
            crateStack
                .filterNotNull()
                .toMutableList()
        }
        .also { println("Crate stacks: $it") }

private val List<String>.crateRows
    get() = map { line -> line.crateLabels }
        .takeWhile { crateLabels -> crateLabels.any { it != null } }

private object CrateFinder {
    private val crateSpaceRegex by lazy {
        Regex(""" {4}|\[(\p{Upper})] ?""")
    }

    val String.crateLabels
        get() = crateSpaceRegex
            .findAll(this)
            .map { it.groups[1]?.value?.first() }
            .toList()
}

fun <T> List<List<T?>>.transposed() =
    List(maxOf { it.size }) { i -> map { it.getOrNull(i) } }

data class Move(val sourceIndex: Int, val destinationIndex: Int, val count: Int) {
    override fun toString() = "Move $count from stack[$sourceIndex] to stack[$destinationIndex]"
}

private object MoveFinder {
    private val moveRegex by lazy {
        Regex("""^move (\d+) from (\d+) to (\d+)$""")
    }

    val String.move
        get() = moveRegex.find(this)
            ?.groupValues
            ?.drop(1)
            ?.map { it.toInt() }
            ?.let {
                Move(
                    count = it[0],
                    sourceIndex = it[1] - 1,
                    destinationIndex = it[2] - 1
                )
            }
}

private val List<List<Char>>.crateStackTops get() = String(map { it.first() }.toCharArray())

