package net.fwitz.math.calculus

import java.util.*
import java.util.function.DoubleFunction
import java.util.stream.IntStream
import kotlin.math.sin
import kotlin.math.sqrt

class Integration private constructor(lo: Double, hi: Double, terms: Int, fn: DoubleFunction<Double>) {
    private val lo: Double
    private val hi: Double
    private val terms: Int
    private val fn: DoubleFunction<Double>
    private val dx: Double

    fun trapezium(): Double {
        var sum = 0.0
        var prev = fn.apply(lo)
        for (i in 1 until terms) {
            val x = lo + i * dx
            val fx = fn.apply(x)
            sum += prev + fx
            prev = fx
        }
        sum += prev + fn.apply(hi)
        return sum * dx * 0.5
    }

    fun midpoint(): Double {
        val dxHalf = dx * 0.5
        var sum = 0.0
        for (i in 1 until terms) {
            val x = lo + i * dx + dxHalf
            val fx = fn.apply(x)
            sum += fx
        }
        return sum * dx
    }

    fun simpson(): Double {
        val stop = if (terms and 1 == 1) terms + 1 else terms
        var sum = 0.0
        var prevA = fn.apply(lo)
        var prevB = fn.apply(lo + dx)
        var i = 2
        while (i < stop) {
            val x = lo + i * dx
            val fx = fn.apply(x)
            sum += prevA + prevB * 4 + fx
            prevA = prevB
            prevB = fx
            i += 2
        }
        sum += prevA + prevB * 4 + fn.apply(hi)
        return sum * dx * 0.3333333333
    }

    fun gauss(): Double {
        var sum = 0.0
        val dx1 = dx * (0.5 - sqrt(3.0) / 6)
        val dxr = dx * (0.5 + sqrt(3.0) / 6)
        for (i in 0 until terms) {
            val xa = lo + i * dx + dx1
            val xb = lo + i * dx + dxr
            val fa = fn.apply(xa)
            val fb = fn.apply(xb)
            sum += fa + fb
        }
        return sum * dx * 0.5
    }

    companion object {
        fun integrate(lo: Double, hi: Double, terms: Int, fn: DoubleFunction<Double>): Integration {
            return Integration(lo, hi, terms, fn)
        }

        @JvmStatic
        fun main(args: Array<String>) {
            IntStream.of(4, 8, 16, 32, 64, 128, 256, 512, 1024)
                .forEach { terms: Int -> debug(terms) }
        }

        private fun debug(terms: Int) {
            val integrate = integrate(0.0, Math.PI, terms, { a: Double -> sin(a) })
            System.out.format(
                "%4d:  trap=%10.7f  midp=%10.7f  simp=%10.7f  gaus=%10.7f\n",
                terms,
                integrate.trapezium(),
                integrate.midpoint(),
                integrate.simpson(),
                integrate.gauss()
            )
        }
    }

    init {
        require(lo < hi) { "lo >= hi: $lo >= $hi" }
        require(terms >= 2) { "terms < 2: $terms" }
        this.lo = lo
        this.hi = hi
        this.terms = terms
        this.fn = Objects.requireNonNull(fn, "fn")
        dx = (hi - lo) / terms
    }
}