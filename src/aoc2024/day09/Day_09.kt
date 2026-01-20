package aoc2024.day09

import println
import readInput
import java.util.*

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2024/day09/Day09_test")
    check(part1(testInput).also { it.println() } == 1928L)
    check(part2(testInput).also { it.println() } == Unit)

    val input = readInput("aoc2024/day09/Day09")
    println("Part 1 Answer: ${part1(input)}")
    //println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>) =
    input
        .first()
        .toDiskMapCodes()
        .decodeDiskMap()
        .apply { compact(this) }
        .asIterable()
        .checksum()

fun part2(input: List<String>) =
    input
        .first()
        .toDiskMapCodes()
        .decodeEfficientDiskMap()
        .apply { compactWholeFiles(this) }
        .println()
//.checksum()

data class EfficientDiskMapEntry(
    /** Null for empty space **/
    val fileId: Int?,
    val size: Int
)

fun String.toDiskMapCodes() = map { it.digitToInt() }

fun List<Int>.decodeDiskMap() =
    flatMapIndexed { index, code ->
        // Start with a file of length <code> containing the file id (index)
        if (index % 2 == 0) file(id = index / 2, size = code)
        // Next is empty space of length <code>
        else emptySpace(size = code)
    }.toTypedArray()

/** TODO: De-dupe with the original version */
fun List<Int>.decodeEfficientDiskMap() =
    LinkedList(
        mapIndexed { index, code ->
            EfficientDiskMapEntry(
                size = code,
                // Start with a file of length <code> containing the file id (index of the file)
                // Next is empty space of length <code>
                fileId = if (index % 2 == 0) index / 2 else null
            )
        }
    )

private fun file(id: Int, size: Int) = List(size) { id }

private fun emptySpace(size: Int) = List(size) { null }

private fun compact(disk: Array<Int?>) {
    disk.indices.reversed().forEach { moveFrom ->
        if (disk[moveFrom] == null) return@forEach
        val moveTo = disk.indexOf(null)
        if (moveTo == -1 || moveTo > moveFrom) return
        disk[moveTo] = disk[moveFrom]
        disk[moveFrom] = null
        // TODO: We should start at moveTo+1 when looking for the next moveTo
    }
}

/** Avoid scanning the LinkedList too many times? */
private fun compactWholeFiles(disk: LinkedList<EfficientDiskMapEntry>) {
    // TODO: Concurrent modification!
    //  Would it make more sense to just NOT be "efficient"
    //  and just decode to one disk unit at a time
    //  so we only have to assign elements in the list
    //  and never insert or remove?!
    disk.descendingIterator().withIndex().forEach { (reversedEntryIndex, entryToMove) ->
        val entryIndex = disk.lastIndex - reversedEntryIndex
        if (entryToMove.fileId == null) return@forEach
        val moveTo = disk.indexOfFirst { entry ->
            entry.fileId == null && entry.size >= entryToMove.size
        }.takeIf { it != -1 && it < entryIndex } ?: return@forEach
        //  NOTE: We need the empty space to make the checksum correct for later files
        //  but since we can only move earlier files to earlier positions henceforth,
        //  it doesn't matter if it's not contiguous.
        disk[entryIndex] = entryToMove.copy(fileId = null)
        if (disk[moveTo].size == entryToMove.size) {
            // File fits exactly: replace empty space with file
            disk[moveTo] = entryToMove
        } else {
            // File fits with space to spare: insert file and reduce empty space
            disk[moveTo] = disk[moveTo].run { copy(size = size - entryToMove.size) }
            disk.add(index = moveTo, entryToMove)
        }
    }
}

private fun Iterable<Int?>.checksum() =
    asSequence().mapIndexed { index, i -> index * (i ?: 0).toLong() }.sum()
