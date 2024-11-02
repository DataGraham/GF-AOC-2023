package aoc2022.day02

import aoc2022.day02.Hand.*
import aoc2022.day02.RoundResult.*
import aoc2022.day02.StrategyDecoder.toRound
import println
import readInput
import require

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day02/Day02_test")
    check(part1(testInput).also { it.println() } == 15)
    //check(part2(testInput).also { it.println() } == 1)

    //val input = readInput("day02/Day02")
    //println("Part 1 Answer: ${day02.part1(input)}")
    //println("Part 2 Answer: ${day02.part2(input)}")
}

fun part1(input: List<String>) = input.sumOf { it.toRound().score }

fun part2(input: List<String>): Int {
    return input.size
}

data class Round(val opponentHand: Hand, val selfHand: Hand)

val Round.score get() = selfHand.intrinsicScore + result.score

val Round.result
    get() = when {
        selfHand beats opponentHand -> Win
        selfHand == opponentHand -> Tie
        else -> Lose.require { opponentHand beats selfHand }
    }

enum class RoundResult(val score: Int) {
    Win(score = 6),
    Lose(score = 0),
    Tie(score = 3)
}

object StrategyDecoder {
    fun String.toRound(): Round {
        val (opponentCode, selfCode) = split(' ')
            .require { size == 2 }
            .require { all { it.length == 1 } }
            .map { it.first() }
        return Round(
            opponentHand = opponentCode.toOpponentHand(),
            selfHand = selfCode.toSelfHand()
        )
    }

    private fun Char.toOpponentHand() = opponentHandsByCode[this]!!
    private fun Char.toSelfHand() = selfHandsByCode[this]!!

    private val opponentHandsByCode = mapOf(
        'A' to Rock,
        'B' to Paper,
        'C' to Scissors
    )

    private val selfHandsByCode = mapOf(
        'X' to Rock,
        'Y' to Paper,
        'Z' to Scissors
    )

}

enum class Hand(val intrinsicScore: Int) {
    Rock(intrinsicScore = 1) {
        override val beats get() = Scissors
    },
    Paper(intrinsicScore = 2) {
        override val beats get() = Rock
    },
    Scissors(intrinsicScore = 3) {
        override val beats get() = Paper
    };

    abstract val beats: Hand

    infix fun beats(other: Hand) = other == beats
}

fun checkBeats() = Hand.entries.forEach { hand ->
    Hand.entries.forEach { other ->
        println("$hand beats $other is ${hand beats other}")
    }
}
