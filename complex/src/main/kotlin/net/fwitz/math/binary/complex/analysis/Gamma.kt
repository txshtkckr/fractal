package net.fwitz.math.binary.complex.analysis

import java.lang.Math
import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.binary.complex.Complex.Companion.POSITIVE_RE_INFINITY
import net.fwitz.math.binary.complex.Complex.Companion.ZERO
import net.fwitz.math.binary.complex.Complex.Companion.real
import kotlin.math.*

object Gamma {
    private val HALF_LN_2PI = 0.5 * ln(Math.PI * 2)
    private const val ONE_TWELFTH = 1.0 / 12
    private val SQRT_2PI = sqrt(2 * Math.PI)
    private val LANCZOS_P = doubleArrayOf(
        0.99999999999980993,
        676.5203681218851,
        -1259.1392167224028,
        771.32342877765313,
        -176.61502916214059,
        12.507343278686905,
        -0.13857109526572012,
        9.9843695780195716e-6,
        1.5056327351493116e-7
    )

    // The coefficients to apply to the negative odd powers of Z, starting with z^-3
    private val COEFFICIENTS = doubleArrayOf(
        -1.0 / 360,
        1.0 / 1260,
        -1.0 / 1680,
        1.0 / 1188,
        -691.0 / 360360,
        1.0 / 156,
        -3617.0 / 122400
    )

    fun gamma(x: Double) = gammaLanczos(x)
    fun gamma(z: Complex) = gammaLanczos(z)

    fun lnGamma(x: Double): Double {
        // For non-positive integers, the value is infinite
        if (x <= 0.0 && x == floor(x)) {
            return Double.POSITIVE_INFINITY
        }

        // Too far outside the range for stirling; use Lanczos
        if (x <= -11 || x >= 30) {
            return ln(gammaLanczos(x))
        }
        var adjust = 0.0
        var temp = x

        // The analysis is only considered valid for the domain 9 <= Re(z) <= 10
        // If we are outside this range, then repeatedly apply the relationship
        //    lnGamma(z + 1) = ln(z) + lnGamma(z)
        // Until we are within the allowed domain
        while (x < 9.0) {
            // Perform lnGamma on a larger value, then subtract the excess ln(z)
            adjust -= ln(temp)
            temp += 1.0
        }

        // If the value is to the right of the good range for this analysis, then compensate
        while (x > 10.0) {
            // Perform lnGamma on a smaller value, then add the missing ln(z - 1)
            temp -= 1.0
            adjust += ln(temp)
        }
        return lnGammaStirling(temp) + adjust
    }

    fun lnGamma(z: Complex): Complex {
        if (z.y == 0.0) {
            return real(lnGamma(z.x))
        }

        // Use conjugate relationship to avoid working in 3rd or 4th quad directly
        if (z.y < 0.0) {
            return lnGamma(z.conjugate).conjugate
        }

        // Too far outside the range for stirling; use Lanczos
        if (z.x <= -11 || z.x >= 30) {
            return gammaLanczos(z).log
        }
        var adjust: Complex = ZERO
        var temp = z

        // The analysis is only considered valid for the domain 9 <= Re(z) <= 10
        // If we are outside this range, then repeatedly apply the relationship
        //    lnGamma(z + 1) = ln(z) + lnGamma(z)
        // Until we are within the allowed domain
        while (temp.x < 9.0) {
            // Perform lnGamma on a larger value, then subtract the excess ln(z)
            adjust -= temp.log
            temp = temp.plus(1.0)
        }

        // If the value is to the right of the good range for this analysis, then compensate
        while (temp.x > 10.0) {
            // Perform lnGamma on a smaller value, then add the missing ln(z - 1)
            temp = temp.minus(1.0)
            adjust = adjust.plus(temp.log)
        }
        return lnGammaStirling(temp).plus(adjust)
    }

    fun lnGammaStirling(x: Double): Double {
        val temp1 = ln(x)
        val temp2 = x - 0.5
        var result = temp1 * temp2
        result -= x
        result += HALF_LN_2PI
        var xPower = 1.0 / x
        val oneOverX2 = xPower * xPower
        result += xPower * ONE_TWELFTH
        for (coefficient in COEFFICIENTS) {
            xPower *= oneOverX2
            result += xPower * coefficient
        }
        return result
    }

    fun gammaStirling(x: Double): Double {
        return exp(lnGammaStirling(x))
    }

    fun lnGammaStirling(z: Complex): Complex {
        var zPower = z.inverse
        var result = z.log * (z - 0.5) - z + HALF_LN_2PI + zPower * ONE_TWELFTH
        
        val oneOverZ2 = zPower * zPower
        for (coefficient in COEFFICIENTS) {
            zPower *= oneOverZ2
            result += zPower * coefficient
        }
        return result
    }

    fun gammaStirling(z: Complex) = lnGamma(z).let {
        when {
            it.x >= 75.0 -> POSITIVE_RE_INFINITY
            it.x < -200.0 -> ZERO
            else -> it.exp
        }
    }

    fun gammaLanczos(x: Double): Double {
        if (x < 0.5) return Math.PI / (sin(Math.PI * x) * gammaLanczos(1 - x))

        val xMinus1 = x - 1.0
        var a = LANCZOS_P[0]
        val t = xMinus1 + 7.5
        for (i in 1 until LANCZOS_P.size) {
            a += LANCZOS_P[i] / (xMinus1 + i)
        }
        return SQRT_2PI * t.pow(xMinus1 + 0.5) * exp(-t) * a
    }

    fun gammaLanczos(z: Complex): Complex {
        if (z.x < 0.5) {
            val gammaOneMinusZ = gammaLanczos(Complex.ONE - z)
            val sinPiZ = (z * Math.PI).sin
            return Complex.PI / (sinPiZ * gammaOneMinusZ)
        }

        val zMinus1 = z - 1.0
        var a: Complex = real(LANCZOS_P[0])
        val t = zMinus1 + (LANCZOS_P.size - 1.5)
        for (i in 1 until LANCZOS_P.size) {
            a += real(LANCZOS_P[i]) / (zMinus1 + i.toDouble())
        }
        return t.pow(zMinus1 + 0.5) * SQRT_2PI * t.negative.exp * a
    }
}