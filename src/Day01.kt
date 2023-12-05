import kotlin.time.measureTime
import kotlin.time.measureTimedValue

fun main() {
    println(
        Regex("ab|ba").findAllWithOverlap("aba")
            .toList().joinToString {
                with(it) { "$value at ${range.first}" }
            }
    )

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)
    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    println("Part 1: ${part1(input)}")
    measurePerformance("Part2", 100) { part2(input) }
    measurePerformance("Part2 with Sequences", 100) { part2Sequence(input) }
}

inline fun <T> measurePerformance(label: String = "", reps: Int, function: () -> T) {
    measureTime { repeat(reps) { function() } }.let { totalDuration ->
        println("$label Computed ${function()} in average of ${(totalDuration / reps).inWholeMicroseconds} microseconds")
    }
}

fun part1(input: List<String>): Int = input.sumOf { line ->
    line.filter { char -> char.isDigit() }.let { digits ->
        "${digits.first()}${digits.last()}".toInt()
    }
}

fun part2(input: List<String>) = input.sumOf { line ->
    line.digitLocations().run { firstDigitValue() * 10 + lastDigitValue() }
}

fun part2Sequence(input: List<String>): Int {
    return input.sumOf { line ->
        val allDigitMatches = anyDigitRegex.findAllWithOverlap(line)
        /*
        val digitMatchesString = allDigitMatches.joinToString(prefix = "\n\t", separator = "\n\t") {
            with(it) { "$value at ${range.first}" }
        }
        println("In $line we found: $digitMatchesString")
        */
        allDigitMatches.run { first().value.digitValue() * 10 + last().value.digitValue() }
    }
}

fun String.digitValue(): Int {
    return when {
        length == 1 && first().isDigit() -> first().digitToInt()
        // TODO: Optimize using a map of digit words
        else -> digitWords.first { it.word == this }.digitValue
    }
}

fun Regex.findAllWithOverlap(input: String) = generateSequence(find(input)) { find(input, it.range.first + 1) }

fun String.digitLocations() = digitWordLocations() + rawDigitLocations()

fun String.digitWordLocations() = digitWords.flatMap { digitWord ->
    digitWord.regex.findAll(this).map { match ->
        DigitLocation(digitValue = digitWord.digitValue, location = match.range.first)
    }
}

fun String.rawDigitLocations() = rawDigitsRegex.findAll(this).map { match ->
    DigitLocation(digitValue = match.value.toInt(), location = match.range.first)
}

data class DigitWord(
    val word: String,
    val digitValue: Int
) {
    val regex = Regex(word)
}

val digitWords = listOf("one", "two", "three", "four", "five", "six", "seven", "eight", "nine")
    .mapIndexed { i, w -> DigitWord(word = w, digitValue = i + 1) }

val rawDigitsRegex = Regex(digitWords.map { it.digitValue }.joinToString(separator = "|"))

val anyDigitRegex = Regex(digitWords.joinToString(separator = "|") { "${it.word}|${it.digitValue}" })

data class DigitLocation(
    val digitValue: Int,
    val location: Int
)

fun List<DigitLocation>.firstDigitValue() = minByOrNull { it.location }!!.digitValue

fun List<DigitLocation>.lastDigitValue() = maxByOrNull { it.location }!!.digitValue
