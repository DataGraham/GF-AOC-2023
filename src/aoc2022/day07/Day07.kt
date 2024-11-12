package aoc2022.day07

import aoc2022.day07.TerminalLine.Command.ChangeDirectoryCommand
import aoc2022.day07.TerminalLine.Command.ListCommand
import aoc2022.day07.TerminalLine.Output.DirectoryListing
import aoc2022.day07.TerminalLine.Output.FileListing
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

private sealed class TerminalLine {
    sealed class Command : TerminalLine() {
        data class ChangeDirectoryCommand(val directoryName: String) : Command()
        data object ListCommand : Command()
    }

    sealed class Output : TerminalLine() {
        data class FileListing(val name: String, val size: Int) : Output()
        data class DirectoryListing(val name: String) : Output()
    }
}

private class TerminalParser {
    private val directoryRegex by lazy { Regex("""dir (.+)""") }
}

private interface TerminalLineParser {
    fun parse(line: String): TerminalLine?
}

private abstract class RegexTerminalLineParser(private val pattern: String) : TerminalLineParser {
    private val regex by lazy { Regex(pattern) }

    final override fun parse(line: String) = regex.matchEntire(line)?.let { match ->
        parseMatch(captures = match.groupValues.drop(1))
    }

    protected abstract fun parseMatch(captures: List<String>): TerminalLine
}

private class ChangeDirectoryParser : RegexTerminalLineParser("""\$ cd (.+)""") {
    override fun parseMatch(captures: List<String>) =
        ChangeDirectoryCommand(directoryName = captures.first())
}

private class ListCommandParser : RegexTerminalLineParser("""\$ ls""") {
    override fun parseMatch(captures: List<String>) = ListCommand
}

private class FileListingParser : RegexTerminalLineParser("""(\d+) (.+)""") {
    override fun parseMatch(captures: List<String>) =
        FileListing(size = captures[0].toInt(), name = captures[2])
}

private class DirectoryListingParser : RegexTerminalLineParser("""dir (.+)""") {
    override fun parseMatch(captures: List<String>) = DirectoryListing(name = captures.first())
}

fun part1(input: List<String>): Int {
    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}
