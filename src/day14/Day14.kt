package day14

import println
import readInput
import transposed

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day14/Day14_test")
    check(part1(testInput).also { it.println() } == 1)
    //check(part2(testInput).also { it.println() } == 1)

//    val input = readInput("day14/Day14")
//    println("Part 1 Answer: ${part1(input)}")
//    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    // Make a string for each column
    val columns = input.transposed()
    val tiltedColumns = columns.map(String::tiltedNorth).also { it.forEach { it.println() } }

    // For each string, sum for each 'O' its index from the end + 1

    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}

private const val ROUND_ROCK = 'O'
private const val CUBE_ROCK = '#'
private const val EMPTY_SPACE = '.'
private val areasRegex = Regex("[$ROUND_ROCK$EMPTY_SPACE]+|$CUBE_ROCK+")

private fun String.tiltedNorth(): String {
    // Split by '#' into substrings of:
    //  only 'O' round rocks and '.' empty spaces
    //  consecutive sequences of '#' (cube rocks)
    val areas = areasRegex.findAll(this).map { it.value }

    // Map each 'O/.' substring to a string with all 'O' first, followed by all '.'
    // i.e. just count occurrences of each character and put all 'O' first.
    val tiltedAreas = areas.map { area ->
        if (area.first() == CUBE_ROCK) area
        else area.filter { it == ROUND_ROCK } + area.filter { it == EMPTY_SPACE }
    }

    // Now put the substrings back together
    return tiltedAreas.joinToString(separator = "")
}
