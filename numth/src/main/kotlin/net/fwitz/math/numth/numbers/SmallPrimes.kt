package net.fwitz.math.numth.numbers

import java.util.*
import java.util.stream.IntStream

object SmallPrimes {
    private const val STOP_AT = 1000
    private val SMALL_PRIMES = sieve(STOP_AT)

    operator fun iterator(): PrimitiveIterator.OfInt {
        return SMALL_PRIMES.iterator()
    }

    fun stream(): IntStream {
        return SMALL_PRIMES.stream()
    }

    fun lessThan(stopAt: Int): IntSet {
        require(stopAt > 2) { "stopAt <= 2: $stopAt" }
        return if (stopAt > STOP_AT) sieve(stopAt) else SMALL_PRIMES.truncate(stopAt)
    }

    private fun sieve(stopAt: Int): IntSet {
        val primes = BitSet(stopAt)
        primes[2] = stopAt
        var prime = 2
        do {
            var i = prime + prime
            while (i < stopAt) {
                primes.clear(i)
                i += prime
            }
            prime = primes.nextSetBit(prime + 1)
        } while (prime != -1)
        return IntSet.ints(primes)
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println(SMALL_PRIMES)
    }

    private class BitSetIterator private constructor(private val bits: BitSet) : PrimitiveIterator.OfInt {
        var next: Int = bits.nextSetBit(0)

        override fun nextInt(): Int {
            val next = next
            if (next == -1) {
                throw NoSuchElementException()
            }
            this.next = bits.nextSetBit(next + 1)
            return next
        }

        override fun hasNext(): Boolean {
            return next != -1
        }

        override fun remove() = throw UnsupportedOperationException()
    }
}