package aoc2024.day07

import RegexParser
import parseLines
import println
import readInput
import kotlin.enums.enumEntries

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day07/Day07_test")
    check(part1(testInput).also { it.println() } == 3749L)
    check(part2(testInput).also { it.println() } == 11387L)

    val input = readInput("aoc2024/day07/Day07")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) = sumOfPossiblyTrueResults<Operator>(input)

fun part2(input: List<String>) = sumOfPossiblyTrueResults<ExtendedOperator>(input)

private inline fun <reified E> sumOfPossiblyTrueResults(input: List<String>) where E : Enum<E>, E : Calculable =
    calibrationEquationParser
        .parseLines(input)
        .filter { equation -> equation.getCouleBeTrue<E>() }
        .sumOf { equation -> equation.result }

private inline fun <reified E> CalibrationEquation.getCouleBeTrue() where E : Enum<E>, E : Calculable =
    enumSets<E>(length = inputs.size - 1)
        .any { operatorSet -> operatorSet.applyToInputs(inputs) == result }

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

interface Calculable {
    operator fun invoke(a: Long, b: Long): Long
}

enum class Operator(private val calculate: (Long, Long) -> Long) : Calculable {
    Addition({ a, b -> a + b }),
    Multiplication({ a, b -> a * b });

    override operator fun invoke(a: Long, b: Long): Long = calculate(a, b)
}

enum class ExtendedOperator(private val calculate: (Long, Long) -> Long) : Calculable {
    Addition({ a, b -> Operator.Addition(a, b) }),
    Multiplication({ a, b -> Operator.Multiplication(a, b) }),
    Concatenation({ a, b -> ("$a$b").toLong() });

    override operator fun invoke(a: Long, b: Long): Long = calculate(a, b)
}

inline fun <reified E : Enum<E>> enumSets(length: Int) = generateSequence(
    List(size = length) { enumEntries<E>().first() }
) { previous ->
    try {
        val indexToIncrement = previous.indexOfFirst { it != enumEntries<E>().last() }
        val resetPrefix = List(size = indexToIncrement) { enumEntries<E>().first() }
        val incrementedOperator = listOf(enumEntries<E>()[previous[indexToIncrement].ordinal + 1])
        val untouchedRemainder = previous.subList(fromIndex = indexToIncrement + 1, toIndex = previous.size)
        resetPrefix + incrementedOperator + untouchedRemainder
    } catch (e: Exception) {
        null
    }
}

/** Considerable slower than iterative */
fun operatorSetsRecursive(length: Int): Sequence<List<Operator>> {
    val sequenceOfSingles = Operator.entries
        .map { operator -> listOf(operator) }
        .asSequence()
    return if (length == 1)
        sequenceOfSingles
    else sequenceOfSingles.flatMap { singleEntry ->
        operatorSetsRecursive(length - 1)
            .map { resetOfSequence -> singleEntry + resetOfSequence }
    }
}

fun List<Calculable>.applyToInputs(inputs: List<Long>) =
    zip(inputs.drop(1))
        .fold(inputs.first()) { acc, (operator, input) ->
            operator(acc, input)
        }
