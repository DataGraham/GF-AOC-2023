package aoc2024.day13

import DeltaPosition
import Position
import RegexParser
import println
import readInput
import java.util.regex.Pattern

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day13/Day13_test")
    check(part1(testInput).also { it.println() } == 480)
    //check(part2(testInput).also { it.println() } == 1)

    //    val input = readInput("aoc2024/day13/Day13")
    //    println("Part 1 Answer: ${part1(input)}")
    //    println("Part 2 Answer: ${part2(input)}")
}



fun part1(input: List<String>): Int {
    val machines = input.chunked(4).map { (aButtonLine, bButtonLine, prizeLine) ->
        MachineInfo(
            buttonA = buttonParser.parse(aButtonLine),
            buttonB = buttonParser.parse(bButtonLine),
            prize = prizeParser.parse(prizeLine)
        )
    }

    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}


private data class ButtonInfo(
    val movement: DeltaPosition
)

private data class PrizeInfo(
    val position: Position
)

private data class MachineInfo(
    val buttonA: ButtonInfo,
    val buttonB: ButtonInfo,
    val prize: PrizeInfo
)

private val buttonParser by lazy {
    //language=regexp
    RegexParser("""Button [AB]: X\+(\d+), Y\+(\d+)""") { (deltaX, deltaY) ->
        ButtonInfo(
            movement = DeltaPosition(
                deltaRow = deltaY.toInt(),
                deltaCol = deltaX.toInt()
            )
        )
    }
}

private val prizeParser by lazy {
    //language=regexp
    RegexParser("""Prize: X=(\d+), Y=(\d+)""") { (x, y) ->
        PrizeInfo(
            position = Position(
                row = y.toInt(),
                col = x.toInt()
            )
        )
    }
}