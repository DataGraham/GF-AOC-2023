package day12

import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day12/Day12_test")
    check(part1(testInput).also { it.println() } == 21)
    // check(part2(testInput) == 1)

    val input = readInput("day12/Day12")
    println("Part 1 Answer: ${part1(input)}")
    // println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) = input.sumOf { line -> arrangementCount(line) }

fun part2(input: List<String>): Int {
    return input.size
}

fun arrangementCount(line: String): Int {
    line.println()
    val lineInfo = parseLine(line)
    val arrangements = nonWorkingRunStarts(lineInfo)
    arrangements.forEach { nonWorkingRunStarts ->
        val states = lineInfo.working.toMutableList()
        nonWorkingRunStarts.forEachIndexed { runIndex, nonWorkingRunStart ->
            (nonWorkingRunStart ..< nonWorkingRunStart + lineInfo.nonWorkingRuns[runIndex])
                .forEach { i -> states[i] = false }
        }
        // TODO: 8405 is too many, so I must be generating some invalid arrangements
        // TODO: Run a validity post-check (non-working run lengths are correct, and all non-? match)
        //  (and print them out in red?)
        // TODO: Well, here is one that generates an invalid arrangement:
        /*
        ###?#?????#????? 6,1,1,1,1
        ######.#..#.#.#.
        ######.#..#.#..#
        ######.#..#..#.#
        ######..#.#.#.#.
        ######..#.#.#..#
        ######..#.#..#.#
        ######...###.#.#
        */
        val statesString = states.joinToString(separator = "") { state ->
            when (state) {
                true -> "."
                false -> "#"
                // Assume all unknowns are working since we've placed all the non-working runs
                else -> "."
            }
        }
        statesString.println()
    }
    return arrangements.size
}

fun nonWorkingRunStarts(lineInfo: LineInfo): List<List<Int>> {
    // Recursion base case:
    // Once there are no more broken runs to place,
    // there simply must be no more definitely-broken states.
    if (lineInfo.nonWorkingRuns.isEmpty()) {
        return if (false in lineInfo.working)
            emptyList() // there are no arrangements that work
        else
            listOf(emptyList()) // there is exactly one arrangement with no non-working runs
    }
    val firstRunPositions = possibleStartPositions(
        // Non-working run length,
        nonWorkingRunLength = lineInfo.nonWorkingRuns.first(),
        // state slice
        working = lineInfo.working.dropLast(
            lineInfo.nonWorkingRuns.drop(1).run { sum() + size }
        )
    )
    return firstRunPositions.flatMap { firstRunPosition ->
        val remainderOffset = firstRunPosition + lineInfo.nonWorkingRuns.first() + 1
        nonWorkingRunStarts(
            LineInfo(
                working = lineInfo.working.drop(remainderOffset),
                nonWorkingRuns = lineInfo.nonWorkingRuns.drop(1)
            )
        ).map { remainingRunStarts ->
            listOf(firstRunPosition) + remainingRunStarts.map { it + remainderOffset }
        }
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
