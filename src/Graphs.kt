import java.util.*

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
    // val pathBack = generateSequence(leastCostEndNode.key) { nodeBefore[it] }.toList()
    // pathBack.reversed().joinToString(separator = "\n").println()
    return leastCostEndNode.value
}

private data class NodeToVisit<T>(
    val node: T,
    val distance: Int
)

data class Edge<T>(val destination: T, val cost: Int)
