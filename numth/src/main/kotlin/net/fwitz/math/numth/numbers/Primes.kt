package net.fwitz.math.numth.numbers

import net.fwitz.math.numth.numbers.SmallPrimes.SMALL_PRIME_LIMIT
import java.util.*
import kotlin.math.sqrt

object Primes {
    fun upTo(last: Int): IntSet = lessThan(last + 1)

    fun lessThan(stopAt: Int): IntSet {
        require(stopAt > 2) { "stopAt <= 2: $stopAt" }
        return when {
            stopAt > SMALL_PRIME_LIMIT -> sieveLargePrimes(stopAt)
            else -> SmallPrimes.SMALL_PRIMES.truncate(stopAt)
        }
    }

    private fun sieveLargePrimes(stopAt: Int): IntSet {
        val primes = BitSet(stopAt)
        val sqrt = sqrt(stopAt.toDouble()).toInt()
        primes.set(SMALL_PRIME_LIMIT, stopAt)

        // Special case processing of the small primes.
        SmallPrimes.SMALL_PRIMES.orInto(primes)
        for (prime in SmallPrimes.SMALL_PRIMES) {
            val rem = SMALL_PRIME_LIMIT % prime
            val first = if (rem > 0) SMALL_PRIME_LIMIT + prime - rem else SMALL_PRIME_LIMIT
            for (i in first until stopAt step prime) primes.clear(i)
            if (prime >= sqrt) return IntSet.bits(primes, stopAt)
        }

        var prime = primes.nextSetBit(SMALL_PRIME_LIMIT)
        while (prime != -1 && prime <= sqrt) {
            var i = prime + prime
            while (i < stopAt) {
                primes.clear(i)
                i += prime
            }
            prime = primes.nextSetBit(prime + 1)
        }

        return IntSet.bits(primes, stopAt)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println(lessThan(SMALL_PRIME_LIMIT * 10))
    }
}