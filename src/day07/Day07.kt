package day07

import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day07/Day07_test")
    check(part1(testInput) == 6440)
    //check(part2(testInput) == 1)

    val input = readInput("day07/Day07")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val hands = input.map { line ->
        line.split(' ').let { (cardsString, bidString) ->
            Hand(cards = cardsString, bid = bidString.toInt())
        }
    }.apply { joinToString(separator = "\n").println() }
    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}

data class Hand(val cards: String, val bid: Int)
