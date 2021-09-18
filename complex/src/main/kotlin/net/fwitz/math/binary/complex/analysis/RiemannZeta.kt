package net.fwitz.math.binary.complex.analysis

import net.fwitz.math.binary.complex.Complex
import kotlin.math.ln

object RiemannZeta {
    private const val DEFAULT_TERMS = 16
    private const val MAX_TERMS = 1000
    private const val EPSILON = 2e-7
    private const val PI_OVER_2 = Math.PI / 2

    private val MINUS_ONE_HALF: Complex = Complex(-0.5, 0.0)
    private val LAURENT_COEFFS = initLaurentCoefficients()
    private val LN = initLogTable()
    private val LN_3 = LN[3]
    private val LN_5 = LN[5]
    private val LN_7 = LN[7]
    private val LN_11 = LN[11]
    private val LN_13 = LN[13]

    fun zeta(s: Complex, terms: Int = DEFAULT_TERMS) = when (s) {
        Complex.ZERO -> MINUS_ONE_HALF
        Complex.ONE -> Complex.POSITIVE_RE_INFINITY
        else -> fromGloballyConvergentNestedSumEta(s)
        //        if (s.x() < 0) {
//            return functionalEquation(s, terms);
//        }
//
//        if (s.x() >= 2.5) {
//            return zetaSum(s);
//        }
//
//        if (s.y() >= -2 && s.y() <= 2) {
//            return laurentSeries(s, terms);
//        }
//
//        return scaledEtaFunction(s, terms);
    }

    fun xi(s: Complex): Complex {
        val oneHalfS = s.times(0.5)
        return oneHalfS *
                (s - 1.0) *
                Complex.PI.pow(-oneHalfS) *
                Gamma.gamma(oneHalfS) *
                zeta(s)
    }

    fun xi(s: Complex, terms: Int): Complex {
        val oneHalfS = s.times(0.5)
        return oneHalfS *
                (s - 1.0) *
                Complex.PI.pow(-oneHalfS) *
                Gamma.gamma(oneHalfS) *
                zeta(s, terms)
    }

    private fun eulerProductMediumSigma(s: Complex): Complex {
        val negS = -s
        val result = oneMinus(negS.exp2) *
                oneMinus(negS.expln(LN_3)) *
                oneMinus(negS.expln(LN_5)) *
                oneMinus(negS.expln(LN_7)) *
                oneMinus(negS.expln(LN_11)) *
                oneMinus(negS.expln(LN_13))
        return result.inverse
    }

    private fun eulerProductLargeSigma(s: Complex): Complex {
        val negS = -s
        val result = oneMinus(negS.exp2) *
                oneMinus(negS.expln(LN_3)) *
                oneMinus(negS.expln(LN_5))
        return result.inverse
    }

    private fun fromGloballyConvergentNestedSumEta(s: Complex): Complex {
        val twoToOneMinusS: Complex = Complex.TWO.pow(Complex.ONE.minus(s))
        val scale: Complex = Complex.ONE.minus(twoToOneMinusS)
        return DirichletEta.globallyConvergentNestedSumEta(s).div(scale)
    }

    // zeta(S) = eta(s) / (1 - 2^(1-s))
    private fun scaledEtaFunction(s: Complex, terms: Int): Complex {
        val twoToOneMinusS: Complex = Complex.TWO.pow(Complex.ONE.minus(s))
        val scale: Complex = Complex.ONE.minus(twoToOneMinusS)
        return DirichletEta.eta(s, terms).div(scale)
    }

    // 2^s * pi^(s-1) * sin(pi * s / 2) * gamma(1 - s)
    private fun functionalCoefficient(
        s: Complex,
        sMinusOne: Complex,
        oneMinusS: Complex
    ) = Complex.TWO.pow(s) *
            Complex.PI.pow(sMinusOne) *
            (s * PI_OVER_2).sin *
            Gamma.gamma(oneMinusS)

    // zeta(s) = 2^s * pi^(s-1) * sin(pi * s / 2) * gamma(1 - s) * zeta(1 - s)
    // zeta(s) = xi(s) * zeta(1-s)
    private fun functionalEquation(s: Complex, terms: Int): Complex {
        val sMinusOne = s - 1.0
        val oneMinusS = -sMinusOne
        return functionalCoefficient(s, sMinusOne, oneMinusS)
            .times(zeta(oneMinusS, terms))
    }

    private fun laurentSeries(s: Complex, terms: Int): Complex {
        val len = terms.coerceAtMost(LAURENT_COEFFS.size)
        val sMinus1 = s.minus(1.0)
        var result = sMinus1.inverse
        for (n in 0 until len) {
            result += sMinus1.pow(n.toDouble()) * LAURENT_COEFFS[n]
        }
        return result
    }

    private fun zetaSum(s: Complex): Complex {
        val negS = -s
        var result: Complex = Complex.ZERO
        var term: Complex = Complex.ZERO
        for (i in 1..MAX_TERMS) {
            term = negS.expln(LN[i])
            result += term
            if (term.abs < EPSILON) return result
        }
        throw IllegalStateException("Sum did not converge for $s; last term=$term")
    }

    private fun oneMinus(s: Complex) = Complex(1.0 - s.x, -s.y)

    private fun initLaurentCoefficients(): DoubleArray {
        val len = Stieltjes.terms()
        val values = DoubleArray(len)
        values[0] = Stieltjes.stieltjes(0)
        values[1] = -Stieltjes.stieltjes(1)
        var scale = -1.0
        for (n in 2 until len) {
            scale /= -n.toDouble()
            values[n] = scale * Stieltjes.stieltjes(n)
        }
        return values
    }

    private fun initLogTable(): DoubleArray {
        val values = DoubleArray(MAX_TERMS + 1)
        values[0] = Double.NEGATIVE_INFINITY
        values[1] = 0.0
        for (i in 2..MAX_TERMS) {
            values[i] = ln(i.toDouble())
        }
        return values
    }

    @JvmStatic
    fun main(args: Array<String>) {
//        Complex z = complex(0.5, 102);
//        for (int i = 1; i < 100; ++i) {
//            final Complex eta = eta(z, i);
//            final Complex zeta = zeta(z, i);
//            System.out.format("%4d:  eta(z)=(%10.7f, %10.7f)  zeta(z)=(%10.7f, %10.7f)\n",
//                    i, eta.x(), eta.y(), zeta.x(), zeta.y());
//        }
        demo(1.0, 1.0)
    }

    private fun demo(x: Double, y: Double) {
        val z = Complex(x, y)
        val zeta = zeta(z)
        System.out.format(
            "z=(%10.7f, %10.7f)  zeta(z)=(%10.7f, %10.7f)\n",
            x, y, zeta.x, zeta.y
        )
    }
}