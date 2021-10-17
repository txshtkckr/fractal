package net.fwitz.math.numth.numbers

import kotlin.math.absoluteValue
import kotlin.math.sqrt

object Divisibility {
    fun factor(n: Int): List<Int> {
        when (n) {
            -3 -> return listOf(-1, 1, 3)
            -2 -> return listOf(-1, 1, 2)
            -1 -> return listOf(-1, 1)
            0 -> return listOf(0)
            1 -> return listOf(1)
            2 -> return listOf(1, 2)
            3 -> return listOf(1, 3)
        }

        val factors = ArrayList<Int>()
        var x = n
        if (x < 0) {
            factors.add(-1)
            x = -x
        }
        factors.add(1)

        val last = sqrt(x.toDouble()).toInt()
        Primes.upTo(last).forEach { prime ->
            while (x % prime == 0) {
                factors.add(prime)
                x /= prime
            }
        }
        return factors
    }

    fun gcd(vararg ints: Int) = gcd(ints.asList())

    fun gcd(ints: List<Int>): Int {
        val values = ints.asSequence().filter { it != 0 }.toMutableList()
        when (values.size) {
            0 -> return 0
            1 -> return values[0]
        }

        var gcd = -1
        for (i in values.indices) {
            when {
                values[i] > 0 -> gcd = 1
                else -> values[i] = -values[i]
            }
        }

        val last = values.minOf { it }
        if (last < 2) return gcd

        Primes.upTo(last).forEach { prime ->
            while (values.all { it % prime == 0 }) {
                values.indices.forEach { values[it] /= prime }
                gcd *= prime
            }
        }

        return gcd
    }

    @JvmStatic
    fun main(args: Array<String>) {
        println(gcd(54, 162))
        println(gcd(3, 0, -54, 162))
        println(gcd(-3, 0, -54, -162))
        println(gcd(210, 455))
    }
}