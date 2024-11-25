package aoc2022.day12

data class HeightMapEntry(val type: Type, val height: Int) {
    enum class Type {
        Normal,
        Start,
        End
    }
}