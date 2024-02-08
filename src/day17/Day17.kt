package day17

import println
import readInput
import java.util.PriorityQueue

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
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("day17/Day17")
    println("Part 1 Answer: ${part1(input)}")
    // println("Part 2 Answer: ${part2(input)}")
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
        edges = {
            Direction.entries
                .filter { it != direction?.opposite }
                .mapNotNull { direction ->
                    (this move direction)
                        .takeIf { it.position.isValid() && it.straightMoveCount <= MAX_STRAIGHT_MOVES }
                        ?.let { destination ->
                            Edge(
                                destination = destination,
                                cost = heatLoss[destination.position.row][destination.position.col]
                            )
                        }
                }
        },
        isEnd = { position == endPosition }
    )
}

fun part2(input: List<String>): Int {
    return input.size
}

/**
 * @param start Starting "node".
 * @param edges Returns list of edges leading from the given node.
 * @param isEnd Identifies whether a node qualifies as a desired destination. Need not identify a unique node.
 * @return Cost of lowest-cost path from start "node" to ANY end "node"
 * */
fun <T> leastPathCost(
    start: T,
    edges: T.() -> List<Edge<T>>,
    isEnd: T.() -> Boolean
): Int {
    val visitedNodes = mutableSetOf<T>()
    val leastKnownDistance = mutableMapOf(start to 0)
    val nodeBefore = mutableMapOf<T, T>()

    // "unvisited nodes" is not all theoretically possible nodes,
    //  but rather discovered, reachable "nodes TO-BE visited".
    val nodesToVisit = PriorityQueue<NodeToVisit<T>>(compareBy { it.distance })
    nodesToVisit += NodeToVisit(node = start, distance = 0)

    while (nodesToVisit.isNotEmpty()) {
        val nodeToVisit = nodesToVisit.remove()

        // No need to explore connections leading FROM an "end" node.
        if (nodeToVisit.node.isEnd()) continue

        nodeToVisit.node.edges()
            .filter { it.destination !in visitedNodes }
            .forEach { edge ->
                leastKnownDistance.compute(edge.destination) { adjacentNode: T, previousDistance: Int? ->
                    val newDistance = nodeToVisit.distance + edge.cost
                    // If no previous distance:
                    if (previousDistance == null) {
                        // Set this distance and insert into queue with this distance
                        nodesToVisit += NodeToVisit(adjacentNode, newDistance)
                        nodeBefore[adjacentNode] = nodeToVisit.node
                        newDistance
                    } else {
                        // If previous distance is less or equal:
                        if (previousDistance <= newDistance) {
                            // Do nothing
                            previousDistance
                        } else { // If previous distance is greater:
                            // Remove with old distance from priority queue and re-add with new, lower distance
                            nodesToVisit -= NodeToVisit(node = adjacentNode, distance = previousDistance)
                            nodesToVisit += NodeToVisit(node = adjacentNode, distance = newDistance)
                            nodeBefore[adjacentNode] = nodeToVisit.node
                            // Set distance
                            newDistance
                        }
                    }
                }
            }
        visitedNodes += nodeToVisit.node
    }

    // Return min cost among all "endish" nodes
    val endNodes = leastKnownDistance.filter { it.key.isEnd() }
    val leastCostEndNode = endNodes.minBy { it.value }
    val pathBack = generateSequence(leastCostEndNode.key) { nodeBefore[it] }.toList()
    pathBack.reversed().joinToString(separator = "\n").println()
    return leastCostEndNode.value
}

data class NodeToVisit<T>(
    val node: T,
    val distance: Int
)

data class Edge<T>(val destination: T, val cost: Int)

const val MAX_STRAIGHT_MOVES = 3

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