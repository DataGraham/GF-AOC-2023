package aoc2022.day01

import println
import readInput
import split

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day01/Day01_test")
    check(part1(testInput).also { it.println() } == 24000)
    check(part2(testInput).also { it.println() } == 45000)

    val input = readInput("aoc2022/day01/Day01")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) = input.toElves().maxOf(Elf::totalCalories)

private val Elf.totalCalories get() = foodItems.sumOf { it.calories }

fun part2(input: List<String>) = input.toElves().map { it.totalCalories }.sorted().takeLast(3).sum()

data class FoodItem(val calories: Int)

data class Elf(val foodItems: List<FoodItem>)

fun List<String>.toElves() =
    split { it.isBlank() }
        .map { elfCalorieStrings ->
            elfCalorieStrings.toElf()
        }

private fun List<String>.toElf() = Elf(
    foodItems = map { caloriesString ->
        FoodItem(calories = caloriesString.toInt())
    }
)