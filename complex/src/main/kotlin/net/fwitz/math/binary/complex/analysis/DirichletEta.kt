package net.fwitz.math.binary.complex.analysis

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.binary.complex.functions.Binomial.BINOMIAL_MAX_N
import net.fwitz.math.binary.complex.functions.Binomial.binom
import net.fwitz.math.binary.complex.functions.Factorial
import net.fwitz.math.binary.complex.functions.Factorial.factorial
import kotlin.math.absoluteValue
import kotlin.math.ln
import kotlin.math.pow
import kotlin.math.roundToInt

object DirichletEta {
    private val BORWEIN_D_TERMS = Factorial.MAX_INT_VALUE shr 1
    private val BORWEIN_D = calculateBorweinD(BORWEIN_D_TERMS)
    private const val DEFAULT_TERMS = 16
    private const val DEFAULT_TERMS_BOOST_NEAR_1 = 84
    private const val MINUS_PI_OVER_2 = Math.PI / -2
    private val CPI = Complex.PI

    fun eta(s: Complex): Complex {
        val closeness = 1.0 - (s.x - 1).absoluteValue
        var terms = DEFAULT_TERMS
        if (closeness > 0.0) {
            // & ~1 because we need to keep the term count even because we are oscillating as we converge to the
            // correct value, and the sign flip would cause extra noise
            terms += (DEFAULT_TERMS_BOOST_NEAR_1 * closeness).toInt() and 1.inv()
        }
        return eta(s, terms)
    }

    fun eta(s: Complex, terms: Int): Complex {
        // eta(0) = 1/2
        if (Complex.ZERO == s) {
            return Complex(0.5, 0.0)
        }

        // eta(-2n) = 0
        if (s.x < 0.0 && s.y == 0.0) {
            val reInt = s.x.roundToInt()
            if (s.x == reInt.toDouble() && reInt and 1 == 0) {
                return Complex.ZERO
            }
        }
        return if (s.x > -2) {
            eulerAcceleratedSum(s, terms)
        } else functionalEquation(s, terms)
    }

    private fun eulerAcceleratedSum(s: Complex, terms: Int): Complex {
        val minusS = -s
        var outerSum: Complex = Complex.ZERO
        var negate = true
        var coeff = 1.0
        var innerSum: Complex = Complex.ONE
        var nMinusJPlus1 = terms.toLong()
        var j = 1L
        for (k in terms * terms downTo terms + 1) {
            val term: Complex = Complex.real(k.toDouble()).pow(minusS) * innerSum
            outerSum = outerSum.plusOrMinus(term, negate)
            coeff = coeff * nMinusJPlus1 / j
            innerSum += coeff
            --nMinusJPlus1
            ++j
            negate = !negate
        }
        outerSum = outerSum.div(2.0.pow(terms.toDouble()))
        for (k in terms downTo 1) {
            val term: Complex = Complex.real(k.toDouble()).pow(minusS)
            outerSum = outerSum.plusOrMinus(term, negate)
            negate = !negate
        }
        return outerSum
    }

    // eta(-s) = 2 * (1 - 2^(-s-1)) / (1 - 2^(-s)) * pi^(-s-1) * s * sin(pi * s / 2) * gamma(s) * eta(s + 1)
    // eta(s) = 2 * (1 - 2^(s-1)) / (1 - 2^s) * pi ^ (s-1) * (-s) * sin(s * -pi/2) * gamma(-s) * eta(-s + 1)
    private fun functionalEquation(s: Complex, terms: Int): Complex {
        val sMinus1 = s - 1.0
        val minusS = -s
        val fracTermNumer: Complex = Complex.ONE - Complex.TWO.pow(sMinus1)
        val fracTermDenom: Complex = Complex.ONE - Complex.TWO.pow(s)
        val piTerm = CPI.pow(sMinus1)
        val sinTerm = s.times(MINUS_PI_OVER_2).sin
        val gammaTerm = Gamma.gamma(minusS)
        val etaTerm = eta(minusS.plus(1.0), terms)
        return fracTermNumer * 2.0 / fracTermDenom * piTerm * minusS * sinTerm * gammaTerm * etaTerm
    }

    private fun borweinsMethod(s: Complex): Complex {
        val d = BORWEIN_D
        val n = d.size - 1
        val dn = d[n]
        var negate = false
        val minusS = -s
        var sum: Complex = Complex.ZERO
        for (k in 0 until n - 1) {
            val term: Complex = Complex.real((k + 1).toDouble()).pow(minusS) * (d[k] - dn)
            sum = sum.plusOrMinus(term, negate)
            negate = !negate
        }
        return sum / -d[n]
    }

    private fun calculateBorweinD(n: Int): DoubleArray {
        val d = DoubleArray(n + 1)
        for (k in 0..n) {
            var sum = 0.0
            var fourToI: Long = 1
            for (i in 0..k) {
                val x1 = factorial(n + i - 1).toDouble() / factorial(n - i)
                val x2 = fourToI.toDouble() / factorial(2 * i)
                sum += x1 * x2
                fourToI = fourToI shl 2
            }
            d[k] = sum * n
        }
        return d
    }

    // Based on (21) from http://mathworld.wolfram.com/RiemannZetaFunction.html
    // sum(n=0 to inf):
    //   (1/2^(n+1)) * sum(k=0 to n):
    //     (-1)^k * binom(n, k) * (k + 1)^(-s)
    fun globallyConvergentNestedSumEta(s: Complex): Complex {
        val EPSILON = 1e-7

        // The n=0 term is always exactly 0.5
        // The n=1 term is 0.25 * (1 - 2^(-s))
        // Distributing and adding them together gives 0.75 - 0.25 * 2^(-s)
        val minusS = -s
        var sum: Complex = Complex.real(0.75) - (Complex.TWO.pow(minusS) * 0.25)
        var n = 2
        var scale = 0.125
        while (true) {
            // The k=0 inner term is always 1, so we start from k=1
            var innerSum: Complex = Complex.ONE
            var negate = true
            for (k in 1..n) {
                innerSum = innerSum.plusOrMinus(
                    Complex.real((k + 1).toDouble()).pow(minusS).times(binom(n, k).toDouble()),
                    negate
                )
                negate = !negate
            }
            innerSum = innerSum.times(scale)
            sum = sum.plus(innerSum)
            if (n == BINOMIAL_MAX_N || innerSum.abs2 < EPSILON) return sum
            ++n
            scale /= 2.0
        }
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val s = Complex(0.5, 25.010858)
        for (terms in 1..20) {
            debug(terms, s)
        }
    }

    private fun debug(terms: Int, s: Complex) {
        val etaS = eta(s)
        System.out.format("%4d: (%10.7f, %10.7f) => (%10.7f, %10.7f)\n", terms, s.x, s.y, etaS.x, etaS.y)
    }
}