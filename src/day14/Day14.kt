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
    val tiltedColumns = columns.map(String::tiltedNorth)

    // For each string, sum for each 'O' its index from the end + 1

    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}

private val openAndBlockedAreas = Regex("""[O.]+|#+""")

private fun String.tiltedNorth() {
    // Split by '#' into substrings of:
    //  only 'O' round rocks and '.' empty spaces
    //  consecutive sequences of '#' (cube rocks)
    println()
    openAndBlockedAreas.findAll(this).forEach { it.value.println() }

    // Map each 'O/.' substring to a string with all 'O' first, followed by all '.'
    // i.e. just count occurrences of each character and put all 'O' first.

    // Now put the substrings back together
}
