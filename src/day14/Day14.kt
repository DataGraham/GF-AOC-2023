package day14

import printLines
import println
import readInput
import transposed

fun main() {
    check(
        listOf("abc", "def") == listOf("abc", "def")
    )
    
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day14/Day14_test")
    check(part1(testInput).also { it.println() } == 136)
    check(part2(testInput).also { it.println() } == 64)

    val input = readInput("day14/Day14")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    // Make a string for each column
    val columns = input.transposed()
    val tiltedColumns = columns.map { column -> column.tilted(towardsEnd = false) }
    // For each string, sum for each 'O' its index from the end + 1
    return tiltedColumns.sumOf(String::columnLoad)
}

fun part2(input: List<String>): Int {
    val stableState =
        generateSequence(input to input.cycled()) { (_, current) ->
            current.printLines()
            println()
            current to current.cycled()
        }.first { (previous, current) -> previous == current }.first
    return stableState.transposed().sumOf { column -> column.columnLoad() }
}

private fun List<String>.cycled(): List<String> {
    val tiltedNorth = transposed().map { column -> column.tilted(towardsEnd = false) }
    val tiltedWest = tiltedNorth.transposed().map { row -> row.tilted(towardsEnd = false) }
    val tiltedSouth = tiltedWest.transposed().map { column -> column.tilted(towardsEnd = true) }
    val tiltedEast = tiltedSouth.transposed().map { row -> row.tilted(towardsEnd = true) }
    return tiltedEast
}

private const val ROUND_ROCK = 'O'
private const val CUBE_ROCK = '#'
private const val EMPTY_SPACE = '.'
private val areasRegex = Regex("[$ROUND_ROCK$EMPTY_SPACE]+|$CUBE_ROCK+")

private fun String.tilted(towardsEnd: Boolean): String {
    // Split by '#' into substrings of:
    //  only 'O' round rocks and '.' empty spaces
    //  consecutive sequences of '#' (cube rocks)
    val areas = areasRegex.findAll(this).map { it.value }

    // Map each 'O/.' substring to a string with all 'O' first, followed by all '.'
    // i.e. just count occurrences of each character and put all 'O' first (or last if towardsEnd)
    val tiltedAreas = areas.map { area ->
        if (area.first() == CUBE_ROCK) area
        else (area.filter { it == ROUND_ROCK } + area.filter { it == EMPTY_SPACE })
            .let { areaTiltedToStart ->
                if (towardsEnd) areaTiltedToStart.reversed()
                else areaTiltedToStart
            }
    }

    // Now put the substrings back together
    return tiltedAreas.joinToString(separator = "")
}

private fun String.columnLoad() =
    withIndex().sumOf { (index, char) ->
        if (char == ROUND_ROCK) length - index
        else 0
    }