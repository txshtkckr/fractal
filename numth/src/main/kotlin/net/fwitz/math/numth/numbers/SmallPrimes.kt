package net.fwitz.math.numth.numbers

import java.util.*
import kotlin.math.sqrt

object SmallPrimes {
    const val SMALL_PRIME_LIMIT = 1000
    val SMALL_PRIMES = sieveSmallPrimes()

    operator fun iterator() = SMALL_PRIMES.iterator()

    fun stream() = SMALL_PRIMES.stream


    private fun sieveSmallPrimes(): IntSet {
        val primes = BitSet(SMALL_PRIME_LIMIT)
        val sqrt = sqrt(SMALL_PRIME_LIMIT.toDouble()).toInt()
        primes.set(2, SMALL_PRIME_LIMIT)

        var prime = 2
        do {
            var i = prime + prime
            while (i < SMALL_PRIME_LIMIT) {
                primes.clear(i)
                i += prime
            }
            prime = primes.nextSetBit(prime + 1)
        } while (prime != -1 && prime <= sqrt)
        return IntSet.bits(primes, SMALL_PRIME_LIMIT)
    }


    @JvmStatic
    fun main(args: Array<String>) {
        println(SMALL_PRIMES)
    }
}