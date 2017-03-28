package net.fwitz.math.complex;

import net.fwitz.math.complex.analysis.Erf;

import java.io.Serializable;
import java.util.stream.IntStream;

import static java.lang.Double.POSITIVE_INFINITY;
import static java.lang.Double.doubleToLongBits;
import static java.lang.Math.atan2;
import static java.lang.Math.copySign;
import static net.fwitz.math.complex.RealMath.rabs;
import static net.fwitz.math.complex.RealMath.racos;
import static net.fwitz.math.complex.RealMath.racosh;
import static net.fwitz.math.complex.RealMath.rasin;
import static net.fwitz.math.complex.RealMath.ratan;
import static net.fwitz.math.complex.RealMath.rcos;
import static net.fwitz.math.complex.RealMath.rcosh;
import static net.fwitz.math.complex.RealMath.rexp;
import static net.fwitz.math.complex.RealMath.rhypot;
import static net.fwitz.math.complex.RealMath.rlog;
import static net.fwitz.math.complex.RealMath.rlog1p;
import static net.fwitz.math.complex.RealMath.rsin;
import static net.fwitz.math.complex.RealMath.rsinh;
import static net.fwitz.math.complex.RealMath.rsqrt;
import static net.fwitz.math.complex.RealMath.rtanh;

/**
 * An immutable complex number.
 * <p>
 * Note that since these values are immutable, a preference has been given to terminology like
 * {@link #plus(Complex) plus} or {@link #times(Complex) times} that connotes an result yielding
 * operation as opposed to {@code add}, which might suggest that the operation is mutative and
 * and encourage mistakes.  As this class is immutable, none of its method will
 * </p>
 */
public class Complex implements Serializable {
    /**
     * The maximum number of roots that may be requested using {@link #roots(int)}.
     */
    public static final int MAX_ROOTS = 10000;
    private static final Complex[] NO_ROOTS = {};

    public static final Complex POSITIVE_RE_INFINITY = real(Double.POSITIVE_INFINITY);

    public static final Complex NaN = new Complex(Double.NaN, Double.NaN);

    public static final Complex ZERO = real(0.0);
    public static final Complex ONE = real(1.0);
    public static final Complex TWO = real(2.0);
    public static final Complex E = real(Math.E);
    public static final Complex PI = real(Math.PI);

    public static final Complex I = imaginary(1);

    private static final double A_CROSSOVER = 1.5;
    private static final double B_CROSSOVER = 0.6417;

    private static final double TWO_PI = Math.PI * 2.0;
    private static final double PI_OVER_2 = Math.PI * 0.5;
    private static final double LN_2 = rlog(2.0);
    private static final double LN_2_RECIP = 1.0 / LN_2;
    private static final double LN_10 = rlog(10.0);
    private static final double LN_10_RECIP = 1.0 / LN_10;
    private static final Complex ONE_OVER_2_I = imaginary(2).inverse();

    private final double re;
    private final double im;

    private Complex(double re, double im) {
        this.re = re;
        this.im = im;
    }

    @Override
    public int hashCode() {
        return Double.hashCode(re) * 31 + Double.hashCode(im) + 17;
    }

    public boolean equals(Object o) {
        return o == this || (o instanceof Complex && equalTo((Complex) o));
    }

    private boolean equalTo(Complex c) {
        return doubleToLongBits(re) == doubleToLongBits(c.re) &&
                doubleToLongBits(im) == doubleToLongBits(c.im);
    }

    //================================================================
    // Simple properties
    //----------------------------------------------------------------

    /**
     * Returns {@code true} if either {@link #re()} or {@link #im()} is {@link Double#NaN}.
     */
    public boolean isNaN() {
        return Double.isNaN(re) || Double.isNaN(im);
    }

    /**
     * Returns {@code true} if {@link #isNaN()} is {@code false} either {@link #re()} or {@link #im()} is
     * {@link Double#isInfinite(double) infinite}.
     */
    public boolean isInfinite() {
        if (Double.isInfinite(re)) {
            return !Double.isNaN(im);
        }

        return Double.isInfinite(im) && !Double.isNaN(re);
    }

    /**
     * Returns the real component of this complex number, commonly referred to as {@code Re(z)}.
     */
    public double re() {
        return re;
    }

    /**
     * Returns this complex number with its real component replaced by the given value.
     * The imaginary component is copied from this complex number without modification.
     */
    public Complex re(double x) {
        return new Complex(x, im);
    }

    /**
     * Returns the imaginary component of this complex number, commonly referred to as {@code Im(z)}.
     */
    public double im() {
        return im;
    }

    /**
     * Returns this complex number with its imaginary component replaced by the given value.
     * The real component is copied from this complex number without modification.
     */
    public Complex im(double y) {
        return new Complex(re, y);
    }

    /**
     * Returns the absolute value of this complex number, also known as its magnitude, {@code |z|}.
     * This is specified in terms of {@link Math#hypot(double, double)}, from which it follows that:
     * <ul>
     * <li>If either component is infinite, the result is positive infinity.</li>
     * <li>If either component is NaN (and neither component is infinite), the result is NaN.</li>
     * <li>Otherwise, the result is the square root of the sum of the squares of the two components.</li>
     * </ul>
     */
    public double abs() {
        return rhypot(re, im);
    }

    /**
     * Returns the square of this complex number's absolute value, {@code |z|^2}.
     * This is specified in the same terms as {@link #abs()}, except no square root is taken in the last case.
     */
    public double abs2() {
        // Need to explicitly check for either component being infinite to ensure that they override NaN as specified.
        if (Double.isInfinite(re) || Double.isInfinite(im)) {
            return Double.POSITIVE_INFINITY;
        }
        return re * re + im * im;
    }

    /**
     * Returns the argument of this complex number, {@code arg(z)}, with a range of {@code [-PI, PI]}.
     * The value is calculated as per the specification for {@link Math#atan2(double, double)}.
     * All behaviours regarding sign, infinities, and NaN values are given in its documentation.
     * This function has a branch cut along the negative real axis.
     */
    public double arg() {
        return atan2(im, re);
    }

    /**
     * Returns the negative value of this complex number, {@code -z}.
     * For complex number {@code (a + bi)}, this yields {@code (-a - bi)}.
     */
    public Complex negative() {
        return new Complex(-re, -im);
    }

    /**
     * Returns the complex conjugate of this complex number, which is its reflection across the real axis.
     * For complex number {@code (a + bi)}, this yields {@code (a - bi)}.
     */
    public Complex conjugate() {
        return new Complex(re, -im);
    }

    /**
     * Returns this complex number rectified into the first quadrant by taking the absolute value of its
     * real and imaginary parts independently.
     *
     * @return <code>|re| + |im|</code><em>i</em>
     */
    public Complex rectify() {
        return new Complex(rabs(re), rabs(im));
    }

    /**
     * Returns the multiplicative inverse ("reciprocal") of this complex number, {@code 1/z}.
     */
    public Complex inverse() {
        double re2 = re * re;
        double im2 = im * im;
        // Prevent real numbers from using -0.0 as the imaginary portion of the inverse, but
        // if both parts were +/-0, then make sure that they both end up NaN.
        if (im2 == 0.0) {
            return re2 == 0.0 ? NaN : real(1.0 / re);
        } else if (re2 == 0.0) {
            return imaginary(-1.0 / im);
        } else {
            // 1 / (a + bi)   [ multiply both top and bottom by the complex conjugate, (a - bi) ]
            // (a - bi) / (a^2 + b^2)
            double scale = re2 + im2;
            return new Complex(re / scale, -im / scale);
        }
    }


    //================================================================
    // Arithmetic 
    //----------------------------------------------------------------

    /**
     * Return the result of adding the real value {@code x} to this complex number.
     */
    public Complex plus(double x) {
        return new Complex(re + x, im);
    }

    /**
     * Returns the result of adding the imaginary value {@code y*i} to this complex number.
     */
    public Complex plusI(double y) {
        return new Complex(re, im + y);
    }

    /**
     * Returns the result of adding the complex value {@code c} to this complex number.
     */
    public Complex plus(Complex c) {
        return new Complex(re + c.re, im + c.im);
    }

    /**
     * Return the result of subtracting the real value {@code x} from this complex number.
     */
    public Complex minus(double x) {
        return new Complex(re - x, im);
    }

    /**
     * Return the result of subtracting the imaginary value {@code y*i} from this complex number.
     */
    public Complex minusI(double y) {
        return new Complex(re, im - y);
    }

    /**
     * Return the result of subtracting the complex value {@code c} from this complex number.
     */
    public Complex minus(Complex c) {
        return new Complex(re - c.re, im - c.im);
    }

    /**
     * Returns the result of multiplying this complex value by the real value {@code x}.
     */
    public Complex times(double x) {
        return new Complex(re * x, im * x);
    }

    /**
     * Returns the result of multiplying this complex value by the imaginary value {@code i}.
     * This convenience method will generally be more efficient than using the more general methods
     * {@link #timesI(double) timesI(1)} or {@link #times(Complex) times}({@link #I}).
     */
    public Complex timesI() {
        return new Complex(-im, re);
    }

    /**
     * Returns the result of multiplying this complex value by the imaginary value {@code y*i}.
     */
    public Complex timesI(double y) {
        return new Complex(-im * y, re * y);
    }

    /**
     * Returns the result of multiplying this complex value by the imaginary value {@code -i}.
     */
    public Complex timesNegativeI() {
        return new Complex(im, -re);
    }

    /**
     * Returns the result of multiplying this complex value by the complex value {@code c}.
     * This method uses the well-known and straight-forward approach of First Outside Inside Last distribution
     * of terms, then simplifying:
     * <ul>
     * <li>(a + bi)(c + di)</li>
     * <li>ac + adi + bci + bd(i^2)</li>
     * <li>ac - bd + adi + bci</li>
     * <li>(ac - bd) + i(ad + bc)</li>
     * </ul>
     */
    public Complex times(Complex c) {
        return times(this, c);
    }

    // The logic and rules applied here are taken loosely from the example in Annex G of the C11 standard
    // ("IEC 60559-compatible complex arithmetic"), but tweaked for Java's slightly different rules around
    // infinities and NaN values.
    private static Complex times(Complex z, Complex w) {
        double a = z.re;
        double b = z.im;
        double c = w.re;
        double d = w.im;
        double ac = a * c;
        double ad = a * d;
        double bc = b * c;
        double bd = b * d;
        double x = ac - bd;
        double y = ad + bc;

        if (Double.isNaN(x) || Double.isNaN(y)) {
            boolean recalc = false;

            if (Double.isInfinite(a) || Double.isInfinite(b)) {
                // "Box" infinity and change NaNs in the other factor to 0
                a = boxInf(a);
                b = boxInf(b);
                c = zeroOutNaN(c);
                d = zeroOutNaN(d);
                recalc = true;
            }

            if (Double.isInfinite(c) || Double.isInfinite(d)) {
                // "Box" infinity and change NaNs in the other factor to 0
                a = zeroOutNaN(a);
                b = zeroOutNaN(b);
                c = boxInf(c);
                d = boxInf(d);
                recalc = true;
            }

            // If we got a NaN, none of the inputs were infinite, but at least one intermediate term was infinite,
            // then we need to recover the infinite result by zeroing out NaNs.  (Otherwise, the NaN result is
            // correct and gets retained.)
            if (!recalc && (Double.isInfinite(ac)
                    || Double.isInfinite(ad)
                    || Double.isInfinite(bc)
                    || Double.isInfinite(bd))) {
                a = zeroOutNaN(a);
                b = zeroOutNaN(b);
                c = zeroOutNaN(c);
                d = zeroOutNaN(d);
                recalc = true;
            }

            if (recalc) {
                // recalc now that we have fixed things up
                x = Double.POSITIVE_INFINITY * (a * c - b * d);
                y = Double.POSITIVE_INFINITY * (a * d + b * c);
            }
        }

        return new Complex(x, y);
    }

    private static double boxInf(double x) {
        return copySign(Double.isInfinite(x) ? 1.0 : 0.0, x);
    }

    private static double zeroOutNaN(double x) {
        return Double.isNaN(x) ? copySign(0.0, x) : x;
    }


    /**
     * Returns the result of dividing this complex value by the real value {@code x}.
     */
    public Complex div(double x) {
        return new Complex(re / x, im / x);
    }

    /**
     * Returns the result of dividing this complex value by the imaginary value {@code i}.
     * Note that diving by {@code i} is mathematically exactly equivalent to
     * {@link #timesNegativeI() multiplying by -i}.
     */
    public Complex divI() {
        return timesNegativeI();
    }

    /**
     * Returns the result of dividing this complex value by the imaginary value {@code i}.
     * Note that diving by {@code -i} is mathematically exactly equivalent to
     * {@link #timesI() multiplying by i}.
     */
    public Complex divNegativeI() {
        return timesI();
    }

    /**
     * Divides this complex number by {@code c} and returns the result.
     * The algorithm used is from <cite>Smith, R. 1962. Complex division. Comm. ACM 5, 435</cite>:
     * <p>
     * If {@code |c| < |d|}, then:
     * </p>
     * <ul>
     * <li>(a + bi) / (c + di)</li>
     * <li>(a + bi)(c - di) / (c + di)(c - di)  [ Use complex conjugate to make denominator real ]</li>
     * <li>[ (ac + bd) + (bc - ad)i ] / (cc + dd)  [ Distribute multiplications ]</li>
     * <li>(ac + bd) / (cc + dd) + [ (bc - ad) / (cc + dd) ] i  [ Distribute division ]</li>
     * <li>(ac/d + b) / (cc/d + d) + [ (bc/d - a) / (cc/d + d) ] i  [ Divide everything by d ]</li>
     * <li>(au + b) / (cu + d) + [ (bu - a) / (cu + d) ] i  [ Substitute u = c/d ]</li>
     * </ul>
     * <p>
     * Otherwise, by the same reasoning and starting from after the division distribution, we have:
     * </p>
     * <ul>
     * <li>(ac + bd) / (cc + dd) + [ (bc - ad) / (cc + dd) ] i</li>
     * <li>(a + bd/c) / (c + dd/c) + [ (b - ad/c) / (c + dd/c) ] i  [ Divide everything by c ]</li>
     * <li>(a + bu) / (c + du) + [ (b - au) / (c + du) ] i  [ Substitute u = d/c ]</li>
     * </ul>
     * <p>
     * Dividing everything through by the larger term first minimizes the risk of overflow or loss of precision.
     * </p>
     */
    public Complex div(Complex c) {
        return div(this, c);
    }

    private static Complex div(Complex z, Complex w) {
        double absIm = rabs(w.im);
        if (absIm == 0.0) {
            return z.div(w.re);
        }

        double a = z.re;
        double b = z.im;
        double c = w.re;
        double d = w.im;

        double x, y, u, denom;
        double absRe = rabs(w.re);
        if (absRe < absIm) {
            u = w.re / w.im;
            denom = w.re * u + w.im;
            x = (a * u + b) / denom;
            y = (b * u - a) / denom;
        } else {
            u = w.im / w.re;
            denom = w.re + w.im * u;
            x = (a + b * u) / denom;
            y = (b - a * u) / denom;
        }

        // Recover infinities and zeros that computed as NaN+iNaN;
        // the only cases are nonzero/zero, infinite/finite, and finite/infinite, ...
        if (Double.isNaN(x) && Double.isNaN(y)) {
            if (denom == 0.0 && (!Double.isNaN(a) || !Double.isNaN(b))) {
                double inf = copySign(POSITIVE_INFINITY, c);
                x = inf * a;
                y = inf * b;
            } else if ((Double.isInfinite(a) || Double.isInfinite(b)) && Double.isFinite(c) && Double.isFinite(d)) {
                a = boxInf(a);
                b = boxInf(b);
                x = POSITIVE_INFINITY * (a * c + b * d);
                y = POSITIVE_INFINITY * (b * c - a * d);
            } else if ((Double.isInfinite(c) || Double.isInfinite(d)) && Double.isFinite(a) && Double.isFinite(b)) {
                c = boxInf(c);
                d = boxInf(d);
                x = 0.0 * (a * c + b * d);
                y = 0.0 * (b * c - a * d);
            }
        }

        return complex(x, y);
    }

    //================================================================
    // Logarithmic
    //----------------------------------------------------------------

    // The naive approach is to return
    //     rlog(sqrt(re * re + im + im))
    // but this risks an excessive loss of precision for small values.  To prevent this, we rewrite the expression
    // with a substitution of variables.  First, as signs and which component is which do not matter, let's assume
    // that neither value is 0 and name their absolute values a and b, without worrying just yet about which is which.
    // We can then restructure the expression as follows:
    //     log(sqrt(a^2 + b^2))
    //     log(sqrt(b^2 * (a/b)^2 + b^2))  [ a^2 = b^2 * (a/b)^2 ]
    //     log(sqrt(b^2 * u^2 + b^2))      [ defining u = a/b ]
    //     log(sqrt(b^2 * (u^2 + 1)))      [ factoring out b^2 within the sqrt ]
    //     log(b * sqrt(u^2 + 1))          [ moving the b^2 outside the sqrt as b ]
    //     log(b) + log(sqrt(u^2 + 1))     [ log(xy) = log(x) + log(y) ]
    //     log(b) + log((u^2 + 1) ^ 0.5)   [ sqrt(x) = x^0.5 ]
    //     log(b) + 0.5 * log(u^2 + 1)     [ log(x^y) = y * log(x) ]
    //     log(b) + 0.5 * logp1(u^2)       [ logp1(x) = log(1 + x) ]
    // log1p yields its best results when values are small, so assign the smaller value to a to ensure that
    // u = a/b <= 1.

    /**
     * Returns the natural logarithm of this complex number's {@link #abs() absolute value}, {@code log |z|}.
     */
    public double logabs() {
        if (im == 0.0) {
            return rlog(rabs(re));
        }

        double absIm = rabs(im);
        if (re == 0.0) {
            return rlog(absIm);
        }

        double absRe = rabs(re);
        if (absRe > absIm) {
            double u = absIm / absRe;
            return rlog(absRe) + 0.5 * rlog1p(u * u);
        }

        double u = absRe / absIm;
        return rlog(absIm) + 0.5 * rlog1p(u * u);
    }

    /**
     * Returns the natural logarithm of this complex number, {@code log z}.
     * All of the logarithmic functions provided here have a branch cut along the negative real axis.
     */
    public Complex log() {
        return new Complex(logabs(), arg());
    }

    /**
     * Returns the logarithm of this complex number in the given {@code base}.
     * All of the logarithmic functions provided here have a branch cut along the negative real axis.
     */
    public Complex logN(double base) {
        final double lnBase = 1.0 / rlog(base);
        return new Complex(re * lnBase, im * lnBase);
    }

    /**
     * Returns the logarithm of this complex number in the given {@code base}.
     * All of the logarithmic functions provided here have a branch cut along the negative real axis.
     */
    public Complex logN(Complex base) {
        return log().div(base.log());
    }

    /**
     * Returns the logarithm of this complex number in base 2.
     * This convenience method is equivalent to {@code logN(2)}, but likely to be slightly more efficient.
     * All of the logarithmic functions provided here have a branch cut along the negative real axis.
     */
    public Complex log2() {
        return new Complex(logabs() * LN_2_RECIP, arg() * LN_2_RECIP);
    }

    /**
     * Returns the logarithm of this complex number in base 10.
     * This convenience method is equivalent to {@code logN(10)}, but likely to be slightly more efficient.
     * All of the logarithmic functions provided here have a branch cut along the negative real axis.
     */
    public Complex log10() {
        return new Complex(logabs() * LN_10_RECIP, arg() * LN_10_RECIP);
    }


    //================================================================
    // Exponential
    //----------------------------------------------------------------

    /**
     * Returns the natural exponential of this complex number, {@code e^z}.
     */
    public Complex exp() {
        return polar(rexp(re), im);
    }

    /**
     * Returns the base 2 exponential of this complex number, {@code 2^z}.
     */
    public Complex exp2() {
        double r = rexp(LN_2 * re);
        double theta = LN_2 * im;
        return polar(r, theta);
    }

    /**
     * Shorthand form of {@code z.times(z)} or {@code z.pow(2)}.
     *
     * @return {@code z^2}
     */
    public Complex pow2() {
        return times(this);
    }

    /**
     * Shorthand form of {@code z.times(z).times(z)} or {@code z.pow(3)}.
     *
     * @return {@code z^3}
     */
    public Complex pow3() {
        return times(this).times(this);
    }

    /**
     * Returns the result of raising this complex number to the given power, {@code z^x}.
     * This function has a branch cut for {@code z} (this complex number) along the negative real axis.
     */
    public Complex pow(double x) {
        if (x == 0.0) {
            return ONE;
        }
        if (x == 1.0) {
            return this;
        }
        if (x == -1.0) {
            return inverse();
        }
        if (re == 0.0 && im == 0.0) {
            return ZERO;
        }

        double r = rexp(logabs() * x);
        double theta = arg() * x;
        return polar(r, theta);
    }

    /**
     * Returns the result of raising this complex number to the given power, {@code z^c}.
     */
    public Complex pow(Complex c) {
        if (c.im == 0.0) {
            return pow(c.re);
        }
        if (re == 0.0 && im == 0.0) {
            return ZERO;
        }

        double logr = logabs();
        double theta = arg();

        double rho = rexp(logr * c.re - c.im * theta);
        double beta = theta * c.re + c.im * logr;
        return polar(rho, beta);
    }

    /**
     * Returns the {@code n} roots of this complex number, ordered by increasing argument starting from the
     * positive real axis.  Specifically, this returns the distinct solutions {@code z} for the equation
     * {@code z^n = c}, where {@code c} is this complex number.
     * <p>
     * The number of roots to return must be at least one (in which case the array contains {@code this} complex
     * number as its only element).  The number may not be larger than {@link #MAX_ROOTS}.  Provided both of these
     * conditions are met, this method will always return an array with the appropriate number of entries.
     * </p>
     * <p>
     * If this complex number is {@link #ZERO}, then the return value is an array with zero as its only element.
     * If this complex number is {@link #isNaN() not-a-number} or {@link #isInfinite() infinite},
     * then an empty array is returned.
     * </p>
     *
     * @param n the exponent, and therefore also the number of roots to be found
     * @return an array of solutions, as described
     */
    public Complex[] roots(int n) {
        if (n < 1) {
            throw new IllegalArgumentException("n < 1: " + n);
        }
        if (isNaN() || isInfinite()) {
            return NO_ROOTS;
        }
        if (n == 1) {
            return new Complex[]{this};
        }
        if (n > MAX_ROOTS) {
            throw new IllegalArgumentException("n > " + MAX_ROOTS + ": " + n);
        }

        double r = rexp(logabs() / n);
        if (r == 0.0) {
            return new Complex[]{ZERO};
        }

        double theta0 = arg() / n;
        return IntStream.range(0, n)
                .mapToDouble(x -> x * TWO_PI / n + theta0)
                .map(Complex::positiveRealBranchCut)
                .sorted()
                .mapToObj(theta -> polar(r, theta))
                .toArray(Complex[]::new);
    }


    /**
     * Maps an angle theta expressed in radians to the range {@code [0, 2*PI)}.
     * Most of the functions in this class branch cut along the negative real axis, meaning that they use an
     * angle in the range {@code [-PI, PI]}, with special cases around {@code -0.0} determining when {@code -PI}
     * is preferred over {@code +PI}.  This function normalizes the range to {@code [0, 2*PI)}, as required for
     * sorting the roots returned by {@link #roots(int)}.
     *
     * @param theta the angle to normalize
     * @return the angle normalized to {@code [0, 2*PI)}, corresponding to a branch cut along the positive real axis
     */
    private static double positiveRealBranchCut(double theta) {
        theta = atan2(rsin(theta), rcos(theta));
        return (theta < 0.0) ? theta + TWO_PI : theta;
    }

    /**
     * Returns the complex square root of real argument {@code x}.
     * This is equivalent to {@link Math#sqrt(double)}, except that it returns imaginary roots for negative numbers.
     */
    public static Complex sqrt(double x) {
        return (x >= 0) ? real(rsqrt(x)) : imaginary(rsqrt(-x));
    }

    /**
     * Returns the complex square root of this complex number.
     * This convenience method is equivalent to {@link #pow(double) pow(0.5)}, except that it may
     * be more efficient and/or return more accurate results under certain conditions.
     * This complex function has a branch cut along the negative real axis.
     * <p>
     * The algorithm used comes from <cite>Friedland, P. 1967. Absolute value and square root of a
     * complex number. Comm. ACM 10, 665</cite>:
     * </p>
     * <ul>
     * <li>c + di = sqrt(a + bi)</li>
     * <li>t = sqrt((|a| + hypot(a, b)) / 2)
     * <li>If a >= 0:  c = t, d = b / 2t</li>
     * <li>Otherwise:  c = |b|/2t, d = sgn(b) * t</li>
     * </ul>
     */
    public Complex sqrt() {
        double y = rabs(im);
        if (y == 0.0) {
            return sqrt(re);
        }

        double x = rabs(re);
        if (x == 0.0) {
            return ZERO;
        }

        double t = rsqrt((x + rhypot(x, y)) / 2);
        if (re >= 0.0) {
            return new Complex(t, im / (2 * t));
        }
        return new Complex(y / (2 * t), copySign(t, im));
    }


    //================================================================
    // Trigonometric
    //----------------------------------------------------------------

    /**
     * Returns the sine of this complex value.
     */
    public Complex sin() {
        // Handle this special case to make sure we don't end up with -0.0i when dealing with real numbers.
        if (im == 0.0) {
            return real(rsin(re));
        }
        return new Complex(rsin(re) * rcosh(im), rcos(re) * rsinh(im));
    }

    /**
     * Returns the cosine of this complex value.
     */
    public Complex cos() {
        // Handle this special case to make sure we don't end up with -0.0i when dealing with real numbers.
        if (im == 0.0) {
            return real(rcos(re));
        }
        return new Complex(rcos(re) * rcosh(im), rsin(re) * rsinh(-im));
    }

    /**
     * Returns the tangent of this complex value.
     */
    public Complex tan() {
        double cosRe = rcos(re);
        double sinhIm = rsinh(im);
        double scale = cosRe * cosRe + sinhIm * sinhIm;

        if (rabs(im) < 1) {
            return new Complex(0.5 * rsin(2.0 * re) / scale, 0.5 * rsinh(2.0 * im) / scale);
        }

        double f = cosRe / sinhIm;
        f = 1 + f * f;
        return new Complex(0.5 * rsin(2.0 * re) / scale, 1.0 / (rtanh(im) * f));
    }

    /**
     * Returns the tangent of this complex value.
     * This convenience method is equivalent to {@link #tan()}.{@link #inverse()}.
     */
    public Complex cot() {
        return tan().inverse();
    }

    /**
     * Returns the secant of this complex value.
     * This convenience method is equivalent to {@link #cos()}.{@link #inverse()}.
     */
    public Complex sec() {
        return cos().inverse();
    }

    /**
     * Returns the cosecant of this complex value.
     * This convenience method is equivalent to {@link #sin()}.{@link #inverse()}.
     */
    public Complex csc() {
        return sin().inverse();
    }


    //================================================================
    // Inverse trigonometric
    //----------------------------------------------------------------
    // Note that "inverse" here is in reference to inverting the trigonometric
    // function, not a multiplicative inverse as for the inverse() method.
    // The overloading of this term is admittedly unhelpful...
    //----------------------------------------------------------------

    /**
     * Returns the complex inverse sine of the given real value.
     * This is equivalent to {@link Math#asin(double)}, except that its domain is ended by complex analysis.
     * This is also a convenience method equivalent to first forming a complex number and using {@link #asin()}
     * on that, except that this method is slightly more efficient.
     */
    public static Complex asin(double a) {
        if (rabs(a) <= 1.0) {
            return real(rasin(a));
        }
        if (a < 0.0) {
            return new Complex(-PI_OVER_2, racosh(-a));
        }
        return new Complex(PI_OVER_2, -racosh(a));
    }

    /**
     * Returns the inverse sine of this complex number.
     * This function has a branch cut on the real axis outside the interval {@code [-1, 1]}.
     * The algorithm used is from <cite>Hull, T. 1997. Implementing the Complex Arcsine and
     * Arccosine Functions Using Exception Handling. Comm. ACM 23, 3</cite>.
     */
    public Complex asin() {
        double y = rabs(im);
        if (y == 0.0) {
            return asin(re);
        }

        double x = rabs(re);
        double r = rhypot(x + 1.0, y);
        double s = rhypot(x - 1.0, y);
        double a = 0.5 * (r + s);
        double b = x / a;  // 0.5 * (r - s), avoiding risk of cancellation error
        double y2 = y * y;

        double zRe;
        if (b <= B_CROSSOVER) {
            zRe = rasin(b);
        } else if (x <= 1) {
            double scale = 0.5 * (a + x) * (y2 / (r + (x + 1.0)) + (s + (1.0 - x)));
            zRe = ratan(x / rsqrt(scale));
        } else {
            double apx = a + x;
            double scale = 0.5 * (apx / (r + (x + 1.0)) + apx / (s + (x - 1.0)));
            zRe = ratan(x / (y * rsqrt(scale)));
        }

        double zIm;
        if (a <= A_CROSSOVER) {
            double am1;
            if (x < 1) {
                am1 = 0.5 * (y2 / (r + (x + 1.0)) + y2 / (s + (1.0 - x)));
            } else {
                am1 = 0.5 * (y2 / (r + (x + 1.0)) + (s + (x - 1.0)));
            }
            zIm = rlog1p(am1 + rsqrt(am1 * (a + 1.0)));
        } else {
            zIm = rlog(a + rsqrt(a * a - 1.0));
        }

        return new Complex(copySign(zRe, re), copySign(zIm, im));
    }

    /**
     * Returns the complex inverse sine of the given real value.
     * This is equivalent to {@link Math#acos(double)}, except that its domain is ended by complex analysis.
     * This is also a convenience method equivalent to first forming a complex number and using {@link #acos()}
     * on that, except that this method is slightly more efficient.
     * The algorithm used is from <cite>Hull, T. 1997. Implementing the Complex Arcsine and
     * Arccosine Functions Using Exception Handling. Comm. ACM 23, 3</cite>.
     */
    public static Complex acos(double a) {
        if (rabs(a) <= 1.0) {
            return real(racos(a));
        }
        if (a < 0.0) {
            return new Complex(Math.PI, -racosh(-a));
        }
        return new Complex(0, racosh(a));
    }

    /**
     * Returns the inverse sine of this complex value.
     * This function has a branch cut on the real axis outside the interval {@code [-1, 1]}.
     */
    public Complex acos() {
        if (im == 0) {
            return acos(re);
        }

        double x = rabs(re);
        double y = rabs(im);
        double r = rhypot(x + 1.0, y);
        double s = rhypot(x - 1.0, y);
        double a = 0.5 * (r + s);
        double b = x / a;
        double y2 = y * y;

        double zRe;
        if (b <= B_CROSSOVER) {
            zRe = racos(b);
        } else if (x <= 1) {
            double scale = 0.5 * (a + x) * (y2 / (r + x + 1.0) + (s + (1.0 - x)));
            zRe = ratan(rsqrt(scale) / x);
        } else {
            double apx = a + x;
            double scale = 0.5 * (apx / (r + x + 1.0) + apx / (s + (x - 1.0)));
            zRe = ratan((y * rsqrt(scale)) / x);
        }

        double zIm;
        if (a <= A_CROSSOVER) {
            double am1;
            if (x < 1) {
                am1 = 0.5 * (y2 / (r + (x + 1.0)) + y2 / (s + (1.0 - x)));
            } else {
                am1 = 0.5 * (y2 / (r + (x + 1.0)) + (s + (x - 1.0)));
            }
            zIm = rlog1p(am1 + rsqrt(am1 * (a + 1.0)));
        } else {
            zIm = rlog(a + rsqrt(a * a - 1.0));
        }

        return new Complex(rabs(zRe), rabs(zIm));
    }

    /**
     * Returns the inverse tangent for this complex value.
     * This function has a branch cut on the imaginary axis outside the interval {@code [-i, i]}.
     */
    public Complex atan() {
        if (im == 0.0) {
            return real(ratan(re));
        }

        Complex iMinusZ = complex(-re, -im + 1);
        Complex iPlusZ = complex(re, im + 1);
        return iMinusZ.div(iPlusZ).log().times(ONE_OVER_2_I);
    }

    /**
     * Returns the inverse cotangent (or arc-cotangent) of this complex number.
     * This convenience method is equivalent to <code>{@link #inverse().{@link #atan()}</code>.
     */
    public Complex acot() {
        return inverse().atan();
    }

    /**
     * Returns the inverse secant (or arc-secant) of this complex number.
     * This convenience method is equivalent to <code>{@link #inverse()}.{@link #acos()}</code>.
     */
    public Complex asec() {
        return inverse().acos();
    }

    /**
     * Returns the inverse cosecant (or arc-cosecant) of this complex number.
     * This convenience method is equivalent to <code>{@link #inverse()}.{@link #asin()}</code>.
     */
    public Complex acsc() {
        return inverse().asin();
    }


    //================================================================
    // Hyperbolic
    //----------------------------------------------------------------

    /**
     * Returns the hyperbolic sine of this complex number.
     */
    public Complex sinh() {
        return new Complex(rsinh(re) * rcos(im), rcosh(re) * rsin(im));
    }

    /**
     * Returns the hyperbolic cosine of this complex number.
     */
    public Complex cosh() {
        return new Complex(rcosh(re) * rcos(im), rsinh(re) * rsin(im));
    }

    /**
     * Returns the hyperbolic tangent of this complex number.
     */
    public Complex tanh() {
        double cosIm = rcos(im);
        double sinhRe = rsinh(re);
        double scale = cosIm * cosIm + sinhRe * sinhRe;

        if (rabs(re) < 1.0) {
            return new Complex(sinhRe * rcosh(re) / scale, 0.5 * rsin(2.0 * im) / scale);
        }

        double f = cosIm / sinhRe;
        f = 1.0 + f * f;
        return new Complex(1 / (rtanh(re) * f), 0.5 * rsin(2.0 * im) / scale);
    }

    /**
     * Returns the inverse hyperbolic cotangent of this complex number.
     * This convenience method is equivalent to <code>{@link #tanh()}.{@link #inverse()}</code>.
     */
    public Complex coth() {
        return tanh().inverse();
    }

    /**
     * Returns the inverse hyperbolic secant of this complex number.
     * This convenience method is equivalent to <code>{@link #cosh()}.{@link #inverse()}</code>.
     */
    public Complex sech() {
        return cosh().inverse();
    }

    /**
     * Returns the inverse hyperbolic cosecant of this complex number.
     * This convenience method is equivalent to <code>{@link #sinh()}.{@link #inverse()}</code>.
     */
    public Complex csch() {
        return sinh().inverse();
    }


    //================================================================
    // Inverse Hyperbolic
    //----------------------------------------------------------------
    // Note that "inverse" here is in reference to inverting the hyperbolic
    // function, not a multiplicative inverse as for the inverse() method.
    // The overloading of this term is admittedly unhelpful...
    //----------------------------------------------------------------

    /**
     * Returns the inverse hyperbolic sine of this complex number.
     * This function has a branch cut on the imaginary axis outside the range {@code [-i, i]}.
     */
    public Complex asinh() {
        return timesI().asin().timesNegativeI();
    }

    /**
     * Returns the inverse hyperbolic cosine of this complex number.
     * This function has a branch cut for values less than 1 on the real axis.
     */
    public Complex acosh() {
        if (im == 0) {
            return acosh(re);
        }
        Complex z = acos();
        return (z.im > 0) ? z.timesNegativeI() : z.timesI();
    }

    /**
     * Returns the complex inverse hyperbolic cosine of the given real value.
     * This convenience method is equivalent to first forming a complex number and using {@link #acosh()}
     * on that, except that this method is slightly more efficient.
     */
    public static Complex acosh(double a) {
        if (a >= 1.0) {
            return real(racosh(a));
        }
        if (a >= -1.0) {
            return new Complex(0.0, racos(a));
        }
        return new Complex(racosh(-a), Math.PI);
    }

    /**
     * Returns the inverse hyperbolic tangent of this complex number.
     * This function has a branch cut on the real axis outside the range {@code [-1, 1]}.
     */
    public Complex atanh() {
        return timesI().atan().timesNegativeI();
    }

    /**
     * Returns the complex inverse hyperbolic tangent of the given real number.
     * This convenience method is equivalent to first forming a complex number and using {@link #atanh()}
     * on that.
     */
    public static Complex atanh(double a) {
        return real(a).atanh();
    }

    /**
     * Returns the inverse hyperbolic cotangent of this complex number.
     * This convenience method is equivalent to <code>{@link #inverse()}.{@link #tanh()}</code>.
     */
    public Complex acoth() {
        return inverse().tanh();
    }

    /**
     * Returns the inverse hyperbolic secant of this complex number.
     * This convenience method is equivalent to <code>{@link #inverse()}.{@link #cosh()}</code>.
     */
    public Complex asech() {
        return inverse().cosh();
    }

    /**
     * Returns the inverse hyperbolic cosecant of this complex number.
     * This convenience method is equivalent to <code>{@link #inverse()}.{@link #cosh()}</code>.
     */
    public Complex acsch() {
        return inverse().sinh();
    }

    /**
     * Projects this complex number onto a Riemann Sphere, in a strict sense that meets the normal
     * mathematical definition for this concept.  Specifically, this is the same as {@link #cproj()},
     * except that there is only one infinity {@code (+infinity, 0)} and only one zero {@code (0, 0)}
     * because the signs are discarded for values that meet these conditions.  However, if neither of
     * the mapping conditions is met, then negative zeroes are preserved.
     * <ol>
     * <li>If either the real or imaginary component is infinite, then {@link #POSITIVE_RE_INFINITY} is returned</li>
     * <li>If both the real and imaginary components are zero (with sign ignored), then {@link #ZERO} is returned</li>
     * <li>Otherwise, this complex number is returned unmodified</li>
     * </ol>
     */
    public Complex proj() {
        if (Double.isInfinite(re) || Double.isInfinite(im)) {
            return POSITIVE_RE_INFINITY;
        }
        if (re == 0.0 && im == 0.0) {
            return ZERO;
        }
        return this;
    }

    /**
     * Projects this complex number onto a Riemann Sphere, in the same sense as for the
     * {@code cproj(3)} C function.  Specifically, this is the same as the mathematical
     * definition of the Riemann Sphere, except that it has two infinities and 4 zeros
     * due the preservation of the signs.
     * <ol>
     * <li>If either the real or imaginary component is infinite, then the result is a new complex number with
     * positive infinity as its real component and either positive or negative {@code 0} as its imaginary
     * component, preserving the existing sign from {@link #im()}.</li>
     * <li>Otherwise, this complex number is returned unmodified</li>
     * </ol>
     */
    public Complex cproj() {
        if (Double.isInfinite(re) || Double.isInfinite(im)) {
            return new Complex(Double.POSITIVE_INFINITY, copySign(0.0, im));
        }
        return this;
    }

    /**
     * Factory method that promotes a simple real number to a complex number with real component {@code re} and
     * imaginary component {@code 0}.
     *
     * @param re the real component of the complex number
     * @return {@code re} as a complex number
     */
    public static Complex real(double re) {
        return new Complex(re, 0.0);
    }

    /**
     * Factory method that promotes a simple real number to a complex number with real component {@code 0} and
     * imaginary component {@code im}.
     *
     * @param im the imaginary component of the complex number
     * @return {@code im * i} as a complex number
     */
    public static Complex imaginary(double im) {
        return new Complex(0.0, im);
    }

    /**
     * Factory method for constructing a complex number from its real and imaginary rectangular components.
     *
     * @param re the real (or "x") component
     * @param im the imaginary (or "y") component
     * @return the resulting complex number
     */
    public static Complex complex(double re, double im) {
        return new Complex(re, im);
    }

    /**
     * Factory method for constructing a complex number from its radial and angular polar coordinate components.
     *
     * @param r     the radial distance of the complex number from the origin; negative values are accepted and behave
     *              consistently (that is, {@code polar(5, 0)} should be approximately equivalent to
     *              {@code polar(-5, PI)}.
     * @param theta the angular rotation counter-clockwise relative to the positive real axis.  The angle's permissible
     *              domain is not arbitrarily restricted.
     * @return the complex number corresponding the given polar coordinates.
     */
    public static Complex polar(double r, double theta) {
        // Ensure we don't end up with -0.0 for either part
        if (r == 0.0 && !Double.isNaN(theta)) {
            return ZERO;
        }
        return new Complex(r * rcos(theta), r * rsin(theta));
    }

    /**
     * @return the {@link SplitComplex} dual for this complex number.
     * @see SplitComplex#asComplex()
     */
    public SplitComplex asSplitComplex() {
        return SplitComplex.splitComplex(re, im);
    }

    // compute erfcx(z) = exp(z^2) erfz(z)
    private static Complex erfcx(Complex z, double relerr) {
        return w(z.timesI(), relerr);
    }

    private static Complex w(Complex z, double relerr) {
        return Erf.w(z, relerr);
    }

    @Override
    public String toString() {
        if (Double.isNaN(im)) {
            return "(" + re + ' ' + im + "i)";
        }
        if (im < 0) {
            return "(" + re + im + "i)";
        }
        return "(" + re + '+' + im + "i)";
    }

    public static void main(String[] args) {
        System.out.println(ONE_OVER_2_I);
    }
}
