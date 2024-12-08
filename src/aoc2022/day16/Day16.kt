package aoc2022.day16

import Edge
import RegexParser
import leastPathCost
import parseLines
import println
import readInput
import kotlin.math.max

fun main() {
    // test()

    // test if implementation meets criteria from the description, like:
    val testInput = readInput("aoc2022/day16/Day16_test")
    // val input = readInput("aoc2022/day16/Day16")

    check(part1Greedy(testInput).also { it.println() } == 1651)
    //println("Part 1 Answer: ${part1(input)}")

    //check(part2(testInput).also { it.println() } == 1)
    //println("Part 2 Answer: ${part2(input)}")
}

private fun test() {
    testCase(
        0,
        Valve("A", 0, emptyList())
    )
    testCase(
        28,
        Valve("A", 0, listOf("B")),
        Valve("B", 1, listOf("A"))
    )
}

private fun testCase(expected: Int, vararg valves: Valve) = check(
    valves
        .associateBy { it.name }
        .maximumReleasablePressure(
            startNode = Node(
                currentValveName = valves.first().name,
                openedValves = emptySet(),
                elapsedMinutes = 0,
                pressureReleased = 0
            )
        )
        .also { it.println() }
        == expected
)

fun part1(input: List<String>): Int {
    val valves = parseValves(input)
    val initialNode = Node(
        currentValveName = "AA",
        openedValves = emptySet(),
        elapsedMinutes = 0,
        pressureReleased = 0
    )
    // TODO: What if we ran this on the simplified graph of direct, weighted edges between non-zero-flow valves?
    return valves.maximumReleasablePressure(initialNode)
}

fun part1Greedy(input: List<String>): Int {
    val valves = parseValves(input)
    val nonZeroValves = valves.values.filter { valve -> valve.flowRate > 0 }
    val openedValveNames = HashSet<String>()
    var timeElapsed = 0
    var totalEventualPressureReleased = 0
    var currentValveName = "AA" // TODO: Or is it the first valve always?
    while (openedValveNames.size < nonZeroValves.size && timeElapsed < 30) {
        // Find the greediest next unopened valve with non-zero-flow (which could be current)
        // using the shortest physical path from here to that valve.
        val nextValveOpeningStats = nonZeroValves
            .filter { valve -> valve.name !in openedValveNames }
            .associateWith { destinationValve ->
                val d = leastPathCost(
                    start = currentValveName,
                    edges = { valveName ->
                        valves[valveName]!!.leadsTo.map { adjacentValveName ->
                            Edge(
                                destination = adjacentValveName,
                                cost = 1
                            )
                        }
                    },
                    isEnd = { valveName ->
                        valveName == destinationValve.name
                    }
                )

                // Ignore if it would take too long to get to
                if ((d + 1) <= (30 - timeElapsed)) ValveOpeningStats(
                    distance = d,
                    eventualPressureReleased = (30 - timeElapsed - d - 1) * destinationValve.flowRate
                )
                else null
            }

        // Choose the max eventual pressure released
        val (nextValve, nextValveStats) = nextValveOpeningStats.entries.maxBy { entry ->
            entry.value?.eventualPressureReleased ?: 0
        }
        if (nextValveStats == null) {
            timeElapsed = 30
        } else {
            println("Opening valve ${nextValve.name} after $timeElapsed minutes")

            // Mark us being at the valve
            currentValveName = nextValve.name

            // Mark the valve as opened
            openedValveNames += nextValve.name

            // Add to elapsed time
            timeElapsed += nextValveStats.distance + 1

            // Add the pressure released
            totalEventualPressureReleased += nextValveStats.eventualPressureReleased
        }
    }
    return totalEventualPressureReleased
}

private data class ValveOpeningStats(
    val distance: Int,
    val eventualPressureReleased: Int
)

private val valveParser =
    RegexParser("""Valve (\p{Upper}+) has flow rate=(\d+); tunnels? leads? to valves? (.*)""") { captures ->
        Valve(
            name = captures[0],
            flowRate = captures[1].toInt(),
            leadsTo = captures[2].split(", ")
        )
    }

private fun parseValves(input: List<String>) =
    valveParser
        .parseLines(input)
        .associateBy { valve -> valve.name }

private data class Valve(
    val name: String,
    val flowRate: Int,
    val leadsTo: List<String>
)

// A node is:
// The valve that I'm at
// The set of open valves
// The elapsed minutes
// The pressure released
private data class Node(
    val currentValveName: String,
    val openedValves: Set<String>,
    val elapsedMinutes: Int,
    val pressureReleased: Int
)

private fun Map<String, Valve>.maximumReleasablePressure(startNode: Node): Int = max(
    startNode.pressureReleased,
    startNode.pressureReleased + (
        nodesAfter(startNode)
            .takeIf { it.isNotEmpty() }
            ?.maxOf { nextNode ->
                maximumReleasablePressure(nextNode)
            } ?: 0
        )
)

// TODO: Don't go from a closed valve to a visited valve? (wait, that's not always true)
// TODO: Just stop when you've visited all valves?
// TODO: Well, actually, I think it's true to say that it can't be useful to revisit a valve
//  if you haven't opened any valves since you were last there
//  (i.e. track visited valves but reset, except maybe the current valve, when you open a valve)
//  Because this still allows you to go through a valve without opening, to get to a valve to open,
//  and then back-track through that valve again back to the visited valve to then go open another valve.

// TODO: Or could this somehow be modelled as a least-cost path?
//  Like imagine you could have all of the valves releasing their flow for the whole time,
//  but then each move you make adds to the flow that is _not_ released?

// TODO: Or is there some sort of graph path maximization algorithm? Like a "longest" or "most cost" path?
//  There is! -> https://en.wikipedia.org/wiki/Longest_path_problem

// TODO: To simplify the graph, find all of the shortest pair-wise paths in terms of simple distance between valves.
//  Then, we can consider a meta-graph where each node has a valve and a set of open valves,
//  where from each node, there are edges leading to, for each unopened valve,
//  a state where we are at that valve and it is now opened,
//  with a negative cost equal to: (D + 1) x F
//
//  Where D equals the physical distance of the shortest path to that valve,
//  and F equals the sum of the flow rates of the already-opened valves.
//
//  Basically, what we're saying is that the only sensible move is to proceed via the shortest path
//  to an unopened valve and open it.
//
// But, two things:
//
//  1. We must limit the total time T = (sum of D for all valve-to-valve traversals) + (number of valves opened)
//
//  2. We also need to be able, in the case that all valves are opened, to jump to t = 30 minutes,
//      with a negative cost of (30 - t) x F
//
// In both cases, it feels like I need to include the elapsed time, and this is going to blow-up the size of the graph!

// TODO: At the end of the day, the only possibilities we're really considering is the different orders in which to open
//  all of the valves (or some subset of them as time allows).
//
//  But for V valves that's O(V!)
//
//  Is there a greedy algorithm somehow, like can I somehow, at each step, pick the best available trade-off
//  for any next valve, between the time it takes to get there, vs. the total pressure that will then be released
//  in the rest of the time?

// The next nodes are:
// IFF elapsed minutes < 30:
//   Each of these with elapsed minutes + 1 and pressure released + flowRate of all previously opened valves
//      IFF the current valve is closed -> the valve is open
//      For each adjacent valve -> go to that valve

// TODO: Do this on the simplified graph?
//  https://cp-algorithms.com/graph/bellman_ford.html
//  Or this fancy new one?
//  https://www.quantamagazine.org/finally-a-fast-algorithm-for-shortest-paths-on-negative-graphs-20230118/
//  OR, is it worth making a modified version so that we don't have to add time to the graph and thus blowing it up?


private fun Map<String, Valve>.nodesAfter(node: Node): List<Node> {
    println("Considering nodes after $node")

    if (node.elapsedMinutes >= 30)
        return emptyList()

    val pressureReleasePerMinute = node.openedValves.sumOf { openedValveName ->
        this[openedValveName]!!.flowRate
    }

    if (node.openedValves == values.filter { it.flowRate > 0 }.map { it.name }.toSet())
        return listOf(
            node.copy(
                elapsedMinutes = 30, // TODO: Extract constant for 30 minutes
                pressureReleased = node.pressureReleased + pressureReleasePerMinute * (30 - node.elapsedMinutes)
            )
        )

    val nextPressureReleased = node.pressureReleased + pressureReleasePerMinute
    val nextElapsedMinutes = node.elapsedMinutes + 1

    return listOfNotNull(
        if (this[node.currentValveName]!!.flowRate > 0 && node.currentValveName !in node.openedValves)
            node.copy(
                openedValves = node.openedValves + node.currentValveName,
                elapsedMinutes = nextElapsedMinutes,
                pressureReleased = nextPressureReleased
            )
        else null
    ) + this[node.currentValveName]!!.leadsTo.map { adjacentValveName ->
        node.copy(
            currentValveName = adjacentValveName,
            elapsedMinutes = nextElapsedMinutes,
            pressureReleased = nextPressureReleased
        )
    }
}

fun part2(input: List<String>) = input.size
