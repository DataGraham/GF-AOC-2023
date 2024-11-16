import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.reduce
import kotlin.math.max
import kotlin.math.min

suspend fun <T> Flow<T>.all(predicate: (T) -> Boolean) =
    map { predicate(it) }.firstOrNull { !it } == null

suspend fun Flow<Int>.sum() = reduce { total, value -> total + value }

suspend fun Flow<Int>.min() = reduce { minimum, value -> min(minimum, value) }

suspend fun Flow<Int>.max() = reduce { maximum, value -> max(maximum, value) }

