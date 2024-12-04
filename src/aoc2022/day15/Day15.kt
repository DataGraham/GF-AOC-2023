package aoc2022.day15

import Position
import RegexParser
import manhattanDistanceTo
import parseLines
import println
import readInput
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

fun impossibleBeaconCount(input: List<String>, row: Int): Int {
    val sensorReadings = sensorReadingParser.parseLines(input)
    val impossibleBeaconColumns = sensorReadings
        .map { sensorReading -> sensorReading.beaconColumnsWithinRange(row = row) }
        .reduce { totalSet, set -> totalSet + set } -
        sensorReadings.beaconColumnsInRow(targetRow = row)
    return impossibleBeaconColumns.size
}

fun SensorReading.beaconColumnsWithinRange(row: Int): Set<Int> {
    val delta = (sensorPosition manhattanDistanceTo closestBeacon) - abs(row - sensorPosition.row)
    return if (delta < 0) emptySet()
    else ((sensorPosition.col - delta)..(sensorPosition.col + delta)).toSet()
}

private fun List<SensorReading>.beaconColumnsInRow(targetRow: Int) =
    mapNotNull { sensorReading ->
        sensorReading.closestBeacon.run {
            col.takeIf { row == targetRow }
        }
    }.toSet()