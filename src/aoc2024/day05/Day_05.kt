package aoc2024.day05

import Parser
import RegexParser
import UniversalParser
import aoc2024.day05.PrintingLine.*
import middleElement
import parseLines
import printLines
import println
import readInput
import require

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day05/Day05_test")
    check(part1(testInput).also { it.println() } == 143)
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("aoc2024/day05/Day05")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val lines = printingLineParser
        .parseLines(input)
        .apply { printLines() }
    val rules = lines.mapNotNull { it as? Rule }
    val updates = lines.mapNotNull { it as? Update }
    return updates
        .filter { update -> update.followsRules(rules) }
        .sumOf { correctUpdate -> correctUpdate.pageNumbers.middleElement() }
}

private fun Update.followsRules(rules: List<Rule>) =
    rules.none { rule -> breaks(rule) }

fun part2(input: List<String>): Int {
    return input.size
}

sealed class PrintingLine {
    data class Rule(val earlierPageNumber: Int, val laterPageNumber: Int) : PrintingLine()
    data class Update(val pageNumbers: List<Int>) : PrintingLine()
    data object Blank : PrintingLine()
}

fun Update.breaks(rule: Rule): Boolean {
    val earlierPageIndex = pageNumbers.indexOf(rule.earlierPageNumber)
    val laterPageIndex = pageNumbers.indexOf(rule.laterPageNumber)
    return earlierPageIndex != -1 && laterPageIndex != -1 && earlierPageIndex > laterPageIndex
}

val printingLineParser by lazy {
    UniversalParser(ruleParser, updateParser, blankParser)
}

val ruleParser by lazy {
    RegexParser("""(\d+)\|(\d+)""") { captures ->
        captures
            .map { it.toInt() }
            .let { pageNumbers ->
                Rule(earlierPageNumber = pageNumbers[0], laterPageNumber = pageNumbers[1])
            }
    }
}

val updateParser by lazy {
    object : Parser<Update> {
        override fun parse(line: String) =
            Update(
                pageNumbers = line
                    .split(',')
                    .map { it.toInt() }
                    .require { isNotEmpty() }
            )
    }
}

val blankParser by lazy {
    object : Parser<Blank> {
        override fun parse(line: String) =
            Blank.also { require(line.isBlank()) }
    }
}