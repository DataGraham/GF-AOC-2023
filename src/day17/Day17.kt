package day17

import println
import readInput
import java.util.PriorityQueue

fun main() {
    // test if implementation meets criteria from the description, like:
    val testInput = readInput("day17/Day17_test")
    check(part1(testInput).also { it.println() } == 102)
    //check(part2(testInput).also { it.println() } == 1)

    val input = readInput("day17/Day17")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val heatLoss = input.map { line ->
        line.map { char -> char.digitToInt() }
    }
    val start = Node(
        position = Position(0, 0),
        direction = null,
        straightMoveCount = 0
    )

    return input.size
}

fun part2(input: List<String>): Int {
    return input.size
}

fun <T> leastPathCost(
    start: T,
    edges: T.() -> List<Edge<T>>,
    isEnd: T.() -> Boolean
): Int {
    val visitedNodes = mutableSetOf<T>()
    val leastKnownDistance = mutableMapOf(start to 0)

    // TODO: "unvisited nodes" is not all theoretically possible nodes,
    //  but rather discovered, reachable "nodes TO-BE visited".
    val nodesToVisit = PriorityQueue<NodeToVisit<T>>(compareBy { it.distance })
    nodesToVisit += NodeToVisit(node = start, distance = 0)

    while (nodesToVisit.isNotEmpty()) {
        val nodeToVisit = nodesToVisit.remove()
        nodeToVisit.node.edges()
            .filter { it.destination !in visitedNodes }
            .forEach { edge ->
                leastKnownDistance.compute(edge.destination) { adjacentNode: T, previousDistance: Int? ->
                    val newDistance = nodeToVisit.distance + edge.cost
                    // If no previous distance:
                    if (previousDistance == null) {
                        // Set this distance and insert into queue with this distance
                        nodesToVisit += NodeToVisit(adjacentNode, newDistance)
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
                            // Set distance
                            newDistance
                        }
                    }
                }
                visitedNodes += nodeToVisit.node
            }
    }
    // TODO: Return min cost among all "endish" nodes
    return leastKnownDistance.filter { it.key.isEnd() }.minOf { it.value }
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

infix fun Position.move(direction: Direction) = Position(
    row = row + direction.rowDelta,
    col = col + direction.colDelta
)