package net.fwitz.math.binary;

import net.fwitz.math.binary.complex.Complex;

import java.io.Serializable;
import java.util.function.Function;

import static java.lang.Double.doubleToLongBits;

/**
 * A binary number has two components with some relationship that defines how they interact under elementary
 * operations.  These take the form {@code x + jy}, where the two components are linearly independent under
 * addition, but where multiplicative operations produce {@code j^2} terms that behave in some special way.
 * For example:
 * <ul>
 * <li><code>j&#xB2; = -1</code> &mdash; Complex numbers &mdash; {@code j} is normally called {@code i}</li>
 * <li><code>j&#xB2; = 0</code> &mdash; "Dual" numbers &mdash; {@code j} is normally called &#x3F5; (epsilon)</li>
 * <li><code>j&#xB2; = 1</code> &mdash; "Split-complex" or "hyperbolic" numbers</li>
 * </ul>
 * <p>
 * In each case, the standard arithmetic, exponential, logarithmic, trigonometric, and hyperbolic operations
 * can be extended into this new domain on the basis of the relationships derived from applying the Taylor
 * series expansions to the exponential function.  For complex numbers in particular, this leads to the incredibly
 * rich mathematics called Complex Analysis.
 * </p><p>
 * As all of these binary numbers share some common characteristics, it made sense to represent those capabilities
 * in the same way for each of them.
 * </p><p>
 * Terminology note: When speaking of binary numbers in general, the literature tends to prefer using either
 * &#x3F5; (epsilon) or {@code e}.  The former is problematic for coding and the latter would be too easily
 * confused with Euler's constant, so I have avoided them.  Using {@code i} would be confusing since it behaves
 * differently from the well known imaginary constant in the other systems.  Using {@code j} seemed easiest, as
 * that symbol is at least used in electrical engineering circles (where {@code i} could be confused with current)
 * and it is the normal symbol to use for split-complex numbers, anyway.
 * </p>
 */
public abstract class BinaryNumber<T extends BinaryNumber<T>> implements Serializable {
    /**
     * Constructs a new binary number of the same type, but with the values {@code x + jy}.
     * This factory method makes it easier for {@code BinaryNumber} to provide basic template implementations
     * for all of its subclasses.
     */
    protected abstract T z(double x, double y);

    /**
     * The "real" component of this binary number.
     */
    public abstract double x();

    /**
     * The "imaginary" component of this binary number.
     */
    public abstract double y();

    /**
     * Returns this binary number with its "real" component replaced by the given value.
     */
    public final T x(double x) {
        return z(x, y());
    }

    /**
     * Returns this binary number with its "imaginary" component replaced by the given value.
     */
    public final T y(double y) {
        return z(x(), y);
    }

    /**
     * The polar radius of this binary number.
     * This is a distance measurement of how far this value is from the origin.
     * In particular, this is the multiplier required to reach the point from a unit vector sharing the same
     * {@link #arg() argument}, ignoring problems like branch cuts that could make this impossible.
     */
    public abstract double abs();

    /**
     * The polar angle of this binary number.
     * The exact meaning of the value depends on the form that Euler's identity takes in this binary number
     * system.
     */
    public abstract double arg();

    /**
     * Returns the multiplicative inverse ("reciprocal") of this binary number, {@code 1/z}.
     */
    public abstract T inverse();

    //================================================================
    // Simple invariants
    //----------------------------------------------------------------

    @Override
    public final int hashCode() {
        return Double.hashCode(x()) * 31 + Double.hashCode(y());
    }

    @Override
    public final boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        BinaryNumber<?> other = (BinaryNumber<?>) o;
        return doubleToLongBits(x()) == doubleToLongBits(other.x()) &&
                doubleToLongBits(y()) == doubleToLongBits(other.y());
    }

    /**
     * Returns {@code true} if either {@link #x()} or {@link #y()} is {@link Double#NaN}.
     */
    public final boolean isNaN() {
        return Double.isNaN(x()) || Double.isNaN(y());
    }

    /**
     * Returns {@code true} if {@link #isNaN()} is {@code false} and <strong>either</strong> {@link #x()} or
     * {@link #y()} is {@link Double#isInfinite(double) infinite}.
     */
    public final boolean isInfinite() {
        if (Double.isInfinite(x())) {
            return !Double.isNaN(y());
        }

        return Double.isInfinite(y()) && !Double.isNaN(x());
    }

    /**
     * Returns the negative value of this binary number, {@code -z}.
     * For binary number {@code (x + jy)}, this yields {@code (-x - jy)}.
     */
    public final T negative() {
        return z(-x(), -y());
    }

    /**
     * Returns the conjugate of this binary number, which is its reflection across the "real" axis.
     * For binary number {@code (x + jy)}, this yields {@code (x - jy)}.
     */
    public final T conjugate() {
        return z(x(), -y());
    }

    /**
     * Returns this binary number rectified into the first quadrant by taking the absolute value of its
     * "real" and "imaginary" parts independently.
     *
     * @return <code>|x| + j|y|</code>
     */
    public final T rectify() {
        return z(RealMath.rabs(x()), RealMath.rabs(y()));
    }

    //================================================================
    // Addition and subtraction
    //----------------------------------------------------------------

    /**
     * Return the result of adding the "real" value {@code x} to this binary number.
     */
    public final T plus(double x) {
        return z(x() + x, y());
    }

    /**
     * Return the result of adding or subtracting the "real" value {@code x} to/from this binary number.
     * This is meant to be useful for alternating series, where a <code>-1<sup>k</sup></code> term can
     * more efficiently be expressed with a flipping {@code boolean}.
     */
    public final T plusOrMinus(double x, boolean negate) {
        return negate ? minus(x) : plus(x);
    }

    /**
     * Returns the result of adding the imaginary value {@code jy} to this binary number.
     */
    public final T plusY(double y) {
        return z(x(), y() + y);
    }

    /**
     * Returns the result of adding the binary number {@code c} to this binary number.
     */
    public final T plus(BinaryNumber<? extends T> c) {
        return z(x() + c.x(), y() + c.y());
    }

    /**
     * Return the result of adding or subtracting the complex value {@code c} to/from this binary number.
     * This is meant to be useful for alternating series, where a <code>-1<sup>k</sup></code> term can
     * more efficiently be expressed with a flipping {@code boolean}.
     */
    public final T plusOrMinus(BinaryNumber<? extends T> c, boolean negate) {
        return negate ? minus(c) : plus(c);
    }

    /**
     * Return the result of subtracting the "real" value {@code x} from this binary number.
     */
    public final T minus(double x) {
        return z(x() - x, y());
    }

    /**
     * Return the result of subtracting the "imaginary" value {@code jy} from this binary number.
     */
    public final T minusY(double y) {
        return z(x(), y() - y);
    }

    /**
     * Return the result of subtracting the binary number {@code c} from this binary number.
     */
    public final T minus(BinaryNumber<? extends T> c) {
        return z(x() - c.x(), y() - c.y());
    }

    //================================================================
    // Multiplication and Division
    //----------------------------------------------------------------

    /**
     * Returns the result of multiplying this binary number by the "real" value {@code x}.
     */
    public final T times(double x) {
        return z(x() * x, y() * x);
    }

    /**
     * Returns the result of multiplying this binary number by the "imaginary" value {@code j}.
     */
    public abstract T timesJ();

    /**
     * Returns the result of multiplying this binary number by the "imaginary" value {@code jy}.
     */
    public abstract T timesJ(double y);

    /**
     * Returns the result of multiplying this binary number by the "imaginary" value {@code -j}.
     */
    public abstract T timesNegativeJ();


    /**
     * Returns the result of multiplying this binary number by the binary number {@code c}.
     */
    public abstract T times(BinaryNumber<? extends T> c);


    /**
     * Returns the result of dividing this binary number by the "real" value {@code x}.
     */
    public final T div(double x) {
        return z(x() / x, y() / x);
    }

    /**
     * Returns the result of dividing this binary number by the "imaginary" value {@code j}.
     */
    public abstract T divJ();

    /**
     * Returns the result of dividing this binary number by the "imaginary" value {@code jy}.
     */
    public abstract T divJ(double y);

    /**
     * Returns the result of dividing this binary number by the "imaginary" value {@code -j}.
     */
    public abstract T divNegativeJ();

    /**
     * Returns the result of dividing this binary number by the binary number {@code c}.
     * The default implementation uses the {@link #inverse() multiplicative inverse} so that it
     * can delegate this operation to {@link #times(BinaryNumber)}.
     */
    public T div(BinaryNumber<? extends T> c) {
        return times(c.inverse());
    }


    //================================================================
    // Logarithmic
    //----------------------------------------------------------------

    /**
     * Returns the natural logarithm of this binary number's {@link #abs() absolute value}, {@code log |z|}.
     * The default implementation just delegates to {@link #abs()} and takes the natural logarithm of the result.
     */
    public double logabs() {
        return RealMath.rlog(abs());
    }

    /**
     * Returns the natural logarithm of this binary number, {@code log z}.
     */
    public abstract T log();

    /**
     * Protects region information when it would otherwise be lost so that the result is remapped to the expected
     * region when performing operations that would lose that information.
     * <p>
     * For example, to calculate {@code z^x}, {@link #pow(double)} normally takes the {@link #log()}, scales it
     * using {@link #times(double)}, then takes the {@link #exp()} to get the result.  This works fine for complex
     * numbers, but for dual and split-complex numbers, this result is always mapped to the primary region.
     * In both cases, {@code x} is forced to a positive value, and in the case of split-complex numbers, the value
     * will first be reflected across the line {@code y=x} if necessary to make {@code |x| > |y|}.  However, this
     * sort of operation should more properly remain within the region where it was performed.
     * </p><p>
     * The purpose of this method is to perform any such mappings to the primary region up front, call the mutating
     * function, then restore the appropriate region information afterwards.
     * </p>
     *
     * @param fn the function to decorate with project into region 1 and reverse projection to the original region
     * @return the result of mapping the value back to the original region after mutating it within the primary
     */
    public final T region1mapped(Function<? super T, ? extends T> fn) {
        Function<? super T, ? extends T> dual = region1dual();
        return dual.apply(fn.apply(dual.apply(self())));
    }

    /**
     * Examines this point to determine a self-inverse function that will project this point into
     * the primary region for this coordinate plane or back to where it came from.
     * <p>
     * The default implementation always uses {@link Function#identity()}.
     * </p>
     */
    public Function<? super T, ? extends T> region1dual() {
        return Function.identity();
    }

    /**
     * Returns the logarithm of this binary number in the given {@code base}.
     */
    public T logN(double base) {
        final double lnBase = 1.0 / RealMath.rlog(base);
        return z(x() * lnBase, y() * lnBase);
    }

    /**
     * Returns the logarithm of this binary number in the given {@code base}.
     */
    public T logN(T base) {
        return log().div(base.log());
    }


    //================================================================
    // Exponential
    //----------------------------------------------------------------

    /**
     * Returns the natural exponential of this binary number, {@code e^z}.
     */
    public abstract T exp();

    /**
     * Shorthand form of {@code z.times(z)} or {@code z.pow(2)}.
     *
     * @return {@code z^2}
     */
    public T pow2() {
        return times(this);
    }

    /**
     * Shorthand form of {@code z.times(z).times(z)} or {@code z.pow(3)}.
     *
     * @return {@code z^3}
     */
    public T pow3() {
        return times(this).times(this);
    }

    /**
     * Returns the result of raising this binary number to the given power, {@code z^a}.
     * <p>
     * The default implementation delegates to {@link #log()}, {@link #times(double)}, and {@link #exp()}  to
     * compute {@code z^a} as {@code e^(a log z)}, using {@link #region1mapped(Function)} to preserve the
     * original region.
     * </p>
     */
    public T pow(double a) {
        if (a == 0.0) {
            return z(1, 0);
        }
        if (a == 1.0) {
            return self();
        }
        if (a == -1.0) {
            return inverse();
        }
        if (x() == 0.0 && y() == 0.0) {
            return z(0, 0);
        }

        return region1mapped(z -> z.log().times(a).exp());
    }

    /**
     * Returns the result of raising this binary number to the given power, {@code z^c}.
     * The default implementation delegates to {@link #log()}, {@link #times(BinaryNumber)}, and {@link #exp()}  to
     * compute {@code z^c} as {@code e^(c log z)}.
     */
    public T pow(BinaryNumber<? extends T> c) {
        if (c.y() == 0) {
            return pow(c.x());
        }
        if (x() == 0.0 && y() == 0.0) {
            return z(0, 0);
        }

        return region1mapped(z -> z.log().times(c).exp());
    }

    /**
     * Returns the square root of this binary number.
     * The default implementation delegates to {@link #pow(double) pow(0.5)}.
     */
    public T sqrt() {
        return pow(0.5);
    }


    //================================================================
    // Trigonometric
    //----------------------------------------------------------------

    /**
     * Returns the sine of this binary number.
     */
    public abstract T sin();

    /**
     * Returns the cosine of this binary number.
     */
    public abstract T cos();

    /**
     * Returns the tangent of this binary number.
     * The default implementation delegates to {@link #sin()}, {@link #cos()}, and {@link #div(BinaryNumber)}
     * to calculate the tangent from its definition of {@code tan(z) = sin(z) / cos(z)}.
     */
    public T tan() {
        return sin().div(cos());
    }

    /**
     * Returns the cotangent of this binary number.
     * The default implementation delegates to {@link #sin()}, {@link #cos()}, and {@link #div(BinaryNumber)}
     * to calculate the cotangent from its definition of {@code cot(z) = cos(z) / sin(z)}.
     */
    public T cot() {
        return cos().div(sin());
    }

    /**
     * Returns the secant of this binary number.
     * The default implementation delegates to {@link #cos()} and {@link #inverse()}} to calculate the
     * secant from its definition of {@code sec(z) = 1 / cos(z)}.
     */
    public T sec() {
        return cos().inverse();
    }

    /**
     * Returns the cosecant of this binary number.
     * The default implementation delegates to {@link #sin()} and {@link #inverse()}} to calculate the
     * cosecant from its definition of {@code csc(z) = 1 / sin(z)}.
     */
    public T csc() {
        return sin().inverse();
    }

    //================================================================
    // Hyperbolic
    //----------------------------------------------------------------

    /**
     * Returns the hyperbolic sine of this binary number.
     */
    public abstract T sinh();

    /**
     * Returns the hyperbolic cosine of this binary number.
     */
    public abstract T cosh();

    /**
     * Returns the hyperbolic tangent of this binary number.
     * The default implementation delegates to {@link #sinh()}, {@link #cosh()}, and {@link #div(BinaryNumber)}
     * to calculate the hyperbolic tangent from its definition of {@code tanh(z) = sinh(z) / cosh(z)}.
     */
    public T tanh() {
        return sinh().div(cosh());
    }

    /**
     * Returns the hyperbolic cotangent of this binary number.
     * The default implementation delegates to {@link #sinh()}, {@link #cosh()}, and {@link #div(BinaryNumber)}
     * to calculate the hyperbolic cotangent from its definition of {@code coth(z) = cosh(z) / sinh(z)}.
     */
    public T coth() {
        return cosh().div(sinh());
    }

    /**
     * Returns the hyperbolic secant of this binary number.
     * The default implementation delegates to {@link #cosh()} and {@link #inverse()}} to calculate the
     * hyperbolic secant from its definition of {@code sech(z) = 1 / cosh(z)}.
     */
    public T sech() {
        return cosh().inverse();
    }

    /**
     * Returns the hyperbolic cosecant of this binary number.
     * The default implementation delegates to {@link #sinh()} and {@link #inverse()}} to calculate the
     * hyperbolic cosecant from its definition of {@code csch(z) = 1 / sinh(z)}.
     */
    public T csch() {
        return sinh().inverse();
    }

    /**
     * Forces this binary number to be interpreted as a complex number.
     */
    public Complex asComplex() {
        return Complex.complex(x(), y());
    }

    /**
     * Forces this binary number to be interpreted as a split-complex  (hyperbolic) number.
     */
    public Complex asSplitComplex() {
        return Complex.complex(x(), y());
    }

    // Fix broken type inference
    @SuppressWarnings("unchecked")
    private T self() {
        return (T) this;
    }
}
