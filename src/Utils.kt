import java.math.BigInteger
import java.security.MessageDigest
import kotlin.io.path.Path
import kotlin.io.path.readLines
import kotlin.time.measureTime

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("src/$name.txt").readLines()

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)

inline fun <T> measurePerformance(label: String = "", reps: Int, function: () -> T) {
    val totalDuration = measureTime { repeat(reps) { function() } }
    println("$label Computed ${function()} in average of ${(totalDuration / reps).inWholeMicroseconds} microseconds")
}

fun String.requireSubstringAfter(delimiter: Char) = substring(startIndex = indexOf(delimiter).takeIf { it != -1 }!! + 1)

fun Int.toIntRange() = this..this

inline fun <T> Iterable<T>.split(isDelimiter: (T) -> Boolean): List<List<T>> =
    fold(mutableListOf(mutableListOf<T>())) { acc, t ->
        if (isDelimiter(t)) acc.add(mutableListOf())
        else acc.last().add(t)
        acc
    }.filter { it.isNotEmpty() }

fun <T> Collection<T>.toInfiniteSequence() = generateSequence(this) { it }.flatten()

fun IntRange.dropLast(n: Int) = start..last - n

fun List<String>.printLines() = joinToString(separator = "\n").println()

fun List<String>.transposed() = first().indices.map { col ->
    map { line -> line[col] }.joinToString(separator = "")
}

data class CycleInfo<T>(
    val distanceToStart: Int,
    val startValue: T,
    val length: Int
)

/** See: [YouTube](https://www.youtube.com/watch?v=PvrxZaH_eZ4) */
fun <T> findCycle(initial: T, next: T.() -> T): CycleInfo<T> {
    var slow = initial
    var fast = initial
    do {
        slow = slow.next()
        fast = fast.next().next()
    } while (slow != fast)

    slow = initial
    var distanceToStart = 0
    while (slow != fast) {
        slow = slow.next()
        fast = fast.next()
        ++distanceToStart
    }
    val startValue = slow

    var lengthFinder = startValue
    var length = 0
    do {
        lengthFinder = lengthFinder.next()
        ++length
    } while (lengthFinder != startValue)

    return CycleInfo(
        distanceToStart = distanceToStart,
        startValue = startValue,
        length = length
    )
}