package day12

import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day12/Day12_test")
    check(part1(testInput) == 21)
    //check(part2(testInput) == 1)

    val input = readInput("day12/Day12")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    return input.sumOf { line ->
        arrangementCount(line)
    }
}

fun part2(input: List<String>): Int {
    return input.size
}

fun arrangementCount(line: String): Int {
    val lineInfo = parseLine(line)
    lineInfo.println()
    return 0
}

data class LineInfo(
    val working: List<Boolean?>,
    val nonWorkingRuns: List<Int>
)

private fun parseLine(line: String) =
    line.split(' ')
        .takeIf { it.size == 2 }!!
        .let { (statesString, runsString) ->
            LineInfo(
                working = statesString.map { char -> char.toWorkingState() },
                nonWorkingRuns = runsString.split(',').map(String::toInt)
            )
        }

private fun Char.toWorkingState() = when(this) {
    '.' -> true
    '#' -> false
    '?' -> null
    else -> throw IllegalArgumentException("Unexpected character: $this")
}
