package day03

import readInput

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

private fun MatchResult.isPartNumber(lineIndex: Int, lines: List<String>): Boolean {
    // Check above
    if (lineIndex > 0)
        lines[lineIndex - 1].let { previousLine ->
            if (symbolRegex in previousLine.substring(
                    (range.first - 1).coerceAtLeast(0)..
                        (range.last + 1).coerceAtMost(previousLine.lastIndex)
                )
            ) return true
        }

    // Check before
    if (range.first > 0 && symbolRegex.matches(lines[lineIndex][range.first - 1].toString())) return true

    // Check after
    if (range.last < lines[lineIndex].lastIndex && symbolRegex.matches(lines[lineIndex][range.last + 1].toString())) return true

    // Check below
    if (lineIndex < lines.lastIndex)
        lines[lineIndex + 1].let { nextLine ->
            if (symbolRegex in nextLine.substring(
                    (range.first - 1).coerceAtLeast(0)..
                        (range.last + 1).coerceAtMost(nextLine.lastIndex)
                )
            ) return true
        }

    return false
}

val symbolRegex = Regex("[^0-9.]")

fun part2(input: List<String>): Int {
    return input.size
}
