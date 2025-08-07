package aoc2024.day03

import Parser
import RegexParser
import UniversalParser
import aoc2024.day03.Instruction.*
import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day03/Day03_test")
    check(part1(testInput).also { it.println() } == 161)
    val testInput2 = readInput("aoc2024/day03/Day03_test2")
    check(part2(testInput2).also { it.println() } == 48)

    val input = readInput("aoc2024/day03/Day03")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) = input
    .parseMultiplicationInstructions()
    .sumOf { multiplicationInstruction -> multiplicationInstruction() }

fun part2(input: List<String>): Int {
    val instructions = input.parseInstructions()
    return input.size
}

private fun List<String>.parseMultiplicationInstructions() =
    findAll(multiplicationInstructionRegex)
        .map(multiplicationInstructionParser::parse)

private fun List<String>.parseInstructions() =
    findAll(instructionRegex)
        .map(instructionParser::parse)

private fun List<String>.findAll(regex: Regex) = flatMap { line ->
    regex.findAll(line)
        .map { matchResult -> matchResult.groupValues.first() }
        .toList()
}

private val multiplicationInstructionRegex = Regex("""mul\(\d+,\d+\)""")

private val instructionRegex = Regex("""mul\(\d+,\d+\)|do\(\)|don't\(\)""")

private val multiplicationInstructionParser =
    RegexParser("""mul\((\d+),(\d+)\)""") { captures ->
        MultiplicationInstruction(captures[0].toInt(), captures[1].toInt())
    }

private val doInstructionParser = object : Parser<DoInstruction> {
    override fun parse(line: String) = DoInstruction.takeIf { line == "do()" }!!
}

private val doNotInstructionParser = object : Parser<DoNotInstruction> {
    override fun parse(line: String) = DoNotInstruction.takeIf { line == "don't()" }!!
}

private val instructionParser = UniversalParser(
    multiplicationInstructionParser,
    doInstructionParser,
    doNotInstructionParser
)

private sealed class Instruction {

    data class MultiplicationInstruction(val x: Int, val y: Int) : Instruction() {
        operator fun invoke() = x * y
    }

    data object DoInstruction : Instruction()

    data object DoNotInstruction : Instruction()
}
