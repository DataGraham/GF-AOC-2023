package aoc2022.day07

import aoc2022.day07.FileSystemItem.DirectoryItem
import aoc2022.day07.TerminalLine.Command.ChangeDirectoryCommand
import aoc2022.day07.TerminalLine.Listing
import aoc2022.day07.TerminalLine.Listing.DirectoryListing
import aoc2022.day07.TerminalLine.Listing.FileListing
import aoc2022.day07.parsing.UniversalTerminalLineParser
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
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

fun part1(input: List<String>): Int {
    val terminalLines = with(UniversalTerminalLineParser()) { input.map { line -> parse(line) } }
    return runBlocking {
        buildFileSystem(terminalLines)
            .allDirectories
            .map { it.sizeInBytes }
            .filter { it <= 100000 }
            .reduce { acc, size -> acc + size }
    }
}

fun part2(input: List<String>): Int {
    return input.size
}

private fun buildFileSystem(terminalLines: List<TerminalLine>): DirectoryItem {
    val rootDirectory = DirectoryItem("/", parent = null)
    var currentDirectory: DirectoryItem? = null
    terminalLines.forEach { terminalLine ->
        when (terminalLine) {
            is ChangeDirectoryCommand -> {
                currentDirectory =
                    when (terminalLine.directoryName) {
                        rootDirectory.name -> rootDirectory
                        ".." -> currentDirectory!!.parent
                        else -> currentDirectory!!.children.filterIsInstance<DirectoryItem>().first {
                            it.name == terminalLine.directoryName
                        }
                    }
            }

            TerminalLine.Command.ListCommand -> {} // Nothing to do really

            is Listing -> currentDirectory!!.children += when (terminalLine) {
                is FileListing -> FileSystemItem.FileItem(
                    name = terminalLine.fileName,
                    sizeInBytes = terminalLine.fileSize
                )

                is DirectoryListing -> DirectoryItem(name = terminalLine.directoryName, parent = currentDirectory)
            }
        }
    }
    return rootDirectory
}

sealed class FileSystemItem {
    abstract val name: String
    abstract val sizeInBytes: Int

    data class FileItem(override val name: String, override val sizeInBytes: Int) : FileSystemItem()
    data class DirectoryItem(override val name: String, val parent: DirectoryItem?) : FileSystemItem() {
        val children = mutableListOf<FileSystemItem>()
        override val sizeInBytes get() = children.sumOf { it.sizeInBytes }

        @OptIn(ExperimentalCoroutinesApi::class)
        val allDirectories: Flow<DirectoryItem> = flowOf(this)
            .onCompletion { emitAll(childDirectories.flatMapConcat { it.allDirectories }) }

        private val childDirectories by lazy { children.asFlow().filterIsInstance<DirectoryItem>() }
    }
}