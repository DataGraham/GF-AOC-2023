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
    // check(part2(testInput) == 46)

    val input = readInput("day05/Day05")
    measurePerformance(label = "Part 1 (Linear Search)", reps = 5000) { part1LinearSearch(input) }
    measurePerformance(label = "Part 1 (Binary Search)", reps = 5000) { part1BinarySearch(input) }
    println("Part 2 Answer: ${part2(input)}")
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

fun part2(input: List<String>): Long {
    val seedRanges = input.first()
        .requireSubstringAfter(':')
        .trim()
        .split(' ')
        .map(String::toLong)
        .chunked(2)
        .map { (start, length) -> start ..< start + length }
    val mappings = parseMappings(input.drop(1))
    // TODO: Collapse mappings together into a single mapping?
    return runBlocking {
        seedRanges.map { seedRange ->
            async {
                withContext(Dispatchers.Default) {
                    seedRange.minOf { seed -> mappings.mapInput(seed, Mapping::mapInputBinarySearch) }
                }
            }
        }.minOf { it.await() }
    }
}

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