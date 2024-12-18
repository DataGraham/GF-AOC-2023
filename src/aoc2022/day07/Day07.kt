package aoc2022.day07

import aoc2022.day07.FileSystemBuilder.Companion.buildFileSystem
import aoc2022.day07.FileSystemItem.DirectoryItem
import aoc2022.day07.parsing.UniversalTerminalLineParser.Companion.parseTerminalLines
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import measurePerformance
import min
import println
import readInput
import sum

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day07/Day07_test")
    check(part1(testInput).also { it.println() } == 95437)
    check(part2(testInput).also { it.println() } == 24933642)

    val input = readInput("aoc2022/day07/Day07")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")

    measurePerformance(reps = 10000) {
        part2(input)
    }
}

private const val MAX_CANDIDATE_SIZE = 100000

fun part1(input: List<String>): Int = runBlocking {
    input
        .toFileSystem()
        .allDirectorySizes
        .filter { directorySize -> directorySize <= MAX_CANDIDATE_SIZE }
        .sum()
}

private const val TOTAL_SPACE = 70_000_000
private const val REQUIRED_SPACE = 30_000_000

fun part2(input: List<String>) = runBlocking {
    val root = input.toFileSystem()
    val usedSpace = root.sizeInBytes
    val unusedSpace = TOTAL_SPACE - usedSpace
    val spaceToFree = REQUIRED_SPACE - unusedSpace
    root
        .allDirectorySizes
        .filter { directorySize -> directorySize >= spaceToFree }
        .min()
}

private suspend fun List<String>.toFileSystem() = asFlow().parseTerminalLines().buildFileSystem()

private val DirectoryItem.allDirectorySizes
    get() = allItems
        .filterIsInstance<DirectoryItem>()
        .map { directoryItem -> directoryItem.sizeInBytes }