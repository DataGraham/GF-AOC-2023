package aoc2022.day06

class CharacterFrequencyTracker {
    private val charFreq = HashMap<Char, Int>()

    fun add(charToAdd: Char) {
        charFreq[charToAdd] = charFreq.getOrDefault(charToAdd, 0) + 1
    }

    fun remove(charToRemove: Char) {
        val newFrequency = charFreq[charToRemove]!! - 1
        if (newFrequency > 0) charFreq[charToRemove] = newFrequency
        else charFreq.remove(charToRemove)
    }

    val uniqueCharacterCount get() = charFreq.size
}