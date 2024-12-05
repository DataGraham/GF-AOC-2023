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

    check(part2(testInput, maxCoordinate = 20, multiplier = 4000000).also { it.println() } == 56000011L)
    println("Part 2 Answer: ${part2(input, maxCoordinate = 4000000, multiplier = 4000000)}")
}

fun part1(input: List<String>, row: Int) =
    impossibleBeaconCount(input, row = row)

fun part2(input: List<String>, maxCoordinate: Int, multiplier: Int) =
    sensorReadingParser
        .parseLines(input)
        //  Keep in mind that beacon positions are always within the ruled-out range
        //  and don't need to be ruled-in, as we're explicitly looking for an undetected beacon!
        .undetectedBeaconPosition(maxCoordinate = maxCoordinate)
        .tuningFrequency(multiplier = multiplier)

private fun List<SensorReading>.undetectedBeaconPosition(maxCoordinate: Int) =
    (0..maxCoordinate).let { positionRange ->
        positionRange.firstNotNullOfOrNull { row ->
            IntRangeSet(positionRange)
                .apply { removeAll(beaconColumnsWithinRange(row = row)) }
                .firstOrNull()?.start?.let { beaconColumn ->
                    Position(row = row, col = beaconColumn)
                }
        }!!
    }

private fun Position.tuningFrequency(multiplier: Int) = col.toLong() * multiplier + row.toLong()

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

private fun List<SensorReading>.beaconColumnsWithinRange(row: Int) =
    IntRangeSet(
        map { sensorReading ->
            sensorReading.beaconColumnsWithinRange(row = row)
        }
    )

private fun impossibleBeaconCount(input: List<String>, row: Int) =
    sensorReadingParser.parseLines(input).let { sensorReadings ->
        sensorReadings
            .beaconColumnsWithinRange(row = row)
            .apply {
                removeAll(
                    sensorReadings
                        .detectedBeaconColumnsInRow(targetRow = row)
                        .map { beaconColumn -> beaconColumn.toIntRange() }
                )
            }
            .sumOf { range -> range.size }
    }

fun SensorReading.beaconColumnsWithinRange(row: Int): IntRange {
    val delta = (sensorPosition manhattanDistanceTo closestBeacon) - abs(row - sensorPosition.row)
    return if (delta < 0) IntRange.EMPTY
    else ((sensorPosition.col - delta)..(sensorPosition.col + delta))
}

private fun List<SensorReading>.detectedBeaconColumnsInRow(targetRow: Int) =
    mapNotNull { sensorReading ->
        sensorReading.closestBeacon.run {
            col.takeIf { row == targetRow }
        }
    }.toSet()