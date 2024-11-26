package aoc2022.day14

import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day14/Day14_test")
    check(part1(testInput).also { it.println() } == 24)
    //check(part2(testInput).also { it.println() } == 140)

    //val input = readInput("aoc2022/day14/Day14")
    //println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) = input.size
/* TODO:
Read in each line as a path with a series of coordinate pairs.
Find the min-max range (including the assumed 500, 0 sand start position).
Create a grid and initialize, subtracting the minimum from each x and y.
Iterate each piece of sand until it rests,
finally stopping just before the first piece that falls outside of the min/max grid.
*/

fun part2(input: List<String>) = input.size
