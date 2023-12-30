package day12

import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day12/Day12_test")
    check(part1(testInput).also { it.println() } == 21)
    //check(part2(testInput) == 1)

    val input = readInput("day12/Day12")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) = input.sumOf { line -> arrangementCount(line) }

fun part2(input: List<String>): Int {
    return input.size
}

fun arrangementCount(line: String) = arrangementCount(parseLine(line))

fun arrangementCount(lineInfo: LineInfo): Int {
    // Recursion base case:
    // Once there are no more broken runs to place,
    // there simply must be no more definitely-broken states.
    if (lineInfo.nonWorkingRuns.isEmpty()) {
        return if (false in lineInfo.working) 0 else 1
    }
    val firstRunPositions = possibleStartPositions(
        // Non-working run length,
        nonWorkingRunLength = lineInfo.nonWorkingRuns.first(),
        // state slice
        working = lineInfo.working.dropLast(
            lineInfo.nonWorkingRuns.drop(1).run { sum() + size }
        )
    )
    return firstRunPositions.sumOf { firstRunPosition ->
        arrangementCount(
            LineInfo(
                working = lineInfo.working.drop(firstRunPosition + lineInfo.nonWorkingRuns.first() + 1),
                nonWorkingRuns = lineInfo.nonWorkingRuns.drop(1)
            )
        )
    }
}

fun possibleStartPositions(nonWorkingRunLength: Int, working: List<Boolean?>): List<Int> {
    // Slide across, and it's valid IFF:
    return (0..working.size - nonWorkingRunLength).filter { hypotheticalStart ->
        // Is it valid (true/false):

        //  Everything before, if any, is not false
        (0 ..< hypotheticalStart).none { working[it] == false } &&
            //  everything within the run is not true
            (hypotheticalStart ..< hypotheticalStart + nonWorkingRunLength).none { working[it] == true } &&
            //  The space after (if it exists), is not false
            (hypotheticalStart + nonWorkingRunLength).let { positionAfter ->
                positionAfter > working.lastIndex || working[positionAfter] != false
            }
    }
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

private fun Char.toWorkingState() = when (this) {
    '.' -> true
    '#' -> false
    '?' -> null
    else -> throw IllegalArgumentException("Unexpected character: $this")
}
