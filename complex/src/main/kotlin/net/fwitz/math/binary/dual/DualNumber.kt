package net.fwitz.math.binary.dual

import net.fwitz.math.binary.BinaryNumber
import net.fwitz.math.binary.BinaryNumber.Companion.boxInf
import net.fwitz.math.binary.RealMath
import net.fwitz.math.binary.RealMath.rcos
import net.fwitz.math.binary.RealMath.rcosh
import net.fwitz.math.binary.RealMath.rexp
import net.fwitz.math.binary.RealMath.rsin
import net.fwitz.math.binary.RealMath.rsinh
import net.fwitz.math.binary.RealMath.rtanh
import kotlin.math.absoluteValue
import kotlin.math.ln
import kotlin.math.pow

class DualNumber(
    x: Double,
    y: Double
) : BinaryNumber<DualNumber>(x, y) {
    companion object {
        val POSITIVE_INFINITY = DualNumber(Double.POSITIVE_INFINITY, 0.0)
        val NEGATIVE_INFINITY = DualNumber(Double.NEGATIVE_INFINITY, 0.0)
        val NaN = DualNumber(Double.NaN, Double.NaN)
        val ZERO = DualNumber(0.0, 0.0)
        val ONE = DualNumber(1.0, 0.0)

        fun dualNumber(x: Double, y: Double) = DualNumber(x, y)

        private fun div(z: DualNumber, w: BinaryNumber<out DualNumber>): DualNumber {
            var a = z.x
            var b = z.y
            var c = w.x
            var d = w.y
            if (d == 0.0) return DualNumber(a / c, b / c)

            var x = a / c
            if (x.isNaN()) return NaN

            val bc = b * c
            val ad = a * d
            val numer = bc - ad
            val denom = c * c
            var y = numer / denom

            // Recover infinities and zeros that computed as NaN+iNaN;
            // the only cases are infinite/finite, and finite/infinite, ...
            if (x.isNaN() && y.isNaN()) {
                if ((a.isInfinite() || b.isInfinite()) && c.isInfinite() && d.isFinite()) {
                    a = boxInf(a)
                    b = boxInf(b)
                    x = Double.POSITIVE_INFINITY * (b * c)
                    y = Double.POSITIVE_INFINITY * (b * c - a * d) * c
                } else if ((c.isInfinite() || d.isInfinite()) && a.isFinite() && b.isFinite()) {
                    c = boxInf(c)
                    d = boxInf(d)
                    x = 0.0 * (b * c)
                    y = 0.0 * (b * c - a * d)
                }
            }
            return DualNumber(x, y)
        }
    }

    override fun z(x: Double, y: Double) = DualNumber(x, y)

    override val abs get() = x.absoluteValue
    override val abs2 get() = x * x
    override val arg get() = y / x
    override val inverse: DualNumber
        get() {
            val xInv = 1 / x
            return DualNumber(xInv, -y * xInv * xInv)
        }

    // j[a + jb] = ja + jjb = ja
    override val timesJ get() = DualNumber(0.0, x)

    // jy[a + jb] = jya + jjby = jya
    override fun timesJ(y: Double) = DualNumber(0.0, this.x * y)

    // -j[a + jb] = -ja - jjb = -ja
    override val timesNegativeJ get() = DualNumber(0.0, -x)

    override fun times(c: BinaryNumber<out DualNumber>) = DualNumber(
        x = x * c.x,
        y = x * c.y + c.x * y
    )

    // For dual numbers, values on the pure epsilon axis (that is, those whose real part is 0), are still zeroes,
    // so it is not possible to divide by them.
    override val divJ
        get() = when {
            x.isNaN() -> NaN
            x > 0 -> POSITIVE_INFINITY
            x < 0 -> NEGATIVE_INFINITY
            else -> NaN
        }

    override fun divJ(y: Double) = if (y.isNaN()) NaN else divJ

    override val divNegativeJ get() = divJ

    // Assume: z = x + jy = r e^(ja)
    //    x + jy = r e^(ja)
    //    x + jy = r (1 + ja)
    //    x + jy = r + rja
    // Therefore, since the real and j terms are independent, they must equate individually
    //    x = r
    //    y = ra
    //    a = y/r = y/x
    // Then:
    //    log(z) = log(r e^(ja))
    //    log(z) = log(r) + log(e^(ja))
    //    log(z) = log(r) + ja
    // So:
    //    log(z) = log(x) + j(y/x)
    override val log get() = DualNumber(ln(x), y / x)

    override val exp: DualNumber get() = rexp(x).let { DualNumber(it, it * y) }

    override fun pow(a: Double) = when {
        a == 0.0 -> ONE
        a == 1.0 -> this
        a == -1.0 -> inverse
        x == 0.0 && y == 0.0 -> ZERO
        else -> {
            val xam1 = x.pow(a)
            DualNumber(x * xam1, a * xam1 * y)
        }
    }

    override fun pow(c: BinaryNumber<out DualNumber>) = if (c.y == 0.0) pow(c.x) else (log * c).exp

    /**
     * For dual numbers, this means that negative `x` values return a function to reflect over the line
     * `y = 0`; that is, `x` gets negated, with `y` left unmodified.
     */
    override val region1dual get(): (DualNumber) -> DualNumber {
        if (x >= 0) return { it }
        return { DualNumber(-it.x, it.y) }
    }

    // See SplitComplex.sin for details about how this gets worked out.  The key things to know are that
    //    sin jy = jy - (jy)^3/3! + (jy)^5/5! - ...
    //  And since j^2 = 0, all terms except the first are 0, so we have:
    //    sin jy = jy
    //  For cos, we have a similar turn of events:
    //    cos jy = 1 - (jy)^2/2! + (jy)^4/4! - ...
    //    cos jy = 1
    //  Using the double-angle formula for sin:
    //    sin(a + b) = sin(a) cos(b) + cos(a) sin(b)
    //    sin(x + jy) = sin(x) cos(jy) + cos(x) sin(jy)
    //    sin(x + jy) = sin(x) + jy cos(x)
    override val sin: DualNumber
        get() = when {
            y == 0.0 -> DualNumber(rsin(x), 0.0)
            x == 0.0 -> DualNumber(0.0, y)
            else -> DualNumber(rsin(x), y * rcos(x))
        }

    //  Using the double-angle formula for cos:
    //    cos(a + b) = cos(a) cos(b) - sin(a) sin(b)
    //    cos(x + jy) = cos(x) cos(jy) - sin(x) sin(jy)
    //    cos(x + jy) = cos(x) - jy sin(x)
    override val cos: DualNumber
        get() = when {
            y == 0.0 -> DualNumber(rcos(x), 0.0)
            x == 0.0 -> ONE
            else -> DualNumber(rcos(x), -y * rsin(x))
        }

    // First we need tan(jy):
    //    tan(jy) = sin(jy) / cos(jy)
    //    tan(jy) = jy / 1
    //    tan(jy) = jy
    // The double-angle formula for tan(a + b) is a bit messy, but simplifies quickly for dual numbers:
    //    tan(a + b) = (tan(a) + tan(b)) / (1 - tan(a)tan(b))
    //    tan(x + jy) = (tan(x) + tan(jy)) / (1 - tan(x)tan(jy))  [ substitute a = x, b = jy ]
    //    tan(x + jy) = (tan(x) + jy) / (1 - jy tan(x))           [ tan(jy) = jy ]
    //    tan(x + jy) = (tan(x) + jy) (1 + jy tan(x)) / 1         [ multiplying numer and denom by conjugate ]
    //    tan(x + jy) = tan(x) + jy tan2(x) + jy + jjyy tan(x)    [ FOIL distribution ]
    //    tan(x + jy) = tan(x) + jy [1 + tan2(x)]                 [ j^2 = 0, factor out jy from middle terms ]
    //    tan(x + jy) = tan(x) + jy sec2(x)                       [ 1 + tan^2 = sec^2 ]
    override val tan: DualNumber
        get() = when {
            y == 0.0 -> DualNumber(RealMath.rtan(x), 0.0)
            x == 0.0 -> DualNumber(0.0, y)
            else -> {
                val secx = 1 / rcos(x)
                DualNumber(RealMath.rtan(x), y * secx * secx)
            }
        }

    // The Taylor expansions of sinh(jy) and cosh(jy) turn out exactly like they did for their corresponding
    // trig functions, as the only difference is the sign of high-powered terms that are all 0 anyway.
    //    sinh(jy) = jy
    //    cosh(jy) = 1
    // So
    //    sinh(a + b) = sinh(a) cosh(b) + cosh(a) sinh(b)
    //    sinh(x + jy) = sinh(x) cosh(jy) + cosh(x) sinh(jy)
    //    sinh(x + jy) = sinh(x) + jy cosh(x)
    override val sinh: DualNumber
        get() = when {
            y == 0.0 -> DualNumber(rsinh(x), 0.0)
            x == 0.0 -> DualNumber(0.0, y * rcosh(x))
            else -> DualNumber(rsinh(x), y * rcosh(x))
        }

    //    cosh(a + b) = cosh(a) cosh(b) + sinh(a) sinh(b)
    //    cosh(x + jy) = cosh(x) cosh(jy) + sinh(x) sinh(jy)
    //    cosh(x + jy) = cosh(x) + jy sinh(x)
    override val cosh: DualNumber
        get() = when {
            y == 0.0 -> DualNumber(rcosh(x), 0.0)
            x == 0.0 -> ONE
            else -> DualNumber(rcosh(x), y * rsinh(x))
        }

    //   tanh(jy) = sinh(jy) / cosh(jy)
    //   tanh(jy) = jy
    // So...
    //   tanh(a + b) = (tanh(a) + tanh(b)) / (1 + tanh(a) tanh(b))
    //   tanh(x + jy) = (tanh(x) + tanh(jy)) / (1 + tanh(x) tanh(jy))
    //   tanh(x + jy) = (tanh(x) + jy) / (1 + jy tanh(x))
    //   tanh(x + jy) = (tanh(x) + jy) (1 - jy tanh(x)) / 1
    //   tanh(x + jy) = tanh(x) - jy tanh2(x) + jy - jjyy tanh(x)
    //   tanh(x + jy) = tanh(x) + jy (1 - tanh2(x))
    //   tanh(x + jy) = tanh(x) + jy sech2 (x)
    override val tanh: DualNumber get() = when {
        y == 0.0 -> DualNumber(rtanh(x), 0.0)
        x == 0.0 -> DualNumber(0.0, y)
        else -> {
            val sechx = 1 / rcosh(x)
            DualNumber(rtanh(x), y * sechx * sechx)
        }
    }

    /**
     * Divides this dual number by `c` and returns the result.
     */
    override fun div(c: BinaryNumber<out DualNumber>) = div(this, c)

    override fun toString() = when {
        y.isNaN() -> "($x ${y}ϵ)"
        y < 0 -> "($x${y}ϵ)"
        else -> "($x+${y}ϵ)"
    }
}
