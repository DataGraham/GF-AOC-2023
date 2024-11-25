package aoc2022.day13

import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day13/Day13_test")
    check(part1(testInput).also { it.println() } == 13)
    //check(part2(testInput).also { it.println() } == 29)

    //val input = readInput("aoc2022/day13/Day13")
    //println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) = input.size

fun part2(input: List<String>) = input.size

private fun parseElements(input: List<String>) {
    val tokenRegex = Regex("""\[|]|(d+)""")
    input.forEach { line ->
        tokenRegex.findAll(line).println()
    }
}

private sealed class Element {
    data class IntegerElement(val value: Int) : Element()
    data class ArrayElement(val elements: List<Element>) : Element()
}