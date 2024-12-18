package aoc2022.day10

import Parser
import RegexParser
import UniversalParser
import aoc2022.day10.Instruction.AddInstruction
import aoc2022.day10.Instruction.NoopInstruction
import aoc2022.day10.UniversalInstructionParser.Companion.parseInstructions
import parseLines
import println
import readInput
import kotlin.math.abs

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day10/Day10_test")
    check(part1(testInput).also { it.println() } == 13140)
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("aoc2022/day10/Day10")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer:\n${part2(input)}")
}

private const val INITIAL_REGISTER_VALUE = 1
private const val CRT_WIDTH = 40

fun part1(input: List<String>) = registerValues(input)
    .mapIndexed { cycleIndex, registerValue -> (cycleIndex + 1) * registerValue }
    .filterIndexed { cycleIndex, _ -> cycleIndex.isInterestingCycleIndex }
    .sum() // TODO: Could optimize by using 1 rather than multiplying for uninteresting cycle indexes

fun part2(input: List<String>) = registerValues(input)
    .chunked(CRT_WIDTH)
    .map { lineRegisterValues ->
        String(
            lineRegisterValues.mapIndexed { pixelX, spriteX ->
                if (abs(pixelX - spriteX) <= 1) '#' else ' '
            }.toCharArray()
        )
    }.joinToString(separator = "\n")

private fun registerValues(input: List<String>) = input
    .parseInstructions()
    .asSequence()
    .flatMap { instruction -> instruction.deltas() }
    .scan(INITIAL_REGISTER_VALUE) { registerValue, delta -> registerValue + delta }

val Int.isInterestingCycleIndex get() = (this - 19) % 40 == 0

private fun Instruction.deltas() = when (this) {
    is AddInstruction -> listOf(0, value)
    NoopInstruction -> listOf(0)
}

private sealed class Instruction {
    data class AddInstruction(val value: Int) : Instruction()
    data object NoopInstruction : Instruction()
}

private class UniversalInstructionParser :
    Parser<Instruction> by UniversalParser(addParser(), noopParser()) {
    companion object {
        fun List<String>.parseInstructions() =
            UniversalInstructionParser().parseLines(this)
    }
}

private fun addParser() =
    RegexParser("""addx (-?\d+)""") { captures ->
        AddInstruction(value = captures[0].toInt())
    }

private fun noopParser() =
    RegexParser("""noop""") { NoopInstruction }
