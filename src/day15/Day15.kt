package day15

import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    check(part1("HASH").also { it.println() } == 52)
    val testInput = readInput("day15/Day15_test").first()
    check(part1(testInput).also { it.println() } == 1320)
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("day15/Day15").first()
    println("Part 1 Answer: ${part1(input)}")
    // println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: String): Int {
    return input.split(',').sumOf { it.holidayAsciiStringHelper() }
}

fun part2(input: List<String>): Int {
    return input.size
}

private fun String.holidayAsciiStringHelper() = fold(0) { hash, c -> ((hash + c.code) * 17) % 256 }
