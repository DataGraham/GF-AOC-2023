package aoc2022.day07

import aoc2022.day07.FileSystemBuilder.Companion.buildFileSystem
import aoc2022.day07.FileSystemItem.DirectoryItem
import aoc2022.day07.parsing.UniversalTerminalLineParser.Companion.parseTerminalLines
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.runBlocking
import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day07/Day07_test")
    check(part1(testInput).also { it.println() } == 95437)
    //check(aoc2022.day07.part2(testInput).also { it.println() } == 1)

    val input = readInput("aoc2022/day07/Day07")
    println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${aoc2022.day07.part2(input)}")
}

fun part1(input: List<String>): Int = runBlocking {
    input.parseTerminalLines()
        .buildFileSystem()
        .allItems
        .filterIsInstance<DirectoryItem>()
        .map { directoryItem -> directoryItem.sizeInBytes }
        .filter { directorySize -> directorySize <= 100000 }
        .reduce { totalSize, directorySize -> totalSize + directorySize }
}

fun part2(input: List<String>): Int {
    return input.size
}

