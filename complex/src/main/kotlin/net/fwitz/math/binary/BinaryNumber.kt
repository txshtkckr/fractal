package net.fwitz.math.binary

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.binary.split.SplitComplex
import java.io.Serializable
import java.util.function.Function
import kotlin.math.absoluteValue
import kotlin.math.ln
import kotlin.math.withSign

/**
 * A binary number has two components with some relationship that defines how they interact under elementary
 * operations.  These take the form `x + jy`, where the two components are linearly independent under
 * addition, but where multiplicative operations produce `j²` terms that behave in some special way.
 * In particular:
 *
 *  * `j² = -1` – Complex numbers  `j` is normally called `i`
 *  * `j² = 0` – Dual numbers  `j` is normally called ϵ (epsilon)
 *  * `j² = 1` – Split-complex or "hyperbolic" numbers
 *
 * In each case, the standard arithmetic, exponential, logarithmic, trigonometric, and hyperbolic operations
 * can be extended into this new domain on the basis of the relationships derived from applying the Taylor
 * series expansions to the exponential function.  For complex numbers in particular, this leads to the
 * incredibly rich mathematics called Complex Analysis.
 *
 * As all of these binary numbers share some common characteristics, it made sense to represent those capabilities
 * in the same way for each of them.
 *
 * Terminology note: When speaking of binary numbers in general, the literature tends to prefer using either
 * ϵ (epsilon) or `e`.  The former is problematic for coding and the latter would be too easily
 * confused with Euler's constant, so I have avoided them.  Using `i` would be confusing since it behaves
 * differently from the well known imaginary constant in the other systems.  Using `j` seemed easiest, as
 * that symbol is at least used in electrical engineering circles (where `i` could be confused with current)
 * and it is the symbol most commonly used for split-complex numbers, anyway.
 *
 */
@Suppress("unused")
abstract class BinaryNumber<T : BinaryNumber<T>>(
    /**
     * The "real" component of this binary number.
     */
    val x: Double,

    /**
     * The "imaginary" component of this binary number.
     */
    val y: Double
) : Serializable {
    companion object {
        fun boxInf(x: Double) = if (x.isInfinite()) 1.0 else 0.0.withSign(x)
        fun zeroOutNaN(x: Double) = if (x.isNaN()) 0.0.withSign(x) else x
    }

    /**
     * Constructs a new binary number of the same type, but with the values `x + jy`.
     * This factory method makes it easier for `BinaryNumber` to provide basic template implementations
     * for all of its subclasses.
     */
    protected abstract fun z(x: Double, y: Double): T

    /**
     * Returns this binary number with its "real" component replaced by the given value.
     */
    fun x(x: Double) = z(x, this.y)

    /**
     * Returns this binary number with its "imaginary" component replaced by the given value.
     */
    fun y(y: Double) = z(this.x, y)

    /**
     * The polar radius of this binary number.
     * This is a distance measurement of how far this value is from the origin.
     * In particular, this is the multiplier required to reach the point from a unit vector sharing the same
     * [argument][.arg], ignoring problems like branch cuts that could make this impossible.
     */
    abstract val abs: Double

    /**
     * The square of the polar radius of this binary number.
     * This value is always non-negative, even for split-complex numbers where this isn't otherwise obvious.
     */
    abstract val abs2: Double

    /**
     * The polar angle of this binary number.
     * The exact meaning of the value depends on the form that Euler's identity takes in this binary number
     * system.
     */
    abstract val arg: Double

    /**
     * Returns the multiplicative inverse ("reciprocal") of this binary number, `1/z`.
     */
    abstract val inverse: T

    //================================================================
    // Simple invariants
    //----------------------------------------------------------------
    override fun hashCode(): Int = x.hashCode() * 31 + y.hashCode()

    override fun equals(other: Any?): Boolean {
        if (other === this) {
            return true
        }
        if (other == null || javaClass != other.javaClass) return false
        other as BinaryNumber<*>
        return x.toRawBits() == other.x.toRawBits() && y.toRawBits() == other.y.toRawBits()
    }

    /**
     * Returns `true` if either [.x] or [.y] is [Double.NaN].
     */
    val isNaN get() = x.isNaN() || y.isNaN()

    /**
     * Returns `true` if [.isNaN] is `false` and **either** [.x] or
     * [.y] is [infinite][Double.isInfinite].
     */
    val isInfinite get() = if (x.isInfinite()) !y.isNaN() else y.isInfinite() && !x.isNaN()

    /**
     * Returns the negative value of this binary number, `-z`.
     * For binary number `(x + jy)`, this yields `(-x - jy)`.
     */
    val negative get() = -this
    operator fun unaryMinus() = z(-x, -y)

    /**
     * Returns the conjugate of this binary number, which is its reflection across the "real" axis.
     * For binary number `(x + jy)`, this yields `(x - jy)`.
     */
    val conjugate get() = z(x, -y)

    /**
     * Returns this binary number rectified into the first quadrant by taking the absolute value of its
     * "real" and "imaginary" parts independently.
     *
     * @return `|x| + j|y|`
     */
    val rectify get() = z(x.absoluteValue, y.absoluteValue)

    //================================================================
    // Addition and subtraction
    //----------------------------------------------------------------
    /**
     * Return the result of adding the "real" value `x` to this binary number.
     */
    operator fun plus(x: Double) = z(this.x + x, this.y)
    operator fun plus(x: Int) = plus(x.toDouble())

    /**
     * Return the result of adding or subtracting the "real" value `x` to/from this binary number.
     * This is meant to be useful for alternating series, where a `-1<sup>k</sup>` term can
     * more efficiently be expressed with a flipping `boolean`.
     */
    fun plusOrMinus(x: Double, negate: Boolean) = if (negate) minus(x) else plus(x)

    /**
     * Returns the result of adding the imaginary value `jy` to this binary number.
     */
    fun plusY(y: Double) = z(this.x, this.y + y)
    fun plusY(y: Int) = z(this.x, this.y + y)

    /**
     * Returns the result of adding the binary number `c` to this binary number.
     */
    operator fun plus(c: BinaryNumber<out T>) = z(this.x + c.x, this.y + c.y)

    /**
     * Return the result of adding or subtracting the complex value `c` to/from this binary number.
     * This is meant to be useful for alternating series, where a `-1<sup>k</sup>` term can
     * more efficiently be expressed with a flipping `boolean`.
     */
    fun plusOrMinus(c: BinaryNumber<out T>, negate: Boolean) = if (negate) minus(c) else plus(c)

    /**
     * Return the result of subtracting the "real" value `x` from this binary number.
     */
    operator fun minus(x: Double) = z(this.x - x, this.y)
    operator fun minus(x: Int) = z(this.x - x, this.y)

    /**
     * Return the result of subtracting the "imaginary" value `jy` from this binary number.
     */
    fun minusY(y: Double) = z(this.x, this.y - y)
    fun minusY(y: Int) = z(this.x, this.y - y)

    /**
     * Return the result of subtracting the binary number `c` from this binary number.
     */
    operator fun minus(c: BinaryNumber<out T>) = z(this.x - c.x, this.y - c.y)

    //================================================================
    // Multiplication and Division
    //----------------------------------------------------------------
    /**
     * Returns the result of multiplying this binary number by the "real" value `x`.
     */
    operator fun times(x: Double) = z(this.x * x, this.y * x)
    operator fun times(x: Int) = z(this.x * x, this.y * x)

    /**
     * Returns the result of multiplying this binary number by the "imaginary" value `j`.
     */
    abstract val timesJ: T

    /**
     * Returns the result of multiplying this binary number by the "imaginary" value `jy`.
     */
    abstract fun timesJ(y: Double): T
    fun timesJ(y: Int) = timesJ(y.toDouble())

    /**
     * Returns the result of multiplying this binary number by the "imaginary" value `-j`.
     */
    abstract val timesNegativeJ: T

    /**
     * Returns the result of multiplying this binary number by the binary number `c`.
     */
    abstract operator fun times(c: BinaryNumber<out T>): T

    /**
     * Returns the result of dividing this binary number by the "real" value `x`.
     */
    operator fun div(x: Double) = z(this.x / x, this.y / x)
    operator fun div(x: Int) = z(this.x / x, this.y / x)

    /**
     * Returns the result of dividing this binary number by the "imaginary" value `j`.
     */
    abstract val divJ: T

    /**
     * Returns the result of dividing this binary number by the "imaginary" value `jy`.
     */
    abstract fun divJ(y: Double): T
    fun divJ(y: Int) = divJ(y.toDouble())

    /**
     * Returns the result of dividing this binary number by the "imaginary" value `-j`.
     */
    abstract val divNegativeJ: T

    /**
     * Returns the result of dividing this binary number by the binary number `c`.
     * The default implementation uses the [multiplicative inverse][.inverse] so that it
     * can delegate this operation to [.times].
     */
    open operator fun div(c: BinaryNumber<out T>) = times(c.inverse)

    //================================================================
    // Logarithmic
    //----------------------------------------------------------------
    /**
     * Returns the natural logarithm of this binary number's [absolute value][.abs], `log |z|`.
     * The default implementation just delegates to [.abs] and takes the natural logarithm of the result.
     */
    open val logabs get() = ln(abs)

    /**
     * Returns the natural logarithm of this binary number, `log z`.
     */
    abstract val log: T

    /**
     * Protects region information when it would otherwise be lost so that the result is remapped to the expected
     * region when performing operations that would lose that information.
     *
     * For example, to calculate `z^x`, [.pow] normally takes the [.log], scales it
     * using [.times], then takes the [.exp] to get the result.  This works fine for complex
     * numbers, but for dual and split-complex numbers, this result is always mapped to the primary region.
     * In both cases, `x` is forced to a positive value, and in the case of split-complex numbers, the value
     * will first be reflected across the line `y=x` if necessary to make `|x| > |y|`.  However, this
     * sort of operation should more properly remain within the region where it was performed.
     *
     * The purpose of this method is to perform any such mappings to the primary region up front, call the mutating
     * function, then restore the appropriate region information afterwards.
     *
     * @param block the function to decorate with project into region 1 and reverse projection to the original region
     * @return the result of mapping the value back to the original region after mutating it within the primary
     */
    fun region1mapped(block: (T) -> T): T {
        val dual = region1dual
        return dual(block(dual(self)))
    }

    /**
     * Examines this point to determine a self-inverse function that will project this point into
     * the primary region for this coordinate plane or back to where it came from.
     *
     * The default implementation always uses [Function.identity].
     */
    open val region1dual: (T) -> T = { it }

    /**
     * Returns the logarithm of this binary number in the given `base`.
     */
    fun logN(base: Double) = times(1.0 / ln(base))

    /**
     * Returns the logarithm of this binary number in the given `base`.
     */
    fun logN(base: T) = log.div(base.log)

    //================================================================
    // Exponential
    //----------------------------------------------------------------
    /**
     * Returns the natural exponential of this binary number, `e^z`.
     */
    abstract val exp: T

    /**
     * Shorthand form of `z.times(z)` or `z.pow(2)`.
     *
     * @return `z^2`
     */
    val pow2 get() = times(this)

    /**
     * Shorthand form of `z.times(z).times(z)` or `z.pow(3)`.
     *
     * @return `z^3`
     */
    val pow3 get() = times(this).times(this)

    /**
     * Returns the result of raising this binary number to the given power, `z^a`.
     *
     * The default implementation delegates to [.log], [.times], and [.exp]  to
     * compute `z^a` as `e^(a log z)`, using [.region1mapped] to preserve the
     * original region.
     */
    open fun pow(a: Double): T = when {
        a == 0.0 -> z(1.0, 0.0)
        a == 1.0 -> self
        a == -1.0 -> inverse
        x == 0.0 && y == 0.0 -> z(0.0, 0.0)
        else -> region1mapped { z -> z.log.times(a).exp }
    }

    fun pow(a: Int): T = when {
        a == 0 -> z(1.0, 0.0)
        a == 1 -> self
        a == -1 -> inverse
        x == 0.0 && y == 0.0 -> z(0.0, 0.0)
        else -> region1mapped { z -> z.log.times(a).exp }
    }

    /**
     * Returns the result of raising this binary number to the given power, `z^c`.
     * The default implementation delegates to [.log], [.times], and [.exp]  to
     * compute `z^c` as `e^(c log z)`.
     */
    open fun pow(c: BinaryNumber<out T>) = when {
        c.y == 0.0 -> pow(c.x)
        x == 0.0 && y == 0.0 -> z(0.0, 0.0)
        else -> region1mapped { z -> z.log.times(c).exp }
    }

    /**
     * Returns the square root of this binary number.
     * The default implementation delegates to [pow(0.5)][.pow].
     */
    open val sqrt get() = pow(0.5)

    //================================================================
    // Trigonometric
    //----------------------------------------------------------------
    /**
     * Returns the sine of this binary number.
     */
    abstract val sin: T

    /**
     * Returns the cosine of this binary number.
     */
    abstract val cos: T

    /**
     * Returns the tangent of this binary number.
     * The default implementation delegates to [.sin], [.cos], and [.div]
     * to calculate the tangent from its definition of `tan(z) = sin(z) / cos(z)`.
     */
    open val tan: T get() = sin.div(cos)

    /**
     * Returns the cotangent of this binary number.
     * The default implementation delegates to [.sin], [.cos], and [.div]
     * to calculate the cotangent from its definition of `cot(z) = cos(z) / sin(z)`.
     */
    open val cot get() = cos.div(sin)

    /**
     * Returns the secant of this binary number.
     * The default implementation delegates to [.cos] and [.inverse]} to calculate the
     * secant from its definition of `sec(z) = 1 / cos(z)`.
     */
    open val sec get() = cos.inverse

    /**
     * Returns the cosecant of this binary number.
     * The default implementation delegates to [.sin] and [.inverse]} to calculate the
     * cosecant from its definition of `csc(z) = 1 / sin(z)`.
     */
    open val csc get() = sin.inverse

    //================================================================
    // Hyperbolic
    //----------------------------------------------------------------
    /**
     * Returns the hyperbolic sine of this binary number.
     */
    abstract val sinh: T

    /**
     * Returns the hyperbolic cosine of this binary number.
     */
    abstract val cosh: T

    /**
     * Returns the hyperbolic tangent of this binary number.
     * The default implementation delegates to [.sinh], [.cosh], and [.div]
     * to calculate the hyperbolic tangent from its definition of `tanh(z) = sinh(z) / cosh(z)`.
     */
    open val tanh get() = sinh.div(cosh)

    /**
     * Returns the hyperbolic cotangent of this binary number.
     * The default implementation delegates to [.sinh], [.cosh], and [.div]
     * to calculate the hyperbolic cotangent from its definition of `coth(z) = cosh(z) / sinh(z)`.
     */
    open val coth get() = cosh.div(sinh)

    /**
     * Returns the hyperbolic secant of this binary number.
     * The default implementation delegates to [.cosh] and [.inverse]} to calculate the
     * hyperbolic secant from its definition of `sech(z) = 1 / cosh(z)`.
     */
    open val sech get() = cosh.inverse

    /**
     * Returns the hyperbolic cosecant of this binary number.
     * The default implementation delegates to [.sinh] and [.inverse]} to calculate the
     * hyperbolic cosecant from its definition of `csch(z) = 1 / sinh(z)`.
     */
    open val csch get() = sinh.inverse

    /**
     * Forces this binary number to be interpreted as a complex number.
     */
    fun asComplex() = Complex(x, y)

    /**
     * Forces this binary number to be interpreted as a split-complex  (hyperbolic) number.
     */
    fun asSplitComplex() = SplitComplex(x, y)

    // Fix broken type inference
    @Suppress("UNCHECKED_CAST")
    private val self
        get() = this as T
}