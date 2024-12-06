package aoc2022.day16

import RegexParser
import parseLines
import printLines
import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day16/Day16_test")
    //val input = readInput("aoc2022/day16/Day16")

    check(part1(testInput).also { it.println() } == 26)
    //println("Part 1 Answer: ${part1(input)}")

    //check(part2(testInput).also { it.println() } == 1)
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val valves = valveParser.parseLines(input)
    valves.printLines()
    return input.size
}

private val valveParser =
    RegexParser("""Valve (\p{Upper}+) has flow rate=(\d+); tunnels? leads? to valves? (.*)""") { captures ->
        Valve(
            name = captures[0],
            flowRate = captures[1].toInt(),
            leadsTo = captures[2].split(", ")
        )
    }

data class Valve(
    val name: String,
    val flowRate: Int,
    val leadsTo: List<String>
)

fun part2(input: List<String>) = input.size
