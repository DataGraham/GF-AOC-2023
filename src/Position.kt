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

fun <T> List<List<T>>.positionOf(t: T) = positionOf { it == t }

fun <T> List<List<T>>.positionOf(predicate: (T) -> Boolean) =
    withIndex().firstNotNullOfOrNull { (row, rowValues) ->
        rowValues.indexOfFirst(predicate)
            .takeIf { index -> index != -1 }
            ?.let { col -> Position(row = row, col = col) }
    }

data class DeltaPosition(val deltaRow: Int, val deltaCol: Int)

operator fun Position.minus(otherPosition: Position) =
    DeltaPosition(deltaRow = row - otherPosition.row, deltaCol = col - otherPosition.col)

operator fun Position.plus(deltaPosition: DeltaPosition) = Position(
    row = row + deltaPosition.deltaRow,
    col = col + deltaPosition.deltaCol
)

val DeltaPosition.isAdjacent get() = max(abs(deltaRow), abs(deltaCol)) <= 1

fun DeltaPosition.unitized() = copy(
    deltaRow = deltaRow.unitized(),
    deltaCol = deltaCol.unitized()
)

enum class Direction(val rowDelta: Int, val colDelta: Int) {
    Up(rowDelta = -1, colDelta = 0),
    Down(rowDelta = 1, colDelta = 0),
    Right(rowDelta = 0, colDelta = 1),
    Left(rowDelta = 0, colDelta = -1);
}

infix fun Position.move(direction: Direction) = Position(
    row = row + direction.rowDelta,
    col = col + direction.colDelta
)