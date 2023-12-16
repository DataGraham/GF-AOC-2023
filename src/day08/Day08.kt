package day08

import println
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
    val networkConnectionsByStart = getNetworkConnectionsByStart(input)
    val locations = input.getEndlessDirections().scan("AAA") { location, direction ->
        networkConnectionsByStart.move(from = location, direction = direction)
    }
    return locations.indexOf("ZZZ")
}

fun part2(input: List<String>): Int {
    val networkConnectionsByStart = getNetworkConnectionsByStart(input)
    val initialLocations = networkConnectionsByStart.keys.filter { it.last() == 'A' }
    val pathLengths = initialLocations.map { initialLocation ->
        input.getEndlessDirections().scan(initialLocation) { currentLocation, direction ->
            networkConnectionsByStart.move(from = currentLocation, direction = direction)
        }.indexOfFirst { it.last() == 'Z' }
    }.also { it.println() }
    return 0
}

private fun List<String>.getEndlessDirections() = first().toList().toInfiniteSequence()

private fun getNetworkConnectionsByStart(input: List<String>) = input
    .drop(2)
    .map { line -> NetworkConnection.parseFromString(line) }
    .associateBy { it.start }

fun Map<String, NetworkConnection>.move(from: String, direction: Char) =
    this[from]!!.let {
        when (direction) {
            'L' -> it.left
            'R' -> it.right
            else -> throw IllegalArgumentException("Invalid direction: $direction")
        }
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