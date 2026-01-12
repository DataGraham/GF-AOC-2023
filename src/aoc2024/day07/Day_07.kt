package aoc2024.day07

import RegexParser
import parseLines
import printLines
import println
import readInput
import java.util.regex.Pattern

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day07/Day07_test")
    check(part1(testInput).also { it.println() } == 161)
    //check(part2(testInput).also { it.println() } == 1)

    //val input = readInput("aoc2024/day07/Day07")
    //println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val calibrationEquations = calibrationParser
        .parseLines(input)
        .printLines()
    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}

data class CalibrationEquation(
    val result: Int,
    val inputs: List<Int>
)

val calibrationParser by lazy {
    RegexParser("""(\d+): (.*)""") { captures ->
        val (resultString, inputsString) = captures
        CalibrationEquation(
            result = resultString.toInt(),
            inputs = inputsString
                .split(" ")
                .map { it.toInt() }
        )
    }
}