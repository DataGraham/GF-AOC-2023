package aoc2022.day16

import RegexParser
import parseLines
import printLines
import println
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

// The next nodes are:
// IFF elapsed minutes < 30:
//   Each of these with elapsed minutes + 1 and pressure released + flowRate of all previously opened valves
//      IFF the current valve is closed -> the valve is open
//      For each adjacent valve -> go to that valve
private fun Map<String, Valve>.nodesAfter(node: Node): List<Node> {
    println("Considering nodes after $node")
    if (node.elapsedMinutes >= 30) return emptyList()
    val nextPressureReleased = node.openedValves.sumOf { openedValveName ->
        this[openedValveName]!!.flowRate
    }
    val nextElapsedMinutes = node.elapsedMinutes + 1
    return listOfNotNull(
        if (node.currentValveName !in node.openedValves)
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
