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

fun Int.toIntRange() = this .. this

inline fun <T> Iterable<T>.split(isDelimiter: (T) -> Boolean): List<List<T>> =
    fold(mutableListOf(mutableListOf<T>())) { acc, t ->
        if (isDelimiter(t)) acc.add(mutableListOf())
        else acc.last().add(t)
        acc
    }.filter { it.isNotEmpty() }