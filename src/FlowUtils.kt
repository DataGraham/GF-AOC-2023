import kotlinx.coroutines.flow.*
import kotlin.math.max
import kotlin.math.min

suspend fun <T> Flow<T>.all(predicate: (T) -> Boolean) =
    map { predicate(it) }.firstOrNull { !it } == null

suspend fun Flow<Int>.sum() = reduce { total, value -> total + value }

suspend fun Flow<Int>.min() = reduce { minimum, value -> min(minimum, value) }

suspend fun Flow<Int>.max() = reduce { maximum, value -> max(maximum, value) }

fun <T> Flow<T>.takeUntilInclusive(isLast: (T) -> Boolean) = transformWhile {
    emit(it)
    !isLast(it)
}
