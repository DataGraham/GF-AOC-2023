package day05

import kotlinx.coroutines.*
import measurePerformance
import readInput
import requireSubstringAfter
import split
import kotlin.math.max
import kotlin.math.min

fun main() {

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day05/Day05_test")
    check(part1LinearSearch(testInput) == 35L)
    check(part1BinarySearch(testInput) == 35L)
    check(part2MapEachSeed(testInput) == 46L)

    val input = readInput("day05/Day05")
    measurePerformance(label = "Part 1 (Linear Search)", reps = 5000) { part1LinearSearch(input) }
    measurePerformance(label = "Part 1 (Binary Search)", reps = 5000) { part1BinarySearch(input) }
    // println("Part 2 Answer (Each Seed): ${part2MapEachSeed(input)}")
    measurePerformance(label = "Part 2 (Map Seed Ranges)", reps = 100) { part2MapSeedRanges(input) }
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
    return mappings.fold(seedRanges) { inputRanges, mapping ->
        inputRanges.flatMap { inputRange ->
            mapping.mapInputRange(inputRange)
        }
    }.also { println(it.size) }.minOf { it.first }
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

    fun mapInputRange(inputRange: LongRange): List<LongRange> {
        return sortedMappingRanges.fold(
            RangeMappingState(
                mappedRanges = emptyList(),
                unmappedInput = inputRange
            )
        ) { state, mappingRange ->
            val result = mappingRange.mapInputRange(state.unmappedInput ?: return@fold state)
            RangeMappingState(
                mappedRanges = state.mappedRanges + listOfNotNull(result.unmappedPrefix, result.mappedRange),
                unmappedInput = result.unmappedSuffix
            )
        }.let { finalState ->
            finalState.mappedRanges + listOfNotNull(finalState.unmappedInput)
        }
    }

    private fun MappingRange.mapInputRange(inputRange: LongRange): RangeMappingResult = RangeMappingResult(
        unmappedPrefix = (inputRange.first..min(sourceRange.first - 1, inputRange.last))
            .takeIf { !it.isEmpty() },
        mappedRange = (max(inputRange.first, sourceRange.first)..min(inputRange.last, sourceRange.last))
            .takeIf { !it.isEmpty() }
            ?.let { mappedInputRange -> mappedInputRange.first + destDelta..mappedInputRange.last + destDelta },
        unmappedSuffix = (max(inputRange.first, sourceRange.last + 1)..inputRange.last)
            .takeIf { !it.isEmpty() }
    )
}

data class RangeMappingState(
    val mappedRanges: List<LongRange>,
    val unmappedInput: LongRange?
)

data class RangeMappingResult(
    val unmappedPrefix: LongRange?,
    val mappedRange: LongRange?,
    val unmappedSuffix: LongRange?
)

fun List<Mapping>.mapInput(input: Long, mappingStrategy: Mapping.(Long) -> Long) =
    fold(input) { mappedValue, mapping -> mapping.mappingStrategy(mappedValue) }
