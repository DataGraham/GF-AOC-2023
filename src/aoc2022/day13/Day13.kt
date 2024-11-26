package aoc2022.day13

import aoc2022.day13.Element.ArrayElement
import aoc2022.day13.Element.IntegerElement
import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day13/Day13_test")
    check(part1(testInput).also { it.println() } == 13)
    //check(part2(testInput).also { it.println() } == 29)

    val input = readInput("aoc2022/day13/Day13")
    println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    return parseElements(input)
        .chunked(2)
        .mapIndexed { pairIndex, (left, right) ->
            if (left < right) pairIndex + 1 else 0
        }
        .sum()
}

fun part2(input: List<String>) = input.size

private fun parseElements(input: List<String>): List<Element> {
    return input
        .filter { line -> line.isNotBlank() }
        .map { line -> parseElement(line) }
}

private sealed class Element : Comparable<Element> {
    data class IntegerElement(val value: Int) : Element() {
        override fun compareTo(other: Element) = when (other) {
            is IntegerElement -> value.compareTo(other.value)
            is ArrayElement -> ArrayElement(this).compareTo(other)
        }

        override fun toString() = value.toString()
    }

    data class ArrayElement(val elements: List<Element>) : Element() {
        constructor(vararg elements: Element) : this(elements.toList())

        override fun compareTo(other: Element): Int = when (other) {
            is IntegerElement -> compareTo(ArrayElement(other))
            is ArrayElement -> elements
                .zip(other.elements)
                .map { (left, right) -> left.compareTo(right) }
                .firstOrNull { comparison -> comparison != 0 }
                ?: elements.size.compareTo(other.elements.size)
        }

        override fun toString(): String {
            return "[${elements.joinToString(separator = ",")}]"
        }
    }
}

private fun parseElement(line: String): ArrayElement {
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
