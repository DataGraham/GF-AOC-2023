package aoc2022.day07

import aoc2022.day07.FileSystemItem.DirectoryItem
import aoc2022.day07.TerminalLine.Command.ChangeDirectoryCommand
import aoc2022.day07.TerminalLine.Listing.DirectoryListing
import aoc2022.day07.TerminalLine.Listing.FileListing
import aoc2022.day07.parsing.UniversalTerminalLineParser
import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day07/Day07_test")
    check(part1(testInput).also { it.println() } == 95437)
    //check(aoc2022.day07.part2(testInput).also { it.println() } == 1)

    //val input = readInput("aoc2022/day07/Day07")
    //println("Part 1 Answer: ${aoc2022.day07.part1(input)}")
    //println("Part 2 Answer: ${aoc2022.day07.part2(input)}")
}

fun part1(input: List<String>): Int {
    val terminalLines = with(UniversalTerminalLineParser()) { input.map { line -> parse(line) } }
        .apply { joinToString(separator = "\n").println() }
    val rootDirectory = DirectoryItem("/", parent = null)
    var currentDirectory: DirectoryItem? = null
    terminalLines.forEach { terminalLine ->
        when (terminalLine) {
            is ChangeDirectoryCommand -> {
                currentDirectory =
                    if (terminalLine.directoryName == rootDirectory.name) rootDirectory
                    else currentDirectory!!.children.filterIsInstance<DirectoryItem>().first {
                        it.name == terminalLine.directoryName
                    }
            }

            TerminalLine.Command.ListCommand -> {} // Nothing to do really

            is TerminalLine.Listing -> currentDirectory!!.children += when (terminalLine) {
                is FileListing -> FileSystemItem.FileItem(name = terminalLine.name, size = terminalLines.size)
                is DirectoryListing -> DirectoryItem(name = terminalLine.name, parent = currentDirectory)
            }
        }
    }
    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}

sealed class FileSystemItem {
    data class FileItem(val name: String, val size: Int) : FileSystemItem()
    data class DirectoryItem(val name: String, val parent: DirectoryItem?) : FileSystemItem() {
        val children = mutableListOf<FileSystemItem>()
    }
}