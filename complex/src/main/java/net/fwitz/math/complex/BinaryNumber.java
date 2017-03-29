package net.fwitz.math.complex;

import java.io.Serializable;

import static java.lang.Double.doubleToLongBits;
import static net.fwitz.math.complex.RealMath.rabs;
import static net.fwitz.math.complex.RealMath.rlog;

/**
 * A binary number has two components with some relationship defines as to how they interact under elementary
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
     * Returns {@code true} if {@link #isNaN()} is {@code false} either {@link #x()} or {@link #y()} is
     * {@link Double#isInfinite(double) infinite}.
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
        return z(rabs(x()), rabs(y()));
    }

    //================================================================
    // Additional and subtraction
    //----------------------------------------------------------------

    /**
     * Return the result of adding the "real" value {@code x} to this binary number.
     */
    public final T plus(double x) {
        return z(x() + x, y());
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
    public abstract T timesY();

    /**
     * Returns the result of multiplying this binary number by the "imaginary" value {@code jy}.
     */
    public abstract T timesY(double y);

    /**
     * Returns the result of multiplying this binary number by the "imaginary" value {@code -j}.
     */
    public abstract T timesNegativeY();


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
    public abstract T divY();

    /**
     * Returns the result of dividing this binary number by the "imaginary" value {@code jy}.
     */
    public abstract T divY(double y);

    /**
     * Returns the result of dividing this binary number by the "imaginary" value {@code -j}.
     */
    public abstract T divNegativeY();

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
        return rlog(abs());
    }

    /**
     * Returns the natural logarithm of this binary number, {@code log z}.
     */
    public abstract T log();

    /**
     * Returns the logarithm of this binary number in the given {@code base}.
     */
    public T logN(double base) {
        final double lnBase = 1.0 / rlog(base);
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
     * Returns the result of raising this binary number to the given power, {@code z^x}.
     */
    public abstract T pow(double x);

    /**
     * Returns the result of raising this binary number to the given power, {@code z^c}.
     */
    public abstract T pow(BinaryNumber<? extends T> c);

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
}
