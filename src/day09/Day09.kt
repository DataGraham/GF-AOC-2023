package day09

import readInput

fun main() {

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day09/Day09_test")
    check(part1(testInput) == 114)
    //check(part2(testInput) == 1)

    val input = readInput("day09/Day09")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    return input.sumOf { line -> predictNextValue(line) }
}

fun part2(input: List<String>): Int {
    return input.size
}

private fun predictNextValue(line: String): Int {
    val history = parseHistory(line)
    val derivatives = history.getDerivatives()
    return derivatives.sumOf { it.last() }
}

private fun List<Int>.getDerivatives() = generateSequence(this) { it.differences() }
    .takeWhile { derivative -> !derivative.all { it == 0 } }

private fun parseHistory(line: String) = line.split(' ').map(String::toInt)

private fun List<Int>.differences() = zip(drop(1)).map { (first, second) -> second - first }
