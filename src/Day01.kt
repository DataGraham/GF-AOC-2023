import kotlin.time.measureTimedValue

val digitsByWord = mapOf(
    "one" to "1",
    "two" to "2",
    "three" to "3",
    "four" to "4",
    "five" to "5",
    "six" to "6",
    "seven" to "7",
    "eight" to "8",
    "nine" to "9"
)

fun main() {
    fun part1(input: List<String>): Int = input.sumOf { line ->
        line.filter { char -> char.isDigit() }.let { digits ->
            "${digits.first()}${digits.last()}".toInt()
        }
    }

    data class DigitLocation(
        val digitValue: Int,
        val location: Int
    )

    fun String.digitWordLocations() = digitsByWord.keys.flatMap { numberWord ->
        Regex(numberWord).findAll(this).map { match ->
            DigitLocation(
                digitValue = digitsByWord[numberWord]!!.toInt(),
                location = match.range.first
            )
        }
    }

    fun String.rawDigitLocations() =
        Regex(digitsByWord.values.joinToString(separator = "|")).findAll(this).map { match ->
            DigitLocation(
                digitValue = match.value.toInt(),
                location = match.range.first
            )
        }

    fun part2(input: List<String>) = input.sumOf { line ->
        (line.digitWordLocations() + line.rawDigitLocations()).let { allDigitLocations ->
            allDigitLocations.minByOrNull { it.location }!!.digitValue * 10 +
                allDigitLocations.maxByOrNull { it.location }!!.digitValue
        }
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
