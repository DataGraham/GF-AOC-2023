package day08

import readInput
import toInfiniteSequence
import java.lang.IllegalArgumentException
import kotlin.math.pow

fun main() {
    // test if implementation meets criteria from the description, like:
    check(part1(readInput("day08/Day08_test")) == 6)
    check(part2(readInput("day08/Day08_test2")) == 6L)

    val input = readInput("day08/Day08")
    println("Part 1 Answer: ${part1(input)}")
    println("Part 2 Answer: ${part2(input)}")
}

fun part1(input: List<String>): Int {
    val networkConnectionsByStart = getNetworkConnectionsByStart(input)
    val locations = input.getEndlessDirections().scan("AAA") { location, direction ->
        networkConnectionsByStart.move(from = location, direction = direction)
    }
    return locations.indexOf("ZZZ")
}

fun part2(input: List<String>): Long {
    val networkConnectionsByStart = getNetworkConnectionsByStart(input)
    val initialLocations = networkConnectionsByStart.keys.filter { it.last() == 'A' }
    val endlessDirections = input.getEndlessDirections()
    val pathLengths = initialLocations.map { initialLocation ->
        endlessDirections.scan(initialLocation) { currentLocation, direction ->
            networkConnectionsByStart.move(from = currentLocation, direction = direction)
        }.indexOfFirst { it.last() == 'Z' }
    }
    return lcm(pathLengths)
}

private fun lcm(numbers: List<Int>) = numbers
    // Compute the prime factorization of each path length
    .flatMap { it.primeFactorization() }
    .groupBy { it.prime }
    .values
    // For each prime factor that appears, take only the maximum power
    .map { powersOfThisPrime -> powersOfThisPrime.maxBy { it.exponent } }
    .map { it.prime.toDouble().pow(it.exponent.toDouble()).toLong() }
    // Return the product of these powers (the LCM)
    .reduce(Long::times)

fun Int.primeFactorization(): Sequence<PrimeFactor> {
    // val sqrt = floor(sqrt(this.toFloat()))
    return primeNumbers
        .takeWhile { it <= this }
        .mapNotNull { primeFactor ->
            factorExponent(primeFactor)
                .takeIf { it != 0 }
                ?.let { exponent -> PrimeFactor(prime = primeFactor, exponent = exponent) }
        }
}

private val positiveIntegers = generateSequence(1) { it + 1 }

// Generate a sequence of prime numbers:
//  https://www.tutorialspoint.com/kotlin-program-to-display-all-prime-numbers-from-1-to-n
// TODO: Or more complex but faster, for large numbers anyway: https://en.wikipedia.org/wiki/AKS_primality_test
private val primeNumbers = positiveIntegers.filter { it.isPrime }

private val Int.isPrime get() = this > 1 && (2 .. (this / 2)).all { divisor -> this % divisor != 0 }

private fun Int.factorExponent(factor: Int): Int =
    powersOf(factor).takeWhile { power -> power <= this && this % power == 0 }.count()

private fun powersOf(base: Int) = generateSequence(base) { it * base }

data class PrimeFactor(val prime: Int, val exponent: Int)

private fun List<String>.getEndlessDirections() = first().toList().toInfiniteSequence()

private fun getNetworkConnectionsByStart(input: List<String>) = input
    .drop(2)
    .map { line -> NetworkConnection.parseFromString(line) }
    .associateBy { it.start }

fun Map<String, NetworkConnection>.move(from: String, direction: Char) =
    this[from]!!.let {
        when (direction) {
            'L' -> it.left
            'R' -> it.right
            else -> throw IllegalArgumentException("Invalid direction: $direction")
        }
    }

data class NetworkConnection(
    val start: String,
    val left: String,
    val right: String
) {
    companion object {
        private val connectionRegex = Regex("""([A-Z0-9]+) = \(([A-Z0-9]+), ([A-Z0-9]+)\)""")

        fun parseFromString(line: String) = connectionRegex
            .matchEntire(line)!!
            .let { matchResult -> matchResult.groups.drop(1).map { it!!.value } }
            .let { (start, left, right) ->
                NetworkConnection(start = start, left = left, right = right)
            }
    }
}