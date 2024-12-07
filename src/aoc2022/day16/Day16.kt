package aoc2022.day16

import RegexParser
import parseLines
import printLines
import println
import readInput
import kotlin.math.max

fun main() {
    test()

    // test if implementation meets criteria from the description, like:
    //val testInput = readInput("aoc2022/day16/Day16_test")
    //val input = readInput("aoc2022/day16/Day16")

    //check(part1(testInput).also { it.println() } == 26)
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
    val valves = valveParser.parseLines(input).associateBy { it.name }
    valves.entries.printLines()

    val initialNode = Node(
        currentValveName = "AA",
        openedValves = emptySet(),
        elapsedMinutes = 0,
        pressureReleased = 0
    )

    return valves.maximumReleasablePressure(initialNode)
}

private val valveParser =
    RegexParser("""Valve (\p{Upper}+) has flow rate=(\d+); tunnels? leads? to valves? (.*)""") { captures ->
        Valve(
            name = captures[0],
            flowRate = captures[1].toInt(),
            leadsTo = captures[2].split(", ")
        )
    }

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

// The next nodes are:
// IFF elapsed minutes < 30:
//   Each of these with elapsed minutes + 1 and pressure released + flowRate of all previously opened valves
//      IFF the current valve is closed -> the valve is open
//      For each adjacent valve -> go to that valve
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
