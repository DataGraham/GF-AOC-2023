package aoc2024.day03

import RegexParser
import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day03/Day03_test")
    check(part1(testInput).also { it.println() } == 161)
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("aoc2024/day03/Day03")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) = input
    .parseInstructions()
    .sumOf { instruction -> instruction() }

fun part2(input: List<String>): Int {
    return input.size
}

private fun List<String>.parseInstructions() =
    instructionStrings.map { instructionString -> instructionParser.parse(instructionString) }

private val List<String>.instructionStrings
    get() = flatMap { line ->
        instructionRegex.findAll(line)
            .map { matchResult -> matchResult.groupValues.first() }
            .toList()
    }

private val instructionRegex = Regex("""mul\(\d+,\d+\)""")

private val instructionParser =
    RegexParser("""mul\((\d+),(\d+)\)""") { captures ->
        MultiplicationInstruction(captures[0].toInt(), captures[1].toInt())
    }

private data class MultiplicationInstruction(val x: Int, val y: Int) {
    operator fun invoke() = x * y
}
