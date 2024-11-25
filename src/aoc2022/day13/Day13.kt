package aoc2022.day13

import aoc2022.day13.Element.ArrayElement
import aoc2022.day13.Element.IntegerElement
import printLines
import println
import readInput
import kotlin.coroutines.CoroutineContext

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day13/Day13_test")
    check(part1(testInput).also { it.println() } == 13)
    //check(part2(testInput).also { it.println() } == 29)

    //val input = readInput("aoc2022/day13/Day13")
    //println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    parseElements(input)
    return input.size
}

fun part2(input: List<String>) = input.size

private fun parseElements(input: List<String>) {
    input
        .filter { line -> line.isNotBlank() }
        .map { line -> parseElement(line) }
        .printLines()
}

private fun parseElement(line: String): Element {
    val tokenRegex = Regex("""\[|]|\d+""")
    val tokens = tokenRegex.findAll(line).map { match -> match.value }
    val arrayStack = mutableListOf<MutableList<Element>>()
    tokens.forEach { token ->
        when (token) {
            "[" -> arrayStack += mutableListOf<Element>()
            "]" -> {
                val finishedElement = ArrayElement(arrayStack.removeLast())
                if (arrayStack.isEmpty()) return finishedElement
                else arrayStack.last() += finishedElement
            }

            else -> arrayStack.last() += IntegerElement(token.toInt())
        }
    }
    throw IllegalArgumentException("Missing ']'")
}

private sealed class Element {
    data class IntegerElement(val value: Int) : Element() {
        override fun toString() = value.toString()
    }
    data class ArrayElement(val elements: List<Element>) : Element() {
        override fun toString(): String {
            return "[${elements.joinToString(separator = ",")}]"
        }
    }
}