package aoc2022.day07

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
        data class ChangeDirectory(val directoryName: String) : Command()
        data object List : Command()
    }

    sealed class Output : TerminalLine() {
        data class File(val name: String, val size: Int) : Output()
        data class Directory(val name: String) : Output()
    }
}

fun part1(input: List<String>): Int {
    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}
