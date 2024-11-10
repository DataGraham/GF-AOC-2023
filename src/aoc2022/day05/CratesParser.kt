package aoc2022.day05

import transposed

object CratesParser {
    /** Stacks from left ("1") to right, each with crates from top to bottom. */
    fun parseCrates(input: List<String>) = Crates(
        initialStacks = parseCrateRows(input)
            .transposed()
            .map { crateStack -> crateStack.filterNotNull() }
    )

    private fun parseCrateRows(input: List<String>) =
        input.map { line -> parseCrateLabels(line) }
            .takeWhile { crateLabels -> crateLabels.any { it != null } }

    private fun parseCrateLabels(line: String) =
        crateSpaceRegex
            .findAll(line)
            .map { matchResult -> matchResult.groups[1]?.value?.first() }
            .toList()

    private val crateSpaceRegex by lazy { Regex(""" {4}|\[(\p{Upper})] ?""") }
}