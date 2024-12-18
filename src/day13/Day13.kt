package day13

import printLines
import println
import readInput
import split
import transposedStrings

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day13/Day13_test")
    check(part1(testInput).also { it.println() } == 405)
    check(part2(testInput).also { it.println() } == 400)

    val input = readInput("day13/Day13")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val patterns = input.split { it.isBlank() }
    return patterns.sumOf { pattern ->
        (pattern.horizontalMirrorOffsets().firstOrNull() ?: 0) * 100 +
            (pattern.transposedStrings().horizontalMirrorOffsets().firstOrNull() ?: 0)
    }
}

fun part2(input: List<String>): Int {
    val patterns = input.split { it.isBlank() }
    return patterns.sumOf { pattern ->
        println("Pattern:")
        pattern.printLines()
        val originalMirror =
            pattern.horizontalMirrorOffsets().firstOrNull()?.let { Mirror(it, false) }
                ?: pattern.transposedStrings().horizontalMirrorOffsets().firstOrNull()?.let { Mirror(it, true) }
                ?: throw IllegalArgumentException("No mirror found")
        println("...has mirror $originalMirror")
        pattern.withIndex().firstNotNullOf { (rowIndex, row) ->
            // Produce pattern with flipped state at each row
            // Find the first of them that has a DIFFERENT mirror position
            row.indices.firstNotNullOfOrNull { colIndex ->
                val toggledPattern = pattern.toggledAt(targetRow = rowIndex, targetCol = colIndex)
                val horizontalSummary = toggledPattern.horizontalMirrorOffsets()
                    .map { Mirror(it, false) }
                    .firstOrNull { it != originalMirror }
                    ?.let { it.offset * 100 }
                val verticalSummary = toggledPattern.transposedStrings().horizontalMirrorOffsets()
                    .map { Mirror(it, true) }
                    .firstOrNull { it != originalMirror }
                    ?.offset
                (horizontalSummary ?: verticalSummary)?.also { newMirrorSummary ->
                    println("With fixed smudge at ($rowIndex, $colIndex), this:")
                    toggledPattern.printLines()
                    println("...has a new mirror summary $newMirrorSummary")
                }
            }
        }
    }
}

private fun List<String>.toggledAt(targetRow: Int, targetCol: Int) =
    mapIndexed { index, line ->
        if (index == targetRow) line.toggledAt(targetCol)
        else line
    }

private fun String.toggledAt(index: Int) =
    substring(0 ..< index) +
        this[index].toggled() +
        substring(startIndex = index + 1)

private fun Char.toggled() = when (this) {
    '.' -> '#'
    else -> '.'
}

data class Mirror(val offset: Int, val isVertical: Boolean)

private fun List<String>.horizontalMirrorOffsets(): List<Int> {
    return indices.drop(1).filter { rowsBefore ->
        (rowsBefore..lastIndex).all { lowerLineIndex ->
            val matchingLineIndex = rowsBefore - 1 - (lowerLineIndex - rowsBefore)
            matchingLineIndex < 0 || this[lowerLineIndex] == this[matchingLineIndex]
        }
    }
}
