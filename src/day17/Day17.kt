package day17

import Edge
import leastPathCost
import println
import readInput

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day17/Day17_test")

    // Check that given solution actually has a heat-loss cost of 102
    val testSolution = readInput("day17/Day17_solution")
    check(
        heatLoss(
            solution = testSolution,
            input = testInput
        ).also { it.println() } == 102
    )

    check(part1(testInput).also { it.println() } == 102)
    check(part2(testInput).also { it.println() } == 94)

    val input = readInput("day17/Day17")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun heatLoss(solution: List<String>, input: List<String>): Int {
    val pathCharacters = setOf('>', '<', '^', 'v')
    return solution.withIndex().sumOf { (lineIndex, solutionLine) ->
        solutionLine.withIndex().sumOf { (column, solutionCharacter) ->
            when (solutionCharacter) {
                in pathCharacters -> input[lineIndex][column].digitToInt()
                else -> 0
            }
        }
    }
}

fun part1(input: List<String>): Int {
    val maxStraightMoves = 3
    val heatLoss = input.map { line ->
        line.map { char -> char.digitToInt() }
    }
    val endPosition = Position(row = heatLoss.lastIndex, col = heatLoss.last().lastIndex)
    fun Position.isValid() = row in heatLoss.indices && col in heatLoss[row].indices
    return leastPathCost(
        start = Node(
            position = Position(0, 0),
            direction = null,
            straightMoveCount = 0
        ),
        edges = { node ->
            Direction.entries
                .filter { it != node.direction?.opposite }
                .mapNotNull { direction ->
                    (node move direction)
                        .takeIf { it.position.isValid() && it.straightMoveCount <= maxStraightMoves }
                        ?.let { destination ->
                            Edge(
                                destination = destination,
                                cost = heatLoss[destination.position.row][destination.position.col]
                            )
                        }
                }
        },
        isEnd = { node -> node.position == endPosition }
    )
}

fun part2(input: List<String>): Int {
    val minStraightMoves = 4
    val maxStraightMoves = 10
    val heatLoss = input.map { line ->
        line.map { char -> char.digitToInt() }
    }
    val endPosition = Position(row = heatLoss.lastIndex, col = heatLoss.last().lastIndex)
    fun Position.isValid() = row in heatLoss.indices && col in heatLoss[row].indices
    return leastPathCost(
        start = Node(
            position = Position(0, 0),
            direction = null,
            straightMoveCount = 0
        ),
        edges = { t ->
            when (t.straightMoveCount) {
                // If moved 0, any direction but backwards (that's valid on the map)
                0 -> Direction.entries
                // If moved 1 through 3, only straight (which maybe you can't even do if it's off the map)
                in 0..<minStraightMoves -> listOf(t.direction!!)
                // If moved 4 through 9, any direction on the map except backwards
                in minStraightMoves..<maxStraightMoves -> Direction.entries.filter { it != t.direction?.opposite }
                // If moved 10, must turn left or right
                else -> Direction.entries.filter { it != t.direction && it != t.direction?.opposite }
            }.mapNotNull { direction ->
                (t move direction)
                    .takeIf { it.position.isValid() }
                    ?.let { destination ->
                        Edge(
                            destination = destination,
                            cost = heatLoss[destination.position.row][destination.position.col]
                        )
                    }
            }
        },
        isEnd = { t -> t.position == endPosition && t.straightMoveCount >= minStraightMoves }
    )
}

data class Node(
    val position: Position,
    val direction: Direction?,
    val straightMoveCount: Int
)

infix fun Node.move(moveDirection: Direction) = Node(
    position = position move moveDirection,
    direction = moveDirection,
    straightMoveCount = if (moveDirection == direction) straightMoveCount + 1 else 1
)

data class Position(val row: Int, val col: Int)

enum class Direction(val rowDelta: Int, val colDelta: Int) {
    North(rowDelta = -1, colDelta = 0),
    South(rowDelta = 1, colDelta = 0),
    East(rowDelta = 0, colDelta = 1),
    West(rowDelta = 0, colDelta = -1);
}

val Direction.opposite
    get() = when (this) {
        Direction.North -> Direction.South
        Direction.South -> Direction.North
        Direction.East -> Direction.West
        Direction.West -> Direction.East
    }

infix fun Position.move(direction: Direction) = Position(
    row = row + direction.rowDelta,
    col = col + direction.colDelta
)
