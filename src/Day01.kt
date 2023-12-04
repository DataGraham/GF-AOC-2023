import kotlin.time.measureTimedValue

fun main() {
    fun part1(input: List<String>): Int = input.sumOf { line ->
        line.filter { char -> char.isDigit() }.let { digits ->
            "${digits.first()}${digits.last()}".toInt()
        }
    }

    fun part2(input: List<String>) = input.sumOf { line ->
        line.digitLocations().run { firstDigitValue() * 10 + lastDigitValue() }
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)
    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    measureTimedValue { part2(input) }.let {
        println("Computed ${it.value} in ${it.duration}")
    }
}

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

val digitWords = listOf(
    DigitWord("one", 1),
    DigitWord("two", 2),
    DigitWord("three", 3),
    DigitWord("four", 4),
    DigitWord("five", 5),
    DigitWord("six", 6),
    DigitWord("seven", 7),
    DigitWord("eight", 8),
    DigitWord("nine", 9)
)

val rawDigitsRegex = Regex(digitWords.map { it.digitValue }.joinToString(separator = "|"))

data class DigitLocation(
    val digitValue: Int,
    val location: Int
)

fun List<DigitLocation>.firstDigitValue() = minByOrNull { it.location }!!.digitValue

fun List<DigitLocation>.lastDigitValue() = maxByOrNull { it.location }!!.digitValue
