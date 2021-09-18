package net.fwitz.math.binary.complex

import net.fwitz.math.binary.complex.Complex.Companion.complex
import net.fwitz.math.binary.complex.Complex.Companion.imaginary
import net.fwitz.math.binary.complex.Complex.Companion.real
import net.fwitz.math.binary.complex.ComplexTest.ComplexAssert.Companion.assertComplex
import org.assertj.core.api.AbstractAssert
import org.assertj.core.api.AbstractDoubleAssert
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.assertj.core.api.DoubleAssert
import org.assertj.core.data.Offset.offset
import org.junit.Test
import java.util.stream.IntStream

class ComplexTest {
    companion object {
        private const val DEFAULT_DELTA = 0.00000001

        private fun <SELF : AbstractDoubleAssert<SELF>> AbstractDoubleAssert<SELF>.isAbout(
            expected: Double,
            delta: Double = DEFAULT_DELTA
        ) = when {
            expected.isNaN() -> isNaN
            expected.isInfinite() -> if (expected > 0.0) isPosInf else isNegInf
            expected.equals(-0.0) -> isNegZero
            expected.equals(+0.0) -> isPosZero
            else -> isCloseTo(expected, offset(delta))
        }

        private val <SELF : AbstractDoubleAssert<SELF>> AbstractDoubleAssert<SELF>.isPosInf get() = isPositive.isInfinite
        private val <SELF : AbstractDoubleAssert<SELF>> AbstractDoubleAssert<SELF>.isNegInf get() = isNegative.isInfinite
        private val <SELF : AbstractDoubleAssert<SELF>> AbstractDoubleAssert<SELF>.isPosZero get() = isEqualTo(+0.0)
        private val <SELF : AbstractDoubleAssert<SELF>> AbstractDoubleAssert<SELF>.isNegZero get() = isEqualTo(-0.0)
    }

    @Test
    fun `test isNaN`() {
        assertThat(real(0).isNaN).isFalse
        assertThat(real(4).isNaN).isFalse
        assertThat(real(-4).isNaN).isFalse
        assertThat(real(Double.NaN).isNaN).isTrue
        assertThat(real(Double.POSITIVE_INFINITY).isNaN).isFalse
        assertThat(real(Double.NEGATIVE_INFINITY).isNaN).isFalse
        assertThat(imaginary(0).isNaN).isFalse
        assertThat(imaginary(3).isNaN).isFalse
        assertThat(imaginary(-3).isNaN).isFalse
        assertThat(imaginary(Double.NaN).isNaN).isTrue
        assertThat(imaginary(Double.POSITIVE_INFINITY).isNaN).isFalse
        assertThat(imaginary(Double.NEGATIVE_INFINITY).isNaN).isFalse
        assertThat(complex(4, 3).isNaN).isFalse
        assertThat(complex(-4, 3).isNaN).isFalse
        assertThat(complex(4, -3).isNaN).isFalse
        assertThat(complex(-4, -3).isNaN).isFalse
        assertThat(Complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).isNaN).isFalse
        assertThat(Complex(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).isNaN).isFalse
        assertThat(Complex(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).isNaN).isFalse
        assertThat(Complex(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY).isNaN).isFalse
        assertThat(Complex(Double.POSITIVE_INFINITY, Double.NaN).isNaN).isTrue
        assertThat(Complex(Double.NEGATIVE_INFINITY, Double.NaN).isNaN).isTrue
        assertThat(Complex(Double.NaN, Double.POSITIVE_INFINITY).isNaN).isTrue
        assertThat(Complex(Double.NaN, Double.NEGATIVE_INFINITY).isNaN).isTrue
        assertThat(Complex(Double.NaN, Double.NaN).isNaN).isTrue
    }

    @Test
    fun `test isInfinite`() {
        assertThat(real(0).isInfinite).isFalse
        assertThat(real(4).isInfinite).isFalse
        assertThat(real(-4).isInfinite).isFalse
        assertThat(real(Double.NaN).isInfinite).isFalse
        assertThat(real(Double.POSITIVE_INFINITY).isInfinite).isTrue
        assertThat(real(Double.NEGATIVE_INFINITY).isInfinite).isTrue
        assertThat(imaginary(0).isInfinite).isFalse
        assertThat(imaginary(3).isInfinite).isFalse
        assertThat(imaginary(-3).isInfinite).isFalse
        assertThat(imaginary(Double.NaN).isInfinite).isFalse
        assertThat(imaginary(Double.POSITIVE_INFINITY).isInfinite).isTrue
        assertThat(imaginary(Double.NEGATIVE_INFINITY).isInfinite).isTrue
        assertThat(complex(4, 3).isInfinite).isFalse
        assertThat(complex(-4, 3).isInfinite).isFalse
        assertThat(complex(4, -3).isInfinite).isFalse
        assertThat(complex(-4, -3).isInfinite).isFalse
        assertThat(complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).isInfinite).isTrue
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).isInfinite).isTrue
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).isInfinite).isTrue
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY).isInfinite).isTrue
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NaN).isInfinite).isFalse
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NaN).isInfinite).isFalse
        assertThat(complex(Double.NaN, Double.POSITIVE_INFINITY).isInfinite).isFalse
        assertThat(complex(Double.NaN, Double.NEGATIVE_INFINITY).isInfinite).isFalse
        assertThat(complex(Double.NaN, Double.NaN).isInfinite).isFalse
    }

    @Test
    fun `test x`() {
        assertThat(real(0).x).isEqualTo(0.0)
        assertThat(real(4).x).isAbout(4.0)
        assertThat(real(-4).x).isAbout(-4.0)
        assertThat(real(Double.NaN).x).isNaN
        assertThat(real(Double.POSITIVE_INFINITY).x).isPosInf
        assertThat(real(Double.NEGATIVE_INFINITY).x).isNegInf
        assertThat(imaginary(0).x).isPosZero
        assertThat(imaginary(3).x).isPosZero
        assertThat(imaginary(-3).x).isPosZero
        assertThat(imaginary(Double.NaN).x).isPosZero
        assertThat(imaginary(Double.POSITIVE_INFINITY).x).isPosZero
        assertThat(imaginary(Double.NEGATIVE_INFINITY).x).isPosZero
        assertThat(complex(4, 3).x).isAbout(4.0)
        assertThat(complex(-4, 3).x).isAbout(-4.0)
        assertThat(complex(4, -3).x).isAbout(4.0)
        assertThat(complex(-4, -3).x).isAbout(-4.0)
        assertThat(complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).x).isPosInf
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).x).isPosInf
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).x).isNegInf
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY).x).isNegInf
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NaN).x).isPosInf
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NaN).x).isNegInf
        assertThat(complex(Double.NaN, Double.POSITIVE_INFINITY).x).isNaN
        assertThat(complex(Double.NaN, Double.NEGATIVE_INFINITY).x).isNaN
        assertThat(complex(Double.NaN, Double.NaN).x).isNaN
    }

    @Test
    fun `test y`() {
        assertThat(real(0).y).isPosZero
        assertThat(real(4).y).isPosZero
        assertThat(real(-4).y).isPosZero
        assertThat(real(Double.NaN).y).isPosZero
        assertThat(real(Double.POSITIVE_INFINITY).y).isPosZero
        assertThat(real(Double.NEGATIVE_INFINITY).y).isPosZero
        assertThat(imaginary(0).y).isPosZero
        assertThat(imaginary(3).y).isAbout(3.0)
        assertThat(imaginary(-3).y).isAbout(-3.0)
        assertThat(imaginary(Double.NaN).y).isNaN
        assertThat(imaginary(Double.POSITIVE_INFINITY).y).isPosInf
        assertThat(imaginary(Double.NEGATIVE_INFINITY).y).isNegInf
        assertThat(complex(4, 3).y).isAbout(3.0)
        assertThat(complex(-4, 3).y).isAbout(3.0)
        assertThat(complex(4, -3).y).isAbout(-3.0)
        assertThat(complex(-4, -3).y).isAbout(-3.0)
        assertThat(complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).y).isPosInf
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).y).isNegInf
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).y).isPosInf
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY).y).isNegInf
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NaN).y).isNaN
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NaN).y).isNaN
        assertThat(complex(Double.NaN, Double.POSITIVE_INFINITY).y).isPosInf
        assertThat(complex(Double.NaN, Double.NEGATIVE_INFINITY).y).isNegInf
        assertThat(complex(Double.NaN, Double.NaN).y).isNaN
    }

    @Test
    fun `test abs`() {
        assertThat(real(0).abs).isPosZero
        assertThat(real(4).abs).isAbout(4.0)
        assertThat(real(-4).abs).isAbout(4.0)
        assertThat(real(Double.NaN).abs).isNaN
        assertThat(real(Double.POSITIVE_INFINITY).abs).isPosInf
        assertThat(real(Double.NEGATIVE_INFINITY).abs).isPosInf
        assertThat(imaginary(0).abs).isPosZero
        assertThat(imaginary(3).abs).isAbout(3.0)
        assertThat(imaginary(-3).abs).isAbout(3.0)
        assertThat(imaginary(Double.NaN).abs).isNaN
        assertThat(imaginary(Double.POSITIVE_INFINITY).abs).isPosInf
        assertThat(imaginary(Double.NEGATIVE_INFINITY).abs).isPosInf
        assertThat(complex(4, 3).abs).isAbout(5.0)
        assertThat(complex(-4, 3).abs).isAbout(5.0)
        assertThat(complex(4, -3).abs).isAbout(5.0)
        assertThat(complex(-4, -3).abs).isAbout(5.0)
        assertThat(complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).abs).isPosInf
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).abs).isPosInf
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).abs).isPosInf
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY).abs).isPosInf
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NaN).abs).isPosInf
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NaN).abs).isPosInf
        assertThat(complex(Double.NaN, Double.POSITIVE_INFINITY).abs).isPosInf
        assertThat(complex(Double.NaN, Double.NEGATIVE_INFINITY).abs).isPosInf
        assertThat(complex(Double.NaN, Double.NaN).abs).isNaN
    }

    @Test
    fun `test abs2`() {
        assertThat(real(0).abs2).isPosZero
        assertThat(real(4).abs2).isAbout(16.0)
        assertThat(real(-4).abs2).isAbout(16.0)
        assertThat(real(Double.NaN).abs2).isNaN
        assertThat(real(Double.POSITIVE_INFINITY).abs2).isPosInf
        assertThat(real(Double.NEGATIVE_INFINITY).abs2).isPosInf
        assertThat(imaginary(0).abs2).isPosZero
        assertThat(imaginary(3).abs2).isAbout(9.0)
        assertThat(imaginary(-3).abs2).isAbout(9.0)
        assertThat(imaginary(Double.NaN).abs2).isNaN
        assertThat(imaginary(Double.POSITIVE_INFINITY).abs2).isPosInf
        assertThat(imaginary(Double.NEGATIVE_INFINITY).abs2).isPosInf
        assertThat(complex(4, 3).abs2).isAbout(25.0)
        assertThat(complex(-4, 3).abs2).isAbout(25.0)
        assertThat(complex(4, -3).abs2).isAbout(25.0)
        assertThat(complex(-4, -3).abs2).isAbout(25.0)
        assertThat(complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).abs2).isPosInf
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).abs2).isPosInf
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).abs2).isPosInf
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY).abs2).isPosInf
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NaN).abs2).isPosInf
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NaN).abs2).isPosInf
        assertThat(complex(Double.NaN, Double.POSITIVE_INFINITY).abs2).isPosInf
        assertThat(complex(Double.NaN, Double.NEGATIVE_INFINITY).abs2).isPosInf
        assertThat(complex(Double.NaN, Double.NaN).abs2).isNaN
    }

    @Test
    fun `test arg`() {
        assertThat(real(0).arg).isPosZero
        assertThat(real(4).arg).isPosZero
        assertThat(real(-4).arg).isAbout(Math.PI)
        assertThat(real(Double.NaN).arg).isNaN
        assertThat(real(Double.POSITIVE_INFINITY).arg).isPosZero
        assertThat(real(Double.NEGATIVE_INFINITY).arg).isAbout(Math.PI)
        assertThat(imaginary(0).arg).isPosZero
        assertThat(imaginary(3).arg).isAbout(Math.PI / 2)
        assertThat(imaginary(-3).arg).isAbout(-Math.PI / 2)
        assertThat(imaginary(Double.NaN).arg).isNaN
        assertThat(imaginary(Double.POSITIVE_INFINITY).arg).isAbout(Math.PI / 2)
        assertThat(imaginary(Double.NEGATIVE_INFINITY).arg).isAbout(-Math.PI / 2)
        assertThat(complex(4, 3).arg).isAbout(0.643501108793284)
        assertThat(complex(-4, 3).arg).isAbout(Math.PI - 0.643501108793284)
        assertThat(complex(4, -3).arg).isAbout(-0.643501108793284)
        assertThat(complex(-4, -3).arg).isAbout(-Math.PI + 0.643501108793284)
        assertThat(complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).arg).isAbout(Math.PI / 4)
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).arg).isAbout(-Math.PI / 4)
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).arg).isAbout(Math.PI * 3 / 4)
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY).arg).isAbout(-Math.PI * 3 / 4)
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NaN).arg).isNaN
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NaN).arg).isNaN
        assertThat(complex(Double.NaN, Double.POSITIVE_INFINITY).arg).isNaN
        assertThat(complex(Double.NaN, Double.NEGATIVE_INFINITY).arg).isNaN
        assertThat(complex(Double.NaN, Double.NaN).arg).isNaN

        // Special cases for branch cut and negative 0
        assertThat(complex(Double.POSITIVE_INFINITY, 4).arg).isPosZero
        assertThat(complex(4.0, -0.0).arg).isNegZero
        assertThat(complex(Double.POSITIVE_INFINITY, -4).arg).isNegZero
        assertThat(complex(Double.NEGATIVE_INFINITY, 4).arg).isAbout(Math.PI)
        assertThat(complex(-4.0, -0.0).arg).isAbout(-Math.PI)
        assertThat(complex(Double.NEGATIVE_INFINITY, -4).arg).isAbout(-Math.PI)
        assertThat(complex(-0.0, 1.0).arg).isAbout(Math.PI / 2)
        assertThat(complex(-0.0, -1.0).arg).isAbout(-Math.PI / 2)
        assertThat(complex(Double.NEGATIVE_INFINITY, -0.0).arg).isAbout(-Math.PI)
    }

    @Test
    fun `test conjugate`() {
        assertComplex(real(0).conjugate).x { isPosZero }.y { isNegZero }
        assertComplex(real(0).conjugate).x { isPosZero }.y { isNegZero }
        assertComplex(real(4).conjugate).x { isAbout(4.0) }.y { isNegZero }
        assertComplex(real(-4).conjugate).x { isAbout(-4.0) }.y { isNegZero }
        assertComplex(real(Double.NaN).conjugate).x { isNaN }.y { isNegZero }
        assertComplex(real(Double.POSITIVE_INFINITY).conjugate).x { isPosInf }.y { isNegZero }
        assertComplex(real(Double.NEGATIVE_INFINITY).conjugate).x { isNegInf }.y { isNegZero }
        assertComplex(imaginary(0).conjugate).x { isPosZero }.y { isNegZero }
        assertComplex(imaginary(3).conjugate).x { isPosZero }.y { isAbout(-3.0) }
        assertComplex(imaginary(-3).conjugate).x { isPosZero }.y { isAbout(3.0) }
        assertComplex(imaginary(Double.NaN).conjugate).x { isPosZero }.y { isNaN }
        assertComplex(imaginary(Double.POSITIVE_INFINITY).conjugate).x { isPosZero }.y { isNegInf }
        assertComplex(imaginary(Double.NEGATIVE_INFINITY).conjugate).x { isPosZero }.y { isPosInf }
        assertComplex(complex(4, 3).conjugate).x { isAbout(4.0) }.y { isAbout(-3.0) }
        assertComplex(complex(-4, 3).conjugate).x { isAbout(-4.0) }.y { isAbout(-3.0) }
        assertComplex(complex(4, -3).conjugate).x { isAbout(4.0) }.y { isAbout(3.0) }
        assertComplex(complex(-4, -3).conjugate).x { isAbout(-4.0) }.y { isAbout(3.0) }
        assertComplex(complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).conjugate).x { isPosInf }
            .y { isNegInf }
        assertComplex(complex(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).conjugate).x { isPosInf }
            .y { isPosInf }
        assertComplex(complex(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).conjugate).x { isNegInf }
            .y { isNegInf }
        assertComplex(complex(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY).conjugate).x { isNegInf }
            .y { isPosInf }
        assertComplex(complex(Double.POSITIVE_INFINITY, Double.NaN).conjugate).x { isPosInf }.y { isNaN }
        assertComplex(complex(Double.NEGATIVE_INFINITY, Double.NaN).conjugate).x { isNegInf }.y { isNaN }
        assertComplex(complex(Double.NaN, Double.POSITIVE_INFINITY).conjugate).x { isNaN }.y { isNegInf }
        assertComplex(complex(Double.NaN, Double.NEGATIVE_INFINITY).conjugate).x { isNaN }.y { isPosInf }
        assertComplex(complex(Double.NaN, Double.NaN).conjugate).x { isNaN }.y { isNaN }
    }

    @Test
    fun `test negative`() {
        assertComplex(real(0).negative).cpx(-0.0, -0.0)
        assertComplex(real(4).negative).cpx(-4.0, -0.0)
        assertComplex(real(-4).negative).cpx(4.0, -0.0)
        assertComplex(real(Double.NaN).negative).cpx(Double.NaN, -0.0)
        assertComplex(real(Double.POSITIVE_INFINITY).negative).x { isNegInf }.y { isNegZero }
        assertComplex(real(Double.NEGATIVE_INFINITY).negative).x { isPosInf }.y { isNegZero }
        assertComplex(imaginary(0).negative).cpx(-0.0, -0.0)
        assertComplex(imaginary(3).negative).cpx(-0.0, -3.0)
        assertComplex(imaginary(-3).negative).cpx(-0.0, 3.0)
        assertComplex(imaginary(Double.NaN).negative).cpx(-0.0, Double.NaN)
        assertComplex(imaginary(Double.POSITIVE_INFINITY).negative).x { isNegZero }.y { isNegInf }
        assertComplex(imaginary(Double.NEGATIVE_INFINITY).negative).x { isNegZero }.y { isPosInf }
        assertComplex(complex(4, 3).negative).cpx(-4.0, -3.0)
        assertComplex(complex(-4, 3).negative).cpx(4.0, -3.0)
        assertComplex(complex(4, -3).negative).cpx(-4.0, 3.0)
        assertComplex(complex(-4, -3).negative).cpx(4.0, 3.0)
        assertComplex(complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).negative).x { isNegInf }.y { isNegInf }
        assertComplex(complex(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).negative).x { isNegInf }.y { isPosInf }
        assertComplex(complex(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).negative).x { isPosInf }.y { isNegInf }
        assertComplex(complex(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY).negative).x { isPosInf }.y { isPosInf }
        assertComplex(complex(Double.POSITIVE_INFINITY, Double.NaN).negative).x { isNegInf }.y { isNaN }
        assertComplex(complex(Double.NEGATIVE_INFINITY, Double.NaN).negative).x { isPosInf }.y { isNaN }
        assertComplex(complex(Double.NaN, Double.POSITIVE_INFINITY).negative).x { isNaN }.y { isNegInf }
        assertComplex(complex(Double.NaN, Double.NEGATIVE_INFINITY).negative).x { isNaN }.y { isPosInf }
        assertComplex(complex(Double.NaN, Double.NaN).negative).x { isNaN }.y { isNaN }
    }

    @Test
    fun `test inverse`() {
        assertComplex(real(0).inverse).x { isNaN }.y { isNaN }
        assertComplex(real(4).inverse).cpx(0.25, 0.0)
        assertComplex(real(-4).inverse).cpx(-0.25, 0.0)
        assertComplex(real(Double.NaN).inverse).cpx(Double.NaN, 0.0)
        assertComplex(real(Double.POSITIVE_INFINITY).inverse).cpx(0.0, 0.0)
        assertComplex(real(Double.NEGATIVE_INFINITY).inverse).cpx(-0.0, 0.0)
        assertComplex(imaginary(0).inverse).cpx(Double.NaN, Double.NaN)
        assertComplex(imaginary(3).inverse).cpx(0.0, -1.0/3)
        assertComplex(imaginary(-3).inverse).cpx(0.0, 1.0/3)
        assertComplex(imaginary(Double.NaN).inverse).cpx(0.0, Double.NaN)
        assertComplex(imaginary(Double.POSITIVE_INFINITY).inverse).cpx(0.0, -0.0)
        assertComplex(imaginary(Double.NEGATIVE_INFINITY).inverse).cpx(0.0, 0.0)
        assertComplex(complex(4, 3).inverse).cpx(0.16, -0.12)
        assertComplex(complex(-4, 3).inverse).cpx(-0.16, -0.12)
        assertComplex(complex(4, -3).inverse).cpx(0.16, 0.12)
        assertComplex(complex(-4, -3).inverse).cpx(-0.16, 0.12)
        assertComplex(complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).inverse).x { isNaN }.y{ isNaN }
        assertComplex(complex(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).inverse).x { isNaN }.y{ isNaN }
        assertComplex(complex(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).inverse).x { isNaN }.y{ isNaN }
        assertComplex(complex(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY).inverse).x { isNaN }.y{ isNaN }
        assertComplex(complex(Double.POSITIVE_INFINITY, Double.NaN).inverse).x { isNaN }.y{ isNaN }
        assertComplex(complex(Double.NEGATIVE_INFINITY, Double.NaN).inverse).x { isNaN }.y{ isNaN }
        assertComplex(complex(Double.NaN, Double.POSITIVE_INFINITY).inverse).x { isNaN }.y{ isNaN }
        assertComplex(complex(Double.NaN, Double.NEGATIVE_INFINITY).inverse).x { isNaN }.y{ isNaN }
        assertComplex(complex(Double.NaN, Double.NaN).inverse).x { isNaN }.y{ isNaN }
    }

    @Test
    fun `test plus with neg zero`() {
        assertComplex(real(0.0) + imaginary(0.0)).x { isPosZero }.y { isPosZero }
        assertComplex(real(0.0) + imaginary(-0.0)).x { isPosZero }.y { isPosZero }
        assertComplex(real(-0.0) + imaginary(0.0)).x { isPosZero }.y { isPosZero }
        assertComplex(real(-0.0) + imaginary(-0.0)).x { isPosZero }.y { isPosZero }
    }

    @Test
    fun roots() {
        var re = -9
        while (re <= 9) {
            var im = -9
            while (im <= 9) {
                val z0: Complex = complex(re, im)
                IntStream.of(3, 7, 20, 1000).forEach { n: Int ->
                    val roots: List<Complex> = z0.roots(n)
                    roots.forEach { z -> assertComplex(z.pow(n.toDouble())).cpx(0.0, 0.0) }
                }
                im += 3
            }
            re += 3
        }

        assertThat(Complex.ZERO.roots(3)).containsExactly(Complex.ZERO)
        assertThat(real(Double.NaN).roots(3)).isEmpty()
        assertThat(real(Double.POSITIVE_INFINITY).roots(3)).isEmpty()
        assertThat(real(Double.NEGATIVE_INFINITY).roots(3)).isEmpty()
        assertThat(imaginary(Double.NaN).roots(3)).isEmpty()
        assertThat(imaginary(Double.POSITIVE_INFINITY).roots(3)).isEmpty()
        assertThat(imaginary(Double.NEGATIVE_INFINITY).roots(3)).isEmpty()
        assertThatThrownBy { Complex.ONE.roots(0) }.isInstanceOf(IllegalArgumentException::class.java)
        assertThatThrownBy { Complex.ONE.roots(-2) }.isInstanceOf(IllegalArgumentException::class.java)
        assertThatThrownBy { Complex.ONE.roots(Int.MAX_VALUE) }.isInstanceOf(IllegalArgumentException::class.java)
    }

    class ComplexAssert(actual: Complex) : AbstractAssert<ComplexAssert, Complex>(actual, Complex::class.java) {
        companion object {
            fun assertComplex(actual: Complex) = ComplexAssert(actual).describedAs("Complex(${actual.x}, ${actual.y})")
        }

        fun x(block: DoubleAssert.() -> Unit): ComplexAssert {
            block(DoubleAssert(actual.x).describedAs("${descriptionText()}.x"))
            return myself
        }

        fun y(block: DoubleAssert.() -> Unit): ComplexAssert {
            block(DoubleAssert(actual.y).describedAs("${descriptionText()}.y"))
            return myself
        }

        fun cpx(expX: Double, expY: Double) = x { isAbout(expX) }.y { isAbout(expY) }
    }
}
