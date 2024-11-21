package aoc2022.day11

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
    val monkeys = input.split { it.isBlank() }
    val startingItems = with (startingItemsParser()) {
        monkeys.map { monkey ->
            parse(monkey[1])
        }
    }
    return input.size
}

fun part2(input: List<String>) = input.size

data class StartingItems(val itemWorryNumbers: List<Int>)

// TODO: Could we get it to spit out a group matching that has an array of each string of digits with the commas removed already?
val regex = Regex(""" *Starting items: ((?:\d+(?:, )?)+)""")

private fun startingItemsParser() = RegexParser(""" *Starting items: ((?:\d+(?:, )?)+)""") { captures ->
    StartingItems(
        itemWorryNumbers = captures[0].split(", ").map { it.toInt() }
    )
}

private interface Parser<T> {
    fun parse(line: String): T?
}

// TODO: Use this generalized version elsewhere?
private class UniversalParser<T>(private vararg val parsers: Parser<T>) : Parser<T> {
    fun List<String>.parseInstructions() = map { line -> parse(line) }
    override fun parse(line: String) = parsers.firstNotNullOf { parser -> parser.parse(line) }
}

// TODO: Use this generalized version elsewhere?
private class RegexParser<T>(
    private val pattern: String,
    private val processCaptures: (List<String>) -> T
) : Parser<T> {
    private val regex by lazy { Regex(pattern) }

    override fun parse(line: String) = regex.matchEntire(line)?.let { match ->
        processCaptures(match.groupValues.drop(1))
    }
}
