package day03
import readInput

fun main() {

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day03/Day03_test")
    check(part1(testInput) == 4361)
    //check(part2(testInput) == 1)

    val input = readInput("day03/Day03")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    return input.foldIndexed(0) { index, sum, line ->
        sum +  Regex("([0-9]+)").findAll(line).sumOf { it.value.toInt() }
    }.also { println("Calculated $it") }
}

fun part2(input: List<String>): Int {
    return input.size
}
