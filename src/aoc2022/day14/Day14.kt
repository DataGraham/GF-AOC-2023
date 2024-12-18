package aoc2022.day14

import DeltaPosition
import Position
import aoc2022.day14.Cave.CaveFilling.*
import aoc2022.day14.Cave.SandResult.*
import aoc2022.day14.RockFormationParser.parseRockFormations
import plus
import println
import readInput
import relativeTo
import kotlin.math.max
import kotlin.math.min

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day14/Day14_test")
    val input = readInput("aoc2022/day14/Day14")

    check(part1(testInput).also { it.println() } == 24)
    println("Part 1 Answer: ${part1(input)}")

    check(part2(testInput).also { it.println() } == 93)
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    // Read in each line as a path with a series of coordinate pairs.
    val rockFormations = parseRockFormations(input)

    // Find the min-max range (including the assumed 500, 0 sand start position).
    val allPositions = rockFormations.flatten() + sandStartPosition
    val origin = Position(
        row = allPositions.minOf { it.row },
        col = allPositions.minOf { it.col }
    )
    val max = Position(
        row = allPositions.maxOf { it.row },
        col = allPositions.maxOf { it.col }
    )
    val numRows = max.row - origin.row + 1
    val numCols = max.col - origin.col + 1

    val relativeRockFormations = rockFormations.map { rockFormation ->
        rockFormation.map { position ->
            position relativeTo origin
        }
    }
    val relativeSandStart = sandStartPosition relativeTo origin

    // Create a grid and initialize, subtracting the minimum from each x and y.
    val cave = Cave(numRows = numRows, numCols = numCols)
    cave.fillWithRockFormations(relativeRockFormations)
    cave.println()

    // Iterate each piece of sand until it rests,
    // Finally stopping just before the first piece that falls outside of the min/max grid.
    val sandCount = cave.fillWithSand(relativeSandStart)
    cave.println()
    return sandCount
}

fun part2(input: List<String>): Int {
    val rockFormations = parseRockFormations(input)
    val floorRow = rockFormations.flatten().maxOf { it.row } + 2
    val cave = Cave(floorRow = floorRow)
    cave.fillWithRockFormations(rockFormations)
    cave.println()
    val sandCount = cave.fillWithSand(sandStartPosition)
    cave.println()
    return sandCount
}

typealias RockFormation = List<Position>

private object RockFormationParser {
    private val regex by lazy { Regex("""(\d+),(\d+)""") }

    fun parseRockFormations(input: List<String>): List<RockFormation> = input.map { line ->
        regex.findAll(line)
            .map { match -> match.groupValues.drop(1).map { capture -> capture.toInt() } }
            .map { (x, y) -> Position(row = y, col = x) }
            .toList()
    }
}

private fun Cave.fillWithRockFormations(rockFormations: List<RockFormation>) {
    rockFormations.forEach { rockFormation ->
        rockFormation
            .windowed(size = 2, step = 1)
            .forEach { (relativeStart, relativeEnd) ->
                (relativeStart lineTo relativeEnd).forEach { rockPosition ->
                    fillPosition(rockPosition, Rock)
                }
            }
    }
}

private fun Cave.fillWithSand(relativeSandStart: Position) =
    generateSequence {
        produceSand(relativeSandStart).takeIf { sandResult -> sandResult is Rest }
    }.count()

val sandStartPosition = Position(row = 0, col = 500)

private class Cave(
    private val numRows: Int? = null,
    private val numCols: Int? = null,
    private val floorRow: Int? = null
) {
    companion object {
        private val flowDirections = listOf(
            DeltaPosition(deltaRow = 1, deltaCol = 0),
            DeltaPosition(deltaRow = 1, deltaCol = -1),
            DeltaPosition(deltaRow = 1, deltaCol = 1)
        )
    }

    private val Position.isInBounds
        get() = numRows?.let { row in 0 until numRows } != false
            && numCols?.let { col in 0 until numCols } != false

    enum class CaveFilling { Rock, Sand, Floor }

    private val caveFillings = CaveFillings(floorRow = floorRow)

    private val Position.isFilled get() = caveFillings[this] != null

    fun fillPosition(position: Position, caveFilling: CaveFilling) {
        caveFillings.fillPosition(position, caveFilling)
    }

    sealed class SandResult {
        data class Rest(val restPosition: Position) : SandResult()
        data object Abyss : SandResult()
        data object Blocked : SandResult()
    }

    fun produceSand(sandSource: Position) =
        if (sandSource.isFilled) Blocked
        else sandPositions(sandSource).last().let { lastSandPosition ->
            if (lastSandPosition.isInBounds) {
                fillPosition(lastSandPosition, Sand)
                Rest(lastSandPosition)
            } else Abyss
        }

    private fun sandPositions(sandSource: Position) =
        generateSequence(sandSource) { sandPosition ->
            if (!sandPosition.isInBounds) null
            else flowDirections.firstNotNullOfOrNull { delta ->
                (sandPosition + delta).takeIf { nextPosition ->
                    !nextPosition.isInBounds || !nextPosition.isFilled
                }
            }
        }

    override fun toString() =
        rowIndices.joinToString(separator = "\n") { row ->
            String(
                colIndices
                    .map { col -> caveFillings[Position(row = row, col = col)].char }
                    .toCharArray()
            )
        }

    private val rowIndices get() = minRowIndex..maxRowIndex

    private val minRowIndex get() = numRows?.let { 0 } ?: caveFillings.minRow

    private val maxRowIndex
        get() = (numRows?.let { it - 1 } ?: caveFillings.maxRow)
            .let {
                if (floorRow == null) it
                else it.coerceAtLeast(floorRow)
            }

    private val colIndices
        get() = numCols?.let { 0 until numCols }
            ?: caveFillings.minCol..caveFillings.maxCol

    private val CaveFilling?.char
        get() = when (this) {
            Rock -> '#'
            Sand -> 'O'
            Floor -> '_'
            null -> '.'
        }
}

private class CaveFillings(private val floorRow: Int? = null) {
    private val fillingsMap = mutableMapOf<Position, Cave.CaveFilling>()

    val minRow get() = fillingsMap.minOf { it.key.row }
    val maxRow: Int get() = fillingsMap.maxOf { it.key.row }
    val minCol get() = fillingsMap.minOf { it.key.col }
    val maxCol get() = fillingsMap.maxOf { it.key.col }

    fun fillPosition(position: Position, caveFilling: Cave.CaveFilling) {
        fillingsMap[position] = caveFilling
    }

    operator fun get(position: Position) =
        if (position.row == floorRow) Floor
        else fillingsMap[position]
}

private infix fun Position.lineTo(to: Position) = when {
    col == to.col ->
        (min(row, to.row)..max(row, to.row))
            .map { row -> Position(row = row, col = col) }

    row == to.row -> (min(col, to.col)..max(col, to.col))
        .map { col -> Position(row = row, col = col) }

    else -> throw IllegalArgumentException("Neither row nor column values match (from: ${this}, to: $to)")
}
