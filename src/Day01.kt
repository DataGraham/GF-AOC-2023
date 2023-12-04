fun main() {
    fun part1(input: List<String>): Int = input.sumOf { line ->
        line.filter { char -> char.isDigit() }.let { digits ->
            "${digits.first()}${digits.last()}".toInt()
        }
    }

    fun part2(input: List<String>): Int {
        val replacements = mapOf(
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
        return part1(
            input.map { line ->
                line.replace(Regex(replacements.keys.joinToString(separator = "|"))) { match ->
                    replacements[match.value]!!
                }
            }
        )
    }

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("Day01_test")
    check(part1(testInput) == 142)
    val testInput2 = readInput("Day01_test2")
    check(part2(testInput2) == 281)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
