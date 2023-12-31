package day13

import readInput
import split

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day13/Day13_test")
    check(part1(testInput) == 405)
    //check(part2(testInput) == 1)

    val input = readInput("day13/Day13")
    println("Part 1 Answer: ${part1(input)}")
    // println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val patterns = input.split { it.isBlank() }
    return patterns.sumOf { pattern ->
        (pattern.rowsAboveHorizontalMirror() ?: 0) * 100 +
            (pattern.transposed().rowsAboveHorizontalMirror() ?: 0)
    }
}

private fun List<String>.rowsAboveHorizontalMirror(): Int? {
    return indices.drop(1).firstOrNull { rowsBefore ->
        (rowsBefore..lastIndex).all { lowerLineIndex ->
            val matchingLineIndex = rowsBefore - 1 - (lowerLineIndex - rowsBefore)
            matchingLineIndex < 0 || this[lowerLineIndex] == this[matchingLineIndex]
        }
    }
}

private fun List<String>.transposed() =
    first().indices.map { col ->
        map { line -> line[col] }.joinToString(separator = "")
    }

fun part2(input: List<String>): Int {
    return input.size
}
