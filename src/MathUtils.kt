import kotlin.math.abs
import kotlin.math.pow

fun Int.unitized() = this / abs(this).coerceAtLeast(1)

fun lcm(numbers: List<Int>) = numbers
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

private val Int.isPrime get() = this > 1 && (2..(this / 2)).all { divisor -> this % divisor != 0 }

private fun Int.factorExponent(factor: Int): Int =
    powersOf(factor).takeWhile { power -> power <= this && this % power == 0 }.count()

private fun powersOf(base: Int) = generateSequence(base) { it * base }

data class PrimeFactor(val prime: Int, val exponent: Int)