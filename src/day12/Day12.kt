package day12

import println
import readInput
import kotlin.math.min

fun main() {
    //val problemLine = "###?#?????#????? 6,1,1,1,1"
    //part1(listOf(problemLine))

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day12/Day12_test")
    check(part1(testInput).also { it.println() } == 21)
    check(part2(testInput).also { it.println() } == 525152)

    val input = readInput("day12/Day12")
//    input.forEach { line ->
//        line.println()
//        val part1 = arrangementCount(parseLine(line))
//        val part2 = arrangementCount(unfoldAndParseLine(line))
//        val fifthPower = List(5) { part1 }.reduce { acc, i -> acc * i }
//        println("Part1: $part1, Part2: $part2, FifthPower: $fifthPower")
//    }
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) = input.sumOf { line -> arrangementCount(line) }

fun part2(input: List<String>): Int {
    var done = 0
    return input.sumOf { line ->
        /*
        val lineInfo = parseLine(line)
        val unfoldedLineInfo = LineInfo(
            working = List(5) { lineInfo.working }.flatten(),
            nonWorkingRuns = List(5) { lineInfo.nonWorkingRuns }.flatten()
        )
        */
        val unfoldedLineInfo = unfoldAndParseLine(line)
        arrangementCount(unfoldedLineInfo).also {
            println("Done ${++done}")
        }
    }
}

fun arrangementCount(line: String): Int {
    // line.println()
    val lineInfo = parseLine(line)
    return arrangementCount(lineInfo)
    /*
    val arrangements = nonWorkingRunStarts(lineInfo)
    arrangements.forEach { nonWorkingRunStarts ->
        val states = lineInfo.working.toMutableList()
        nonWorkingRunStarts.forEachIndexed { runIndex, nonWorkingRunStart ->
            (nonWorkingRunStart ..< nonWorkingRunStart + lineInfo.nonWorkingRuns[runIndex])
                .forEach { i -> states[i] = false }
        }
        val statesString = states.joinToString(separator = "") { state ->
            when (state) {
                true -> "."
                false -> "#"
                // Assume all unknowns are working since we've placed all the non-working runs
                else -> "."
            }
        }
        // statesString.println()
    }
    return arrangements.size
    */
}

fun arrangementCount(lineInfo: LineInfo): Int {
    // Recursion base case:
    // Once there are no more broken runs to place,
    // there simply must be no more definitely-broken states.
    if (lineInfo.nonWorkingRuns.isEmpty()) {
        return if (false in lineInfo.working) 0 else 1
    }
    val minRemainderSpace = lineInfo.nonWorkingRuns.drop(1).run { sum() + size } // TODO: or sumOf { it + 1 } ?
    val firstRunPositions = possibleStartPositions(
        // Non-working run length,
        nonWorkingRunLength = lineInfo.nonWorkingRuns.first(),
        // state slice
        working = lineInfo.working,
        // TODO: Nor could it start beyond the first known-working (true/.)
        upperBoundEndPosition = lineInfo.working.lastIndex - minRemainderSpace
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
    val minRemainderSpace = lineInfo.nonWorkingRuns.drop(1).run { sum() + size } // TODO: or sumOf { it + 1 } ?
    val firstRunPositions = possibleStartPositions(
        // Non-working run length,
        nonWorkingRunLength = lineInfo.nonWorkingRuns.first(),
        // state slice
        working = lineInfo.working,
        upperBoundEndPosition = lineInfo.working.lastIndex - minRemainderSpace
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

private fun Boolean?.toChar() = when (this) {
    true -> "."
    false -> "#"
    null -> "?"
}

fun possibleStartPositions(nonWorkingRunLength: Int, working: List<Boolean?>, upperBoundEndPosition: Int): List<Int> {
    // println("Within ${working.joinToString(separator = "") { it.toChar() }}, a run of $nonWorkingRunLength...")
    // Slide across, and it's valid IFF:
    val maxStartPosition = min(
        (upperBoundEndPosition + 1) - nonWorkingRunLength,
        working.indexOfFirst { it == false }.takeIf { it != -1 } ?: Int.MAX_VALUE
    )
    return (0..maxStartPosition).filter { hypotheticalStart ->
        // Is it valid (true/false):

        //  Everything before, if any, is not false
        val isValid = (0 ..< hypotheticalStart).none { working[it] == false } &&
            //  everything within the run is not true
            (hypotheticalStart ..< hypotheticalStart + nonWorkingRunLength).none { working[it] == true } &&
            //  The space after (if it exists), is not false
            (hypotheticalStart + nonWorkingRunLength).let { positionAfter ->
                positionAfter > working.lastIndex || working[positionAfter] != false
            }
        // println("Could start at $hypotheticalStart? $isValid")
        isValid
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

private fun unfoldAndParseLine(line: String) =
    line.split(' ')
        .takeIf { it.size == 2 }!!
        .let { (statesString, runsString) ->
            List(5) { statesString }.joinToString(separator = "?") to
                List(5) { runsString }.joinToString(separator = ",")
        }.let { (statesString, runsString) ->
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
