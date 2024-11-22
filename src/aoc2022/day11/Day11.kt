package aoc2022.day11

import aoc2022.day11.Operand.Constant
import aoc2022.day11.Operand.Old
import aoc2022.day11.Operator.Addition
import aoc2022.day11.Operator.Multiplication
import println
import readInput
import split
import java.util.regex.Pattern

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day11/Day11_test")
    check(part1(testInput).also { it.println() } == 21)
    //check(part2(testInput).also { it.println() } == 8)

    //val input = readInput("aoc2022/day11/Day11")
    //println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val monkeyStrings = input.split { it.isBlank() }
    val monkeys = with(MonkeyParser()) {
        monkeyStrings.map { monkeyStrings -> parseMonkey(monkeyStrings) }
    }
    return input.size
}

fun part2(input: List<String>) = input.size

private class MonkeyParser {
    private val startingItemsParser = startingItemsParser()
    private val operationParser = operationParser()
    private val testParser = testParser()

    fun parseMonkey(monkeyStrings: List<String>) = Monkey(
        startingItems = startingItemsParser.parse(monkeyStrings[1]),
        operation = operationParser.parse(monkeyStrings[2]),
        test = testParser.parse(monkeyStrings.subList(3, 6).joinToString(separator = ""))
    )
}

private data class Monkey(
    val startingItems: StartingItems,
    val operation: Operation,
    val test: Test
)

private data class StartingItems(val itemWorryNumbers: List<Int>)

private data class Operation(val operator: Operator, val operand: Operand) {
    operator fun invoke(worry: Int) = operator(
        worry,
        when (operand) {
            is Constant -> operand.value
            Old -> worry
        }
    )
}

private enum class Operator {
    Addition {
        override fun invoke(first: Int, second: Int) = first + second
    },
    Multiplication {
        override fun invoke(first: Int, second: Int) = first * second
    };

    abstract operator fun invoke(first: Int, second: Int): Int
}

private sealed class Operand {
    data class Constant(val value: Int): Operand()
    data object Old: Operand()
}

private data class Test(val modulus: Int, val trueMonkeyIndex: Int, val falseMonkeyIndex: Int) {
    fun nextMonkeyIndex(worry: Int) = if (worry % modulus == 0) trueMonkeyIndex else falseMonkeyIndex
}

private fun startingItemsParser() =
    RegexParser("""  Starting items: ((?:\d+(?:, )?)+)""") { captures ->
        StartingItems(
            itemWorryNumbers = captures[0]
                .split(", ")
                .map { worryString -> worryString.toInt() }
        )
    }

private fun operationParser() =
    RegexParser("""  Operation: new = old (\+|\*) (?:(\d+)|old)""") { captures ->
        Operation(
            operator = when (captures[0].first()) {
                '+' -> Addition
                '*' -> Multiplication
                else -> throw IllegalArgumentException()
            },
            operand = captures[1]
                .takeIf { it.isNotEmpty() }
                ?.let { constantString -> Constant(constantString.toInt()) }
                ?: Old
        )
    }

// TODO: Parse each line separately instead?
private fun testParser() =
    RegexParser("""  Test: divisible by (\d+)    If true: throw to monkey (\d+)    If false: throw to monkey (\d+)""") { captures ->
        Test(
            modulus = captures[0].toInt(),
            trueMonkeyIndex = captures[1].toInt(),
            falseMonkeyIndex = captures[2].toInt()
        )
    }

private interface Parser<T> {
    fun parse(line: String): T
}

// TODO: Use this generalized version elsewhere?
private class UniversalParser<T>(private vararg val parsers: Parser<T>) : Parser<T> {
    fun List<String>.parseInstructions() = map { line -> parse(line) }
    override fun parse(line: String) = parsers.firstNotNullOf { parser ->
        try {
            parser.parse(line)
        } catch (e: Exception) {
            null
        }
    }
}

// TODO: Use this generalized version elsewhere?
private class RegexParser<T>(
    private val pattern: String,
    private val processCaptures: (List<String>) -> T
) : Parser<T> {
    private val regex by lazy { Regex(pattern) }

    override fun parse(line: String) = processCaptures(
        regex.matchEntire(line)!!.groupValues.drop(1)
    )
}
