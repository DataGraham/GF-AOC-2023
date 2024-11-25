package aoc2022.day12

object HeightMapParser {
    private val characterRange = 'a'..'z'

    fun parseHeightMap(input: List<String>) =
        input.map { line ->
            line.map { character ->
                parseHeightMapEntry(character)
            }
        }

    private fun parseHeightMapEntry(character: Char) = when (character) {
        'S' -> HeightMapEntry(HeightMapEntry.Type.Start, characterHeight(characterRange.first))
        'E' -> HeightMapEntry(HeightMapEntry.Type.End, characterHeight(characterRange.last))
        else -> HeightMapEntry(HeightMapEntry.Type.Normal, characterHeight(character))
    }

    private fun characterHeight(character: Char): Int = when (character) {
        in characterRange -> character - characterRange.first
        else -> throw IllegalArgumentException("Invalid height map character '$character'")
    }
}