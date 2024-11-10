package aoc2022.day05

import removeFirst

class Crates(initialStacks: Collection<Collection<Char>>) {
    private val stacks = initialStacks.map { it.toMutableList() }

    fun perform(move: Move) = with(move) {
        stacks[destinationIndex].addAll(0, stacks[sourceIndex].removeFirst(count))
    }

    val stackTops get() = String(stacks.map { stack -> stack.first() }.toCharArray())
}
