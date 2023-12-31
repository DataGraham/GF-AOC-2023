package day13

import println
import readInput
import split

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
        (pattern.rowsAboveHorizontalMirror() ?: 0) * 100 +
            (pattern.transposed().rowsAboveHorizontalMirror() ?: 0)
    }
}

fun part2(input: List<String>): Int {
    val patterns = input.split { it.isBlank() }
    return patterns.sumOf { pattern ->
        val originalMirror =
            pattern.rowsAboveHorizontalMirror()?.let { Mirror(it, false) }
                ?: pattern.transposed().rowsAboveHorizontalMirror()?.let { Mirror(it, true) }
                ?: throw IllegalArgumentException("No mirror found")
        pattern.withIndex().firstNotNullOf { (rowIndex, row) ->
            // Produce pattern with flipped state at each row
            // Find the first of them that has a DIFFERENT mirror position
            row.indices.firstNotNullOfOrNull { colIndex ->
                val toggledPattern = pattern.toggledAt(targetRow = rowIndex, targetCol = colIndex)
                val horizontalSummary = toggledPattern.rowsAboveHorizontalMirror()
                    ?.let { Mirror(it, false) }
                    ?.takeIf { it != originalMirror }
                    ?.let { it.offset * 100 }
                val verticalSummary = toggledPattern.transposed().rowsAboveHorizontalMirror()
                    ?.let { Mirror(it, true) }
                    ?.takeIf { it != originalMirror }
                    ?.offset
                horizontalSummary ?: verticalSummary
            }
        }
    }
}

private fun List<String>.toggledAt(targetRow: Int, targetCol: Int) =
    mapIndexed { index, line ->
        if (index == targetRow)
            line.substring(0 ..< targetCol) +
                line[targetCol].toggled() +
                line.substring(startIndex = targetCol + 1)
        else
            line
    }

private fun Char.toggled() = when (this) {
    '.' -> '#'
    else -> '.'
}

data class Mirror(val offset: Int, val isVertical: Boolean)

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
