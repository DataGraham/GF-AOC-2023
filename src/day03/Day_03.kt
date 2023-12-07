package day03

import readInput
import toIntRange

fun main() {

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day03/Day03_test")
    check(part1(testInput) == 4361)
    //check(part2(testInput) == 1)

    val input = readInput("day03/Day03")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    return input.foldIndexed(0) { lineIndex, sum, _ ->
        sum + partNumbersSum(lineIndex = lineIndex, lines = input)
    }.also { println("Calculated $it") }
}

private fun partNumbersSum(lineIndex: Int, lines: List<String>) =
    captureIntegersRegex
        .findAll(lines[lineIndex])
        .filter { it.isPartNumber(lineIndex = lineIndex, lines = lines) }
        .sumOf { it.value.toInt() }

val captureIntegersRegex = Regex("([0-9]+)")

private fun MatchResult.isPartNumber(lineIndex: Int, lines: List<String>) =
    LineRange(lineIndex = lineIndex, range = range)
        .adjacentRanges(lineLength = lines[lineIndex].length, numLines = lines.size)
        .any { symbolRegex in lines[it.lineIndex].substring(it.range) }

private fun LineRange.adjacentRanges(lineLength: Int, numLines: Int): List<LineRange> {
    val horizontalRange = (range.first - 1).coerceAtLeast(0)..
        (range.last + 1).coerceAtMost(lineLength - 1)
    val lineRangeAbove =
        if (lineIndex > 0) LineRange(lineIndex = lineIndex - 1, range = horizontalRange)
        else null
    val lineRangeBefore =
        if (range.first > 0) LineRange(lineIndex = lineIndex, range = (range.first - 1).toIntRange())
        else null
    val lineRangeAfter =
        if (range.last < lineLength - 1) LineRange(lineIndex = lineIndex, range = (range.last + 1).toIntRange())
        else null
    val lineRangeBelow =
        if (lineIndex < numLines - 1) LineRange(lineIndex = lineIndex + 1, range = horizontalRange)
        else null
    return listOfNotNull(lineRangeAbove, lineRangeBefore, lineRangeAfter, lineRangeBelow)
}

data class LineRange(val lineIndex: Int, val range: IntRange)

val symbolRegex = Regex("[^0-9.]")

fun part2(input: List<String>): Int {
    return input.size
}
