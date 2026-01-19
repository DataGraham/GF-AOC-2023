package aoc2024.day09

import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day09/Day09_test")
    check(part1(testInput).also { it.println() } == 1928L)
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("aoc2024/day09/Day09")
    println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) =
    input
        .first()
        .toDiskMapCodes()
        .decodeDiskMap()
        .apply { compact(this) }
        .asIterable()
        .checksum()

fun part2(input: List<String>): Int {
    return input.size
}

fun String.toDiskMapCodes() = map { it.digitToInt() }

fun List<Int>.decodeDiskMap() =
    flatMapIndexed { index, code ->
        // Start with a file of length <code> containing the file id (index)
        if (index % 2 == 0) List(code) { index / 2 }
        // Next is empty space of length <code>
        else List(code) { null }
    }.toTypedArray()

private fun compact(disk: Array<Int?>) {
    disk.indices.reversed().forEach { moveFrom ->
        if (disk[moveFrom] == null) return@forEach
        val moveTo = disk.indexOf(null)
        if (moveTo == -1 || moveTo > moveFrom) return
        disk[moveTo] = disk[moveFrom]
        disk[moveFrom] = null
        // TODO: We should start at moveTo+1 when looking for the next moveTo
    }
}

private fun Iterable<Int?>.checksum() =
    asSequence().mapIndexed { index, i -> index * (i ?: 0).toLong() }.sum()
