package aoc2022.day02

import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day02/Day02_test")
    check(part1(testInput).also { it.println() } == 15)
    //check(part2(testInput).also { it.println() } == 1)

    //val input = readInput("day02/Day02")
    //println("Part 1 Answer: ${day02.part1(input)}")
    //println("Part 2 Answer: ${day02.part2(input)}")
}

fun checkBeats() = Hand.entries.forEach { hand ->
    Hand.entries.forEach { other ->
        println("$hand beats $other is ${hand beats other}")
    }
}

enum class Hand() {
    Rock {
        override val beats get() = Scissors
    },
    Paper {
        override val beats get() = Rock
    },
    Scissors {
        override val beats get() = Paper
    };

    abstract val beats: Hand

    infix fun beats(other: Hand) = other == beats
}

fun part1(input: List<String>): Int {
    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}
