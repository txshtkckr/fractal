package net.fwitz.math.binary.complex.functions

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.binary.complex.functions.Factorial.factorial

object Binomial {
    const val BINOMIAL_MAX_N = 33

    private val BINOMIAL: List<IntArray> = run {
        val table = ArrayList<IntArray>(BINOMIAL_MAX_N + 1)
        var prevRow = intArrayOf(1)
        table.add(prevRow)

        for (n in 1..BINOMIAL_MAX_N) {
            val row = IntArray(n + 1)
            row[n] = 1
            row[0] = row[n]
            row[n - 1] = n
            row[1] = row[n - 1]
            val mid = n shr 1
            for (k in 2..mid) {
                row[n - k] = prevRow[k - 1] + prevRow[k]
                row[k] = row[n - k]
            }
            prevRow = row
            table.add(row)
        }

        table
    }

    fun binom(n: Int, k: Int): Int {
        require(!(n < 0 || n > BINOMIAL_MAX_N)) {
            "binom(n=$n, k=$k): n must be in the range [0, $BINOMIAL_MAX_N] (larger values risk a sign overflow)"
        }
        require(!(k < 0 || k > n)) { "binom(n=$n, k=$k): k must be in the range [0, n]" }
        return BINOMIAL[n][k]
    }

    fun binomRow(n: Int): IntArray {
        require(!(n < 0 || n > BINOMIAL_MAX_N)) {
            "binomRow(n=$n): n must be in the range [0, $BINOMIAL_MAX_N] (larger values risk a sign overflow)"
        }
        return BINOMIAL[n]
    }

    fun binom(n: Double, k: Double) = factorial(n) / (factorial(k) * factorial(n - k))
    fun binom(n: Complex, k: Complex) = factorial(n) / (factorial(k) * factorial(n - k))

    @JvmStatic
    fun main(args: Array<String>) {
        for (n in 0..BINOMIAL_MAX_N) {
            val row = binomRow(n)
            println("row[%2d]: %s".format(n, row.contentToString()))
        }
    }
}
