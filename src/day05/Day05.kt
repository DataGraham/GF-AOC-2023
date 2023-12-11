package day05

import readInput
import requireSubstringAfter
import split

fun main() {

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day05/Day05_test")
    check(part1(testInput) == 35)
    //check(part2(testInput) == 1)

    val input = readInput("day05/Day05")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val seeds = input.first()
        .requireSubstringAfter(':')
        .trim()
        .split(' ')
        .map(String::toInt)
    val mapStringSets = input.drop(1).split { it.isBlank() }
    val maps = mapStringSets.map { mapStringSet ->
        mapStringSet.drop(1).map { mapping ->
            mapping
                .split(' ')
                .map(String::toInt)
                .takeIf { it.size == 3 }!!
                .let { (destStart, sourceStart, length) ->
                    MappingRange(
                        sourceRange = sourceStart ..< sourceStart + length,
                        destDelta = destStart - sourceStart
                    )
                }
        }
    }
    return seeds.minOf { seed ->
        maps.mapSeedToLocation(seed).also {
            println("Mapped Seed $seed to location $it")
        }
    }
}

fun part2(input: List<String>): Int {
    return input.size
}

data class MappingRange(val sourceRange: IntRange, val destDelta: Int) : Comparable<Int> {
    override fun compareTo(other: Int): Int {
        TODO("Not yet implemented")
    }
}

fun List<List<MappingRange>>.mapSeedToLocation(seed: Int): Int {
    // TODO: Use Fold
    var mappedValue = seed
    forEach { map ->
        // TODO: Binary search to find relevant mapping range?
        mappedValue = map.firstOrNull { mappedValue in it.sourceRange }
            ?.let { mappedValue + it.destDelta }
            ?: mappedValue
    }
    return mappedValue
}