package aoc2024.day07

import RegexParser
import parseLines
import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day07/Day07_test")
    check(part1(testInput).also { it.println() } == 3749L)
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("aoc2024/day07/Day07")
    println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) =
    calibrationEquationParser.parseLines(input)
        .filter { equation -> equation.couleBeTrue }
        .sumOf { equation -> equation.result }

private val CalibrationEquation.couleBeTrue
    get() = operatorSets(length = inputs.size - 1)
        .any { operatorSet -> operatorSet.applyToInputs(inputs) == result }

fun part2(input: List<String>): Int {
    return input.size
}

data class CalibrationEquation(
    val result: Long,
    val inputs: List<Long>
)

val calibrationEquationParser by lazy {
    RegexParser("""(\d+):(( \d+)+)""") { captures ->
        val (resultString, inputsString) = captures
        CalibrationEquation(
            result = resultString.toLong(),
            inputs = inputsString
                .trim()
                .split(" ")
                .map { it.toLong() }
        )
    }
}

enum class Operator(private val calculate: (Long, Long) -> Long) {
    Addition({ a, b -> a + b }),
    Multiplication({ a, b -> a * b });

    operator fun invoke(a: Long, b: Long): Long = calculate(a, b)
}

/** TODO: Or recursively? */
fun operatorSets(length: Int) = generateSequence(
    List(size = length) { Operator.entries.first() }
) { previous ->
    try {
        val indexToIncrement = previous.indexOfFirst { it != Operator.entries.last() }
        val resetPrefix = List(size = indexToIncrement) { Operator.entries.first() }
        val incrementedOperator = listOf(Operator.entries[previous[indexToIncrement].ordinal + 1])
        val untouchedRemainder = previous.subList(fromIndex = indexToIncrement + 1, toIndex = previous.size)
        resetPrefix + incrementedOperator + untouchedRemainder
    } catch (e: Exception) {
        null
    }
}

fun List<Operator>.applyToInputs(inputs: List<Long>) =
    zip(inputs.drop(1))
        .fold(inputs.first()) { acc, (operator, input) ->
            operator(acc, input)
        }
