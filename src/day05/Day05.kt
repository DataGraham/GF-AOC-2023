package day05

import measurePerformance
import readInput
import requireSubstringAfter
import split

fun main() {

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day05/Day05_test")
    check(part1LinearSearch(testInput) == 35L)
    check(part1BinarySearch(testInput) == 35L)
    //check(part2(testInput) == 1)

    val input = readInput("day05/Day05")
    measurePerformance(label = "Part 1 (Linear Search)", reps = 5000) { part1LinearSearch(input) }
    measurePerformance(label = "Part 1 (Binary Search)", reps = 5000) { part1BinarySearch(input) }
    // println("Part 2 Answer: ${part2(input)}")
}

fun part1LinearSearch(input: List<String>) = part1(input) { mapInputLinearSearch(it) }

fun part1BinarySearch(input: List<String>) = part1(input) { mapInputBinarySearch(it) }

fun part1(input: List<String>, mappingStrategy: Mapping.(Long) -> Long): Long {
    val seeds = input.first()
        .requireSubstringAfter(':')
        .trim()
        .split(' ')
        .map(String::toLong)
    val mapStringSets = input.drop(1).split { it.isBlank() }
    val mappings = mapStringSets.map { mapStringSet ->
        Mapping(
            mapStringSet.drop(1).map { mapping ->
                mapping
                    .split(' ')
                    .map(String::toLong)
                    .takeIf { it.size == 3 }!!
                    .let { (destStart, sourceStart, length) ->
                        MappingRange(
                            sourceRange = sourceStart ..< sourceStart + length,
                            destDelta = destStart - sourceStart
                        )
                    }
            }
        )
    }
    return seeds.minOf { seed ->
        mappings.mapSeedToLocation(seed, mappingStrategy)
        //.also { println("Mapped Seed $seed to location $it") }
    }
}

fun part2(input: List<String>): Int {
    return input.size
}

data class MappingRange(val sourceRange: LongRange, val destDelta: Long)

class Mapping(private val mappingRanges: List<MappingRange>) {
    private val sortedMappingRanges by lazy { mappingRanges.sortedBy { it.sourceRange.first } }

    // TODO: Binary search to find relevant mapping range?
    fun mapInputLinearSearch(input: Long): Long =
        mappingRanges.firstOrNull { input in it.sourceRange }
            ?.let { input + it.destDelta }
            ?: input

    fun mapInputBinarySearch(input: Long): Long =
        sortedMappingRanges.binarySearch { mappingRange ->
            when {
                mappingRange.sourceRange.contains(input) -> 0
                mappingRange.sourceRange.last < input -> -1
                else -> 1
            }
        }.takeIf { rangeIndex -> rangeIndex >= 0 }
            ?.let { rangeIndex -> input + sortedMappingRanges[rangeIndex].destDelta }
            ?: input
}

fun List<Mapping>.mapSeedToLocation(seed: Long, mappingStrategy: Mapping.(Long) -> Long) =
    fold(seed) { mappedValue, mapping -> mapping.mappingStrategy(mappedValue) }