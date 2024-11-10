package aoc2022.day05

data class Move(val sourceIndex: Int, val destinationIndex: Int, val count: Int) {
    override fun toString() = "Move $count from stack[$sourceIndex] to stack[$destinationIndex]"
}