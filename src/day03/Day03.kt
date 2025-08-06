package day03

import readInput
import toIntRange

fun main() {

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day03/Day03_test")
    check(aoc2024.day03.part1(testInput) == 4361)
    //check(aoc2022.day07.part2(testInput) == 1)

    val input = readInput("day03/Day03")
    println("Part 1 Answer: ${aoc2024.day03.part1(input)}")
    println("Part 2 Answer: ${aoc2024.day03.part2(input)}")
}

fun part1(input: List<String>): Int {
    return input.foldIndexed(0) { lineIndex, sum, _ ->
        sum + partNumbersSum(lineIndex = lineIndex, lines = input)
    }.also { println("Calculated $it") }
}

fun part2(input: List<String>): Int {
    val numbersGrid = List(input.size) { MutableList<Int?>(input[it].length) { null } }
    input.forEachIndexed { lineIndex, line ->
        captureIntegersRegex
            .findAll(line)
            .forEach { numberMatch ->
                val intHere = numberMatch.value.toInt()
                numberMatch.range.forEach {
                    numbersGrid[lineIndex][it] = intHere
                }
            }
    }

    return input.foldIndexed(0) { lineIndex, sum, line ->
        val starIndices = line.indices.filter { line[it] == '*' }
        sum + starIndices.sumOf { starIndex ->
            val adjacentNumbers =
                LineRange(lineIndex = lineIndex, range = starIndex.toIntRange())
                    .adjacentRanges(lineLength = input[lineIndex].length, numLines = input.size)
                    .flatMap { adjacentRange ->
                        adjacentRange.range.mapNotNull { adjacentLocation ->
                            numbersGrid[adjacentRange.lineIndex][adjacentLocation]
                        }
                    }
            // TODO: correctly handle duplicate part numbers by checking for 2 numbers by referential equality (===)?!
            val gearRatio = adjacentNumbers.toSet().run {
                if (size == 2) reduce { acc, i -> acc * i } else null
            }
            gearRatio ?: 0
        }
    }
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
