package aoc2022.day11

import RegexParser
import aoc2022.day11.MonkeyParser.Companion.parseMonkeys
import aoc2022.day11.Operand.Constant
import aoc2022.day11.Operand.Old
import aoc2022.day11.Operator.Addition
import aoc2022.day11.Operator.Multiplication
import intParser
import lcm
import println
import readInput
import split

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day11/Day11_test")
    check(part1(testInput).also { it.println() } == 10605L)
    check(part2(testInput).also { it.println() } == 2713310158)

    val input = readInput("aoc2022/day11/Day11")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) = productOf2MaxMonkeyInspections(
    input = input,
    roundCount = 20,
    getWorryReducer = {
        { worry -> worry / 3 }
    }
)

fun part2(input: List<String>) = productOf2MaxMonkeyInspections(
    input = input,
    roundCount = 10000,
    getWorryReducer = { modulo(testModulusLcm) }
)

private fun modulo(modulus: Long) = { input: Long -> (input % modulus) }

private val List<Monkey>.testModulusLcm
    get() = lcm(map { monkey -> monkey.testModulus })

private fun productOf2MaxMonkeyInspections(
    input: List<String>,
    roundCount: Int,
    getWorryReducer: List<Monkey>.() -> worryReducer
): Long {
    val monkeys = input.parseMonkeys()
    val reduceWorry = monkeys.getWorryReducer()
    repeat(roundCount) {
        monkeys.performRound(reduceWorry = reduceWorry)
    }
    return monkeys
        .map { monkey -> monkey.inspectionCount }
        .sortedDescending()
        .let { inspectionCountsDescending ->
            inspectionCountsDescending[0].toLong() * inspectionCountsDescending[1].toLong()
        }
}

typealias worryReducer = (Long) -> Long

private fun List<Monkey>.performRound(reduceWorry: worryReducer) =
    forEach { monkey ->
        monkey.inspectItems(reduceWorry = reduceWorry)
            .forEach { itemThrow -> catchItem(itemThrow) }
    }

private fun List<Monkey>.catchItem(itemThrow: ItemThrow) {
    this[itemThrow.monkeyIndex].catchItem(itemThrow.itemWorry)
}

private class Monkey(
    startingItems: StartingItems,
    private val operation: Operation,
    private val test: Test
) {
    private val itemWorryLevels = startingItems.itemWorryNumbers.toMutableList()

    val testModulus get() = test.modulus

    fun inspectItems(reduceWorry: worryReducer): List<ItemThrow> {
        val itemThrows = itemWorryLevels.map { originalWorryLevel ->
            // TODO: Use a value class for a WorryLevel?
            itemThrow(newWorryLevel = reduceWorry(operation(originalWorryLevel)))
        }
        inspectionCount += itemThrows.size
        itemWorryLevels.clear()
        return itemThrows
    }

    private fun itemThrow(newWorryLevel: Long) = ItemThrow(
        itemWorry = newWorryLevel,
        monkeyIndex = test.nextMonkeyIndex(newWorryLevel)
    )

    fun catchItem(itemWorryLevel: Long) {
        itemWorryLevels += itemWorryLevel
    }

    var inspectionCount: Int = 0
        private set
}

private data class ItemThrow(val itemWorry: Long, val monkeyIndex: Int)

private data class StartingItems(val itemWorryNumbers: List<Long>)

private data class Operation(private val operator: Operator, private val operand: Operand) {
    operator fun invoke(worry: Long) = operator(
        worry,
        when (operand) {
            is Constant -> operand.value
            Old -> worry
        }
    )
}

private enum class Operator {
    Addition {
        override fun invoke(first: Long, second: Long) = first + second
    },
    Multiplication {
        override fun invoke(first: Long, second: Long) = first * second
    };

    abstract operator fun invoke(first: Long, second: Long): Long
}

private sealed class Operand {
    data class Constant(val value: Long) : Operand()
    data object Old : Operand()
}

private data class Test(
    val modulus: Int,
    private val trueMonkeyIndex: Int,
    private val falseMonkeyIndex: Int
) {
    fun nextMonkeyIndex(worry: Long) = if (worry % modulus == 0L) trueMonkeyIndex else falseMonkeyIndex
}

private class MonkeyParser {
    companion object {
        fun List<String>.parseMonkeys() = with(MonkeyParser()) {
            split { line -> line.isBlank() }.map { monkeyStrings -> parseMonkey(monkeyStrings) }
        }
    }

    private val startingItemsParser = startingItemsParser()
    private val operationParser = operationParser()
    private val testParser = TestParser()

    fun parseMonkey(monkeyStrings: List<String>) = Monkey(
        startingItems = startingItemsParser.parse(monkeyStrings[1]),
        operation = operationParser.parse(monkeyStrings[2]),
        test = testParser.parseTest(monkeyStrings.subList(3, 6))
    )
}

private fun startingItemsParser() =
    RegexParser("""  Starting items: ((?:\d+(?:, )?)+)""") { captures ->
        StartingItems(
            itemWorryNumbers = captures[0]
                .split(", ")
                .map { worryString -> worryString.toLong() }
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
                ?.let { constantString -> Constant(constantString.toLong()) }
                ?: Old
        )
    }

private class TestParser {
    private val testModulusParser = testModulusParser()
    private val trueMonkeyIndexParser = trueMonkeyIndexParser()
    private val falseMonkeyIndexParser = falseMonkeyIndexParser()

    fun parseTest(testStrings: List<String>) = Test(
        modulus = testModulusParser.parse(testStrings[0]),
        trueMonkeyIndex = trueMonkeyIndexParser.parse(testStrings[1]),
        falseMonkeyIndex = falseMonkeyIndexParser.parse(testStrings[2])
    )
}

private fun testModulusParser() =
    intParser("""  Test: divisible by (\d+)""")

private fun trueMonkeyIndexParser() =
    intParser("""    If true: throw to monkey (\d+)""")

private fun falseMonkeyIndexParser() =
    intParser("""    If false: throw to monkey (\d+)""")
