package day08

import readInput
import toInfiniteSequence
import java.lang.IllegalArgumentException

fun main() {

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day08/Day08_test")
    check(part1(testInput) == 6)
    //check(part2(testInput) == 1)

    val input = readInput("day08/Day08")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val endlessDirections = input.first().toList().toInfiniteSequence()
    val networkConnectionsByStart = input
        .drop(2)
        .map { line -> NetworkConnection.parseFromString(line) }
        .associateBy { it.start }
    val locations = endlessDirections.scan("AAA") { location, direction ->
        networkConnectionsByStart[location]!!.let {
            when (direction) {
                'L' -> it.left
                'R' -> it.right
                else -> throw IllegalArgumentException("Invalid direction: $direction")
            }
        }
    }
    return locations.indexOf("ZZZ")
}

fun part2(input: List<String>): Int {
    return input.size
}

data class NetworkConnection(
    val start: String,
    val left: String,
    val right: String
) {
    companion object {
        private val connectionRegex = Regex("""([A-Z]+) = \(([A-Z]+), ([A-Z]+)\)""")

        fun parseFromString(line: String) = connectionRegex
            .matchEntire(line)!!
            .let { matchResult -> matchResult.groups.drop(1).map { it!!.value } }
            .let { (start, left, right) ->
                NetworkConnection(start = start, left = left, right = right)
            }
    }
}