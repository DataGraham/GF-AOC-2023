package aoc2024.day07

import RegexParser
import parseLines
import printLines
import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day07/Day07_test")
    check(part1(testInput).also { it.println() } == 161)
    //check(part2(testInput).also { it.println() } == 1)

    //val input = readInput("aoc2024/day07/Day07")
    //println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val calibrationEquations = calibrationEquationParser
        .parseLines(input)
        .printLines()
    operatorSets(length = 3).toList().printLines()
    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}

data class CalibrationEquation(
    val result: Int,
    val inputs: List<Int>
)

enum class Operator(private val calculate: (Int, Int) -> Int) {
    Addition({ a, b -> a + b }),
    Multiplication({ a, b -> a * b });

    operator fun invoke(a: Int, b: Int): Int = calculate(a, b)
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

val calibrationEquationParser by lazy {
    RegexParser("""(\d+):(( \d+)+)""") { captures ->
        val (resultString, inputsString) = captures
        CalibrationEquation(
            result = resultString.toInt(),
            inputs = inputsString
                .trim()
                .split(" ")
                .map { it.toInt() }
        )
    }
}