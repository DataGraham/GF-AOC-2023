package aoc2022.day10

import aoc2022.day10.Instruction.AddInstruction
import aoc2022.day10.Instruction.NoopInstruction
import aoc2022.day10.UniversalInstructionParser.Companion.parseInstructions
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

private interface InstructionParser {
    fun parse(line: String): Instruction?
}

// TODO: Generalize?
private class UniversalInstructionParser : InstructionParser {
    companion object {
        fun List<String>.parseInstructions() =
            with(UniversalInstructionParser()) {
                map { line -> parse(line) }
            }
    }

    private val parsers by lazy { listOf(addParser(), noopParser()) }

    override fun parse(line: String) = parsers.firstNotNullOf { parser -> parser.parse(line) }
}

private fun addParser() = RegexInstructionParser("""addx (-?\d+)""") { captures ->
    AddInstruction(value = captures[0].toInt())
}

private fun noopParser() = RegexInstructionParser("""noop""") { NoopInstruction }

// TODO: Generalize?
private class RegexInstructionParser(
    private val pattern: String,
    private val processCaptures: (List<String>) -> Instruction
) : InstructionParser {
    private val regex by lazy { Regex(pattern) }

    override fun parse(line: String) = regex.matchEntire(line)?.let { match ->
        processCaptures(match.groupValues.drop(1))
    }
}
