import kotlin.math.abs
import kotlin.math.max

// TODO: Use this extracted version everywhere?
data class Position(val row: Int, val col: Int) {
    companion object {
        val ORIGIN = Position(row = 0, col = 0)
    }
}

operator fun <T> List<List<T>>.get(position: Position) =
    this[position.row][position.col]

fun <T> List<List<T>>.isPositionValid(position: Position) =
    with(position) { row in indices && col in this@isPositionValid[row].indices }

fun <T> List<List<T>>.allPositions() = flatMapIndexed { row, rowValues ->
    rowValues.indices.map { col -> Position(row = row, col = col) }
}

fun <T> List<List<T>>.positionOf(t: T) = positionOf { it == t }

fun <T> List<List<T>>.positionOf(predicate: (T) -> Boolean) =
    withIndex().firstNotNullOfOrNull { (row, rowValues) ->
        rowValues.indexOfFirst(predicate)
            .takeIf { index -> index != -1 }
            ?.let { col -> Position(row = row, col = col) }
    }

/** TODO: Could Position and Delta both be/have an (x, y) vector
but just with different names,
so the math doesn't need to be repeated? */
data class DeltaPosition(val deltaRow: Int, val deltaCol: Int)

fun DeltaPosition.toPosition() = Position(row = deltaRow, col = deltaCol)

operator fun Position.minus(otherPosition: Position) =
    DeltaPosition(deltaRow = row - otherPosition.row, deltaCol = col - otherPosition.col)

infix fun Position.relativeTo(other: Position) = (this - other).toPosition()

operator fun Position.plus(deltaPosition: DeltaPosition) = Position(
    row = row + deltaPosition.deltaRow,
    col = col + deltaPosition.deltaCol
)

infix fun Position.manhattanDistanceTo(other: Position) =
    abs(row - other.row) + abs(col - other.col)

val DeltaPosition.isAdjacent get() = max(abs(deltaRow), abs(deltaCol)) <= 1

fun DeltaPosition.unitized() = copy(
    deltaRow = deltaRow.unitized(),
    deltaCol = deltaCol.unitized()
)

// TODO: Could we add directions? (e.g. down + left)
sealed class Direction(val deltaPosition: DeltaPosition) {

    constructor(deltaRow: Int, deltaCol: Int)
        : this(DeltaPosition(deltaRow = deltaRow, deltaCol = deltaCol))

    // TODO: Could we alias with cardinal directions too?
    data object Up : Direction(deltaRow = -1, deltaCol = 0)
    data object Down : Direction(deltaRow = 1, deltaCol = 0)
    data object Right : Direction(deltaRow = 0, deltaCol = 1)
    data object Left : Direction(deltaRow = 0, deltaCol = -1)

    data object UpRight : Direction(deltaRow = -1, deltaCol = 1)
    data object UpLeft : Direction(deltaRow = -1, deltaCol = -1)
    data object DownRight : Direction(deltaRow = 1, deltaCol = 1)
    data object DownLeft : Direction(deltaRow = 1, deltaCol = -1)

    companion object {
        val orthogonal = listOf(Up, Down, Right, Left)
        val diagonal = listOf(UpRight, UpLeft, DownRight, DownLeft)
        val all = orthogonal + diagonal
    }
}

infix fun Position.move(direction: Direction) =
    this + direction.deltaPosition

fun Position.path(direction: Direction) =
    path(deltaPosition = direction.deltaPosition)

fun Position.path(deltaPosition: DeltaPosition) =
    generateSequence(this) { position -> position + deltaPosition }