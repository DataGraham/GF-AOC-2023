package aoc2024.day05

import Parser
import RegexParser
import UniversalParser
import aoc2024.day05.PrintingLine.*
import middleElement
import parseLines
import println
import readInput
import require

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day05/Day05_test")
    check(part1(testInput).also { it.println() } == 143)
    check(part2(testInput).also { it.println() } == 123)

    val input = readInput("aoc2024/day05/Day05")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val (rules, updates) = parse(input)
    return updates
        .filter { update -> update.followsRules(rules) }
        .sumOf { correctUpdate -> correctUpdate.pageNumbers.middleElement() }
}

fun part2(input: List<String>): Int {
    // Turns out this isn't necessary (which TBH isn't surprising given that it's only day 5),
    //  which suggests that all transitive rules that arise from explicit rules are given explicitly.
    // By count, I see that in both the test and "real" inputs, when there are 'N' page numbers included
    //  in any rule, then there are exactly 'N choose 2' (N(N-1)/2) rules, which is the exact number of rules
    //  expected if all transitive rules are given explicitly.
    /* TODO: Sum middle-elements of rule-breaking updates, AFTER having corrected each of their page orders.
        If A|B and B|C, but not explicitly A|C, then we might not be giving enough of an "order" for sorting,
        because we'd claim A and C are equivalent, when they actually aren't (well, IF B appears).
        So, we might have to link together rules into chains, where for each rule, we link to other rules
        that share the same "earlier" or "later" page number.
        Then, we could start with any rule and label that 0, and then go through "earlier" and "later" rules
        labelling them -1, -2... and 1, 2...
        Then, when comparing for sort, we find whether there is a rule chain that includes both page numbers
        and compare there indices within that rule chain?!

        FWIW, bedtime afterthoughts from Keep:
        A rule is not a node, a number is a node
        A rule links two number nodes
        Once all are linked:
        Find all nodes with empty lists of nodes that come before and add 0 to their 'weight'.
        Adding N to a weight also adds N+1 to all nodes that come after (recursively).
        Now we should be able to sort by just comparing weights.
    */
    val (rules, updates) = parse(input)
    //    val ruledPageNumbers =
    //        rules.flatMap { listOf(it.earlierPageNumber, it.laterPageNumber) }.toSet()
    val ruleComparator = RuleComparator(rules)
    return updates
        .filter { update -> !update.followsRules(rules) }
        .map { incorrectUpdate -> Update(pageNumbers = incorrectUpdate.pageNumbers.sortedWith(ruleComparator)) }
        .sumOf { correctedUpdates -> correctedUpdates.pageNumbers.middleElement() }
}

private fun parse(input: List<String>): Pair<List<Rule>, List<Update>> {
    val lines = printingLineParser
        .parseLines(input)
    val rules = lines.filterIsInstance<Rule>()
    val updates = lines.filterIsInstance<Update>()
    return Pair(rules, updates)
}

class RuleComparator(private val rules: List<Rule>) : Comparator<Int> {
    override fun compare(o1: Int, o2: Int): Int {
        val applicableRule = rules.firstOrNull { rule ->
            rule appliesToPage o1 && rule appliesToPage o2
        } ?: return 0 // equivalent if no rule applies
        return if (applicableRule.earlierPageNumber == o1) -1 else 1
    }
}

sealed class PrintingLine {
    data class Rule(val earlierPageNumber: Int, val laterPageNumber: Int) : PrintingLine()
    data class Update(val pageNumbers: List<Int>) : PrintingLine()
    data object Blank : PrintingLine()
}

private fun Update.followsRules(rules: List<Rule>) =
    rules.none { rule -> breaks(rule) }

fun Update.breaks(rule: Rule): Boolean {
    val earlierPageIndex = pageNumbers.indexOf(rule.earlierPageNumber)
    val laterPageIndex = pageNumbers.indexOf(rule.laterPageNumber)
    return earlierPageIndex != -1 && laterPageIndex != -1 && earlierPageIndex > laterPageIndex
}

infix fun Rule.appliesToPage(pageNumber: Int) =
    earlierPageNumber == pageNumber || laterPageNumber == pageNumber

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