package day05

import kotlinx.coroutines.*
import measurePerformance
import readInput
import requireSubstringAfter
import split

fun main() {

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day05/Day05_test")
    check(part1LinearSearch(testInput) == 35L)
    check(part1BinarySearch(testInput) == 35L)
    check(part2MapEachSeed(testInput) == 46L)

    val input = readInput("day05/Day05")
    measurePerformance(label = "Part 1 (Linear Search)", reps = 5000) { part1LinearSearch(input) }
    measurePerformance(label = "Part 1 (Binary Search)", reps = 5000) { part1BinarySearch(input) }
    println("Part 2 Answer (Each Seed): ${part2MapEachSeed(input)}")
    // println("Part 2 Answer (Seed Ranges): ${part2MapSeedRanges(input)}")
}

fun part1LinearSearch(input: List<String>) = part1(input, Mapping::mapInputLinearSearch)

fun part1BinarySearch(input: List<String>) = part1(input, Mapping::mapInputBinarySearch)

fun part1(input: List<String>, mappingStrategy: Mapping.(Long) -> Long): Long {
    val seeds = input.first()
        .requireSubstringAfter(':')
        .trim()
        .split(' ')
        .map(String::toLong)
    val mappings = parseMappings(input.drop(1))
    return seeds.minOf { seed -> mappings.mapInput(seed, mappingStrategy) }
}

fun part2MapEachSeed(input: List<String>): Long {
    val seedRanges = parseSeedRanges(input.first())
    val mappings = parseMappings(input.drop(1))
    return runBlocking(Dispatchers.Default) {
        seedRanges.map { seedRange ->
            async {
                seedRange.minOf { seed -> mappings.mapInput(seed, Mapping::mapInputBinarySearch) }
            }
        }.minOf { it.await() }
    }
}

fun part2MapSeedRanges(input: List<String>): Long {
    val seedRanges = parseSeedRanges(input.first())
    val mappings = parseMappings(input.drop(1))
    return 0
}

private fun parseSeedRanges(seedRangesString: String) = seedRangesString
    .requireSubstringAfter(':')
    .trim()
    .split(' ')
    .map(String::toLong)
    .chunked(2)
    .map { (start, length) -> start ..< start + length }

private fun parseMappings(input: List<String>): List<Mapping> =
    input
        .split { it.firstOrNull()?.isDigit() != true }
        .map { mappingStringSet -> Mapping(mappingStringSet.map(::parseMappingRange)) }

private fun parseMappingRange(mappingString: String) =
    mappingString
        .split(' ')
        .map(String::toLong)
        .takeIf { it.size == 3 }!!
        .let { (destStart, sourceStart, length) ->
            MappingRange(
                sourceRange = sourceStart ..< sourceStart + length,
                destDelta = destStart - sourceStart
            )
        }

data class MappingRange(val sourceRange: LongRange, val destDelta: Long)

class Mapping(private val mappingRanges: List<MappingRange>) {
    private val sortedMappingRanges by lazy { mappingRanges.sortedBy { it.sourceRange.first } }

    fun mapInputLinearSearch(input: Long): Long =
        mappingRanges.firstOrNull { input in it.sourceRange }
            ?.let { input + it.destDelta }
            ?: input

    fun mapInputBinarySearch(input: Long): Long =
        sortedMappingRanges.binarySearch { mappingRange ->
            with(mappingRange.sourceRange) {
                when {
                    last < input -> -1
                    first > input -> 1
                    else -> 0
                }
            }
        }.takeIf { rangeIndex -> rangeIndex >= 0 }
            ?.let { rangeIndex -> input + sortedMappingRanges[rangeIndex].destDelta }
            ?: input
}

fun List<Mapping>.mapInput(input: Long, mappingStrategy: Mapping.(Long) -> Long) =
    fold(input) { mappedValue, mapping -> mapping.mappingStrategy(mappedValue) }