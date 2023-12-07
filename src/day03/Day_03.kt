package day03
import readInput

fun main() {

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day03/Day03_test")
    check(part1(testInput) == 1)
    //check(part2(testInput) == 1)

    val input = readInput("day03/Day03")
    println("Part 1 Answer: ${day02.part1(input)}")
    println("Part 2 Answer: ${day02.part2(input)}")
}

fun part1(input: List<String>): Int {
    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}
