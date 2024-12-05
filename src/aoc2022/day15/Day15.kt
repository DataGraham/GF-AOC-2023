package aoc2022.day15

import Position
import RegexParser
import com.github.jonpeterson.kotlin.ranges.IntRangeSet
import manhattanDistanceTo
import parseLines
import println
import readInput
import size
import toIntRange
import kotlin.math.abs

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day15/Day15_test")
    val input = readInput("aoc2022/day15/Day15")

    check(part1(testInput, row = 10).also { it.println() } == 26)
    println("Part 1 Answer: ${part1(input, row = 2000000)}")

    //check(part2(testInput).also { it.println() } == 93)
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>, row: Int): Int {
    return impossibleBeaconCount(input, row = row)
}

// TODO: Will need to optimize for part 2
//  Using https://github.com/jonpeterson/kotlin-range-sets perhaps?!
//  Although it's not optimized with a binary or interpolation search for the relevant overlapping range.
//  Keep in mind that beacon positions are always within the ruled-out range
//  and don't need to be ruled-in, as we're explicitly looking for an undetected beacon!
fun part2(input: List<String>) = input.size

data class SensorReading(
    val sensorPosition: Position,
    val closestBeacon: Position
)

val sensorReadingParser = """(-?\d+)""".let { captureInteger ->
    RegexParser("""Sensor at x=$captureInteger, y=$captureInteger: closest beacon is at x=$captureInteger, y=$captureInteger""") { captures ->
        val (sensorX, sensorY, beaconX, beaconY) = captures.map { it.toInt() }
        SensorReading(
            sensorPosition = Position(row = sensorY, col = sensorX),
            closestBeacon = Position(row = beaconY, col = beaconX)
        )
    }
}

fun impossibleBeaconCount(input: List<String>, row: Int) =
    sensorReadingParser.parseLines(input).let { sensorReadings ->
        sensorReadings
            .map { sensorReading -> sensorReading.beaconColumnsWithinRange(row = row) }
            .let { ranges -> IntRangeSet(ranges) }
            .apply {
                sensorReadings.beaconColumnsInRow(targetRow = row)
                    .forEach { beaconColumn -> remove(beaconColumn.toIntRange()) }
            }
            .sumOf { range -> range.size }
    }

fun SensorReading.beaconColumnsWithinRange(row: Int): IntRange {
    val delta = (sensorPosition manhattanDistanceTo closestBeacon) - abs(row - sensorPosition.row)
    return if (delta < 0) IntRange.EMPTY
    else ((sensorPosition.col - delta)..(sensorPosition.col + delta))
}

private fun List<SensorReading>.beaconColumnsInRow(targetRow: Int) =
    mapNotNull { sensorReading ->
        sensorReading.closestBeacon.run {
            col.takeIf { row == targetRow }
        }
    }.toSet()