package aoc2024.day13

import DeltaPosition
import Position
import RegexParser
import minus
import println
import readInput
import times

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day13/Day13_test")
    check(part1(testInput).also { it.println() } == 480)
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("aoc2024/day13/Day13")
    println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val machines = input.chunked(4).map { (aButtonLine, bButtonLine, prizeLine) ->
        MachineInfo(
            buttonA = buttonParser.parse(aButtonLine),
            buttonB = buttonParser.parse(bButtonLine),
            prize = prizeParser.parse(prizeLine)
        )
    }

    val costs = machines.map { machine ->
        (100 downTo 0).firstNotNullOfOrNull { pressB ->
            // If we press B this many times (as many as possible)
            val bDelta = machine.buttonB.movement * pressB
            // How far we would we have left to go to the prize?
            val remainingDelta = (machine.prize.position - bDelta) - Position.ORIGIN
            // How many presses of button A would that be (if possible)?
            val pressA =
                if (remainingDelta.deltaRow % machine.buttonA.movement.deltaRow != 0
                    || remainingDelta.deltaCol % machine.buttonA.movement.deltaCol != 0
                ) null
                else (remainingDelta.deltaRow / machine.buttonA.movement.deltaRow)
                    .takeIf { machine.buttonA.movement.deltaCol * it == remainingDelta.deltaCol }
            // What would then be the cost of winning the prize by pressing A and B this many times?
            if (pressA != null) pressA * A_BUTTON_COST + pressB * B_BUTTON_COST
            else null
        }
    }
    return costs.sumOf { it ?: 0 }
}

fun part2(input: List<String>): Int {
    return input.size
}

private const val A_BUTTON_COST = 3
private const val B_BUTTON_COST = 1

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