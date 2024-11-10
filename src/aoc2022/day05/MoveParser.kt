package aoc2022.day05

object MoveParser {
    fun parseMoves(input: List<String>) = input.mapNotNull { line -> parseMove(line) }

    private fun parseMove(line: String) =
        moveRegex.find(line)
            ?.groupValues
            ?.drop(1) // We don't want the entire match, only the capture groups
            ?.map { it.toInt() }
            ?.let {
                Move(
                    count = it[0],
                    // In the inpt text, stacks are numbered starting at 1,
                    // so we subtract 1 to convert to indexes.
                    sourceIndex = it[1] - 1,
                    destinationIndex = it[2] - 1
                )
            }

    private val moveRegex by lazy { Regex("""^move (\d+) from (\d+) to (\d+)$""") }
}