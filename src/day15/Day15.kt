package day15

import println
import readInput
import java.lang.IllegalArgumentException

fun main() {
    // test if implementation meets criteria from the description, like:
    check(part1("HASH").also { it.println() } == 52)
    val testInput = readInput("day15/Day15_test").first()
    check(part1(testInput).also { it.println() } == 1320)
    check(part2(testInput).also { it.println() } == 145)

    val input = readInput("day15/Day15").first()
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: String): Int {
    return input.split(',').sumOf { it.holidayAsciiStringHelper() }
}

fun part2(input: String): Int {
    val boxes = List(9) { mutableListOf<Lens>() }
    val steps = input.split(',').map { Step.fromString(it) }
    steps.forEach { step ->
        val box = boxes[step.boxIndex]
        val indexOfMatchingLabel = box.indexOfFirst { lens -> lens.label == step.label }.takeIf { it != -1 }
        when (step) {
            is Step.Add ->
                if (indexOfMatchingLabel != null) box[indexOfMatchingLabel] = step.lens
                else box += step.lens
            is Step.Remove ->
                if (indexOfMatchingLabel != null) box.removeAt(indexOfMatchingLabel)
        }
    }
    return boxes.withIndex().sumOf { (boxIndex, lenses) ->
        lenses.withIndex().sumOf { (lensIndex, lens) ->
            (boxIndex + 1) * (lensIndex + 1) * lens.focalLength
        }
    }
}

private fun String.holidayAsciiStringHelper() = fold(0) { hash, c -> ((hash + c.code) * 17) % 256 }

data class Lens(
    val label: String,
    val focalLength: Int
)

sealed class Step {
    abstract val boxIndex: Int
    abstract val label: String

    data class Add(
        override val boxIndex: Int,
        val lens: Lens,
    ) : Step() {
        override val label: String get() = lens.label
    }

    data class Remove(
        override val boxIndex: Int,
        override val label: String
    ) : Step()

    companion object {
        fun fromString(string: String): Step {
            val (label, focalLengthString) = string.split('=', '-')
            val stepCharacter = string[label.length]
            val boxIndex = label.holidayAsciiStringHelper()
            return when (stepCharacter) {
                '=' -> Add(
                    boxIndex = boxIndex,
                    lens = Lens(
                        label = label,
                        focalLength = focalLengthString.toInt()
                    )
                )

                '-' -> Remove(
                    boxIndex = boxIndex,
                    label = label
                )

                else -> throw IllegalArgumentException()
            }
        }
    }
}
