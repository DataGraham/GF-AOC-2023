package day06

import readInput
import requireSubstringAfter

fun main() {

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day06/Day06_test")
    check(part1(testInput) == 288)
    //check(part2(testInput) == 1)

    val input = readInput("day06/Day06")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val times = extractRaceNumbers(input[0])
    val recordDistances = extractRaceNumbers(input[1])
    return times.zip(recordDistances).map { (time, recordDistance) ->
        (0 .. time).count { holdTime -> holdTime * (time - holdTime) > recordDistance }
    }.reduce { waysToWinAll, waysToWinThisRace -> waysToWinAll * waysToWinThisRace }
}

fun part2(input: List<String>): Int {
    return input.size
}

fun extractRaceNumbers(line: String) =
    line.requireSubstringAfter(':')
        .split(' ')
        .filter(String::isNotBlank)
        .map(String::toInt)