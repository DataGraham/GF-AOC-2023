package aoc2022.day05

import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day05/Day05_test")
    check(part1(testInput).also { it.println() } == "CMZ")
    //check(part2(testInput).also { it.println() } == 4)

    //val input = readInput("aoc2022/day05/Day05")
    //println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): String {
    val regex = Regex("""( {3}|\[\p{Upper}])""")
    input.forEach { line ->
        val matchResults = regex.findAll(line)
        println("For line: $line")
        println("Found matches: ${matchResults.map { it.value }.toList()}")
    }
    return ""
}

fun part2(input: List<String>): String {
    return ""
}