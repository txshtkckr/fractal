package net.fwitz.math.complex;

import static java.lang.Double.doubleToLongBits;
import static net.fwitz.math.complex.RealMath.rabs;
import static net.fwitz.math.complex.RealMath.ratanh;
import static net.fwitz.math.complex.RealMath.rcosh;
import static net.fwitz.math.complex.RealMath.rexp;
import static net.fwitz.math.complex.RealMath.rlog;
import static net.fwitz.math.complex.RealMath.rlog1p;
import static net.fwitz.math.complex.RealMath.rsinh;

public class SplitComplex {
    private static final SplitComplex NaN = new SplitComplex(Double.NaN, Double.NaN);
    private static final SplitComplex ZERO = new SplitComplex(0, 0);
    private static final SplitComplex ONE = new SplitComplex(1, 0);
    private static final SplitComplex UNIT_POS_1 = new SplitComplex(1, 0);
    private static final SplitComplex UNIT_POS_J = new SplitComplex(0, 1);
    private static final SplitComplex UNIT_NEG_1 = new SplitComplex(-1, 0);
    private static final SplitComplex UNIT_NEG_J = new SplitComplex(0, -1);

    private final double x;
    private final double y;

    private SplitComplex(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static SplitComplex splitComplex(double x, double y) {
        return new SplitComplex(x, y);
    }

    public boolean isNaN() {
        return Double.isNaN(x) || Double.isNaN(y);
    }

    public boolean isInfinite() {
        if (Double.isInfinite(x)) {
            return !Double.isNaN(y);
        }

        return Double.isInfinite(x) && !Double.isNaN(y);
    }

    @Override
    public int hashCode() {
        return Double.hashCode(x) * 31 + Double.hashCode(y) + 83;
    }

    public boolean equals(Object o) {
        return o == this || (o instanceof SplitComplex && equalTo((SplitComplex) o));
    }

    private boolean equalTo(SplitComplex c) {
        return doubleToLongBits(x) == doubleToLongBits(c.x) &&
                doubleToLongBits(y) == doubleToLongBits(c.y);
    }

    public double x() {
        return x;
    }

    public SplitComplex x(double x) {
        return new SplitComplex(x, y);
    }

    public double y() {
        return y;
    }

    public SplitComplex y(double y) {
        return new SplitComplex(x, y);
    }

    public SplitComplex plus(double x) {
        return new SplitComplex(this.x + x, y);
    }

    public SplitComplex plusJ(double y) {
        return new SplitComplex(x, this.y + y);
    }

    public SplitComplex plus(SplitComplex c) {
        return new SplitComplex(x + c.x, y + c.y);
    }


    public SplitComplex minus(double x) {
        return new SplitComplex(this.x - x, y);
    }

    public SplitComplex minusJ(double y) {
        return new SplitComplex(x, this.y - y);
    }

    public SplitComplex minus(SplitComplex c) {
        return new SplitComplex(x - c.x, y - c.y);
    }


    public SplitComplex times(double x) {
        return new SplitComplex(this.x * x, x * y);
    }

    public SplitComplex timesJ() {
        //noinspection SuspiciousNameCombination
        return new SplitComplex(y, x);
    }

    public SplitComplex timesNegativeJ() {
        return new SplitComplex(-y, -x);
    }

    public SplitComplex timesJ(double y) {
        return new SplitComplex(this.y * y, x * y);
    }

    public SplitComplex times(SplitComplex c) {
        return new SplitComplex(x * c.x + y * c.y, x * c.y + y * c.x);
    }

    public SplitComplex div(double x) {
        return new SplitComplex(this.x / x, y / x);
    }

    public SplitComplex divJ(double y) {
        return new SplitComplex(this.y / y, x / y);
    }

    public SplitComplex div(SplitComplex c) {
        return times(c.inverse());
    }


    public SplitComplex conjugate() {
        return new SplitComplex(x, -y);
    }

    public SplitComplex negative() {
        return new SplitComplex(-x, -y);
    }

    public SplitComplex inverse() {
        double modulus = modulus2();
        if (modulus == 0) {
            return NaN;
        }
        return new SplitComplex(x / modulus, -y / modulus);
    }

    public SplitComplex rectify() {
        return new SplitComplex(Math.abs(x), Math.abs(y));
    }

    /**
     * <strong>WARNING</strong>: See terminology note on {@link #modulus2()}!
     */
    public double abs() {
        return Math.sqrt(Math.abs(modulus2()));
    }

    // The naive approach is to return
    //     log(sqrt(abs(x * x - y + y)))
    // but this risks an excessive loss of precision for small values.  To prevent this, we rewrite the expression
    // with a substitution of variables.  First, signs do not matter, but since we need to take a square root and
    // and we're taking the different of two squares, we need to decide which one is smaller up front.  To keep the
    // structure of this as similar as possible to what we have in Complex.logabs(), let's make "a" smaller and
    // swap the terms inside the sqrt to take this into account so we can skip a call to abs().  We can then
    // restructure the expression as follows:
    //     log(sqrt(abs(a^2 - b^2))
    //     log(sqrt(b^2 - a^2))            [ a < b ]
    //     log(sqrt(b^2 - b^2 * (a/b)^2))  [ a^2 = b^2 * (a/b)^2 ]
    //     log(sqrt(b^2 - b^2 * u^2))      [ defining u = a/b ]
    //     log(sqrt(b^2 * (1 - u^2)))      [ factoring out b^2 within the sqrt ]
    //     log(b * sqrt(1 - u^2))          [ moving the b^2 outside the sqrt as b ]
    //     log(b) + log(sqrt(1 - u^2))     [ log(xy) = log(x) + log(y) ]
    //     log(b) + log((1 - u^2) ^ 0.5)   [ sqrt(x) = x^0.5 ]
    //     log(b) + 0.5 * log(1 - u^2)     [ log(x^y) = y * log(x) ]
    //     log(b) + 0.5 * logp1(-u^2)      [ logp1(x) = log(1 + x) ]
    // log1p yields its best results when values are small, so assign the smaller value to a to ensure that
    // u = a/b <= 1.  Of course, this last case is also a hazard.  If a == b, then this is effectively asking
    // us for the log of 0, which isn't defined.

    /**
     * Returns the natural logarithm of this split complex number's {@link #abs() absolute value}, {@code log |z|}.
     */
    public double logabs() {
        if (y == 0) {
            return rlog(rabs(x));
        }

        double absY = rabs(y);
        if (x == 0) {
            return rlog(absY);
        }

        double absX = rabs(x);
        if (absX > absY) {
            double u = absY / absX;
            return rlog(absX) + 0.5 * rlog1p(-u * u);
        } else if (absX == absY) {
            return Double.NEGATIVE_INFINITY;
        }

        double u = absX / absY;
        return rlog(absY) + 0.5 * rlog1p(-u * u);
    }

    /**
     * The modulus-squared for this point.
     * <p>
     * <strong>WARNING</strong>: The terminology for split-complex numbers is not well established and some of the
     * most commonly used conventions are very confusing.  For example, it is common in the literature to refer
     * to the value {@code X^2 - Y^2} as the "modulus", but this is confusing because when it comes to things like
     * the {@code r} to use for polar decomposition, the <strong>square root</strong> of (the absolute value of)
     * this value must be used.  Its meaning therefore parallels {@link Complex#abs2()}, not {@link Complex#abs()}.
     * </p><p>
     * Yet there is also a major difference in behavior as compared to {@code Complex.abs2()}: the value is negative
     * when {@code |y| > |x|}!  Calling this method {@code abs2()} is therefore obviously not a good idea.  So to
     * keep confusion to a minimum, this code uses the following conventions:
     * </p>
     * <ul>
     * <li>{@code abs} &mdash; used for the non-negative polar decomposition distance, which parallels
     * {@link Complex#abs()} in form and function</li>
     * <li>{@code abs2} &mdash; <em>avoided</em></li>
     * <li>{@code modulus} &mdash; <em>avoided</em></li>
     * <li>{@code modulus2} &mdash; (also "modulus-squared") used for the raw distance metric {@code X^2 - Y^2},
     * which parallels {@link Complex#abs2()} in form and function <strong>except that it can be
     * negative</strong>.</li>
     * </ul>
     *
     * @return the modulus-squared for this point.  <strong>WARNING</strong>: See terminology note above!
     */
    public double modulus2() {
        // x^2 - y^2, rearranged to minimize cancellation error
        return (x + y) * (x - y);
    }

    public SplitComplex exp() {
        // Ensure we don't end up with -0.0 for either part
        if (x == 0.0 && !Double.isNaN(y)) {
            return ZERO;
        }
        double r = Math.exp(x);

        //noinspection SuspiciousNameCombination
        return new SplitComplex(r * Math.cosh(y), y * Math.sinh(y));
    }

    public SplitComplex pow2() {
        return times(this);
    }

    public SplitComplex pow3() {
        return times(this).times(this);
    }

    public SplitComplex pow(double a) {
        if (a == 0.0) {
            return ONE;
        }
        if (a == 1.0) {
            return this;
        }
        if (a == -1.0) {
            return inverse();
        }
        if (x == 0.0 && y == 0.0) {
            return ZERO;
        }

        double r = rexp(logabs() * x);
        double theta = arg() * x;
        return polar(r, theta);
    }

    public SplitComplex pow(SplitComplex c) {
        if (c.y == 0) {
            return pow(c.x);
        }
        if (x == 0.0 && y == 0.0) {
            return ZERO;
        }

        // PRobably possible to shortcut this, like Complex.pow(Complex) does?
        return log().times(c).exp();
    }

    public SplitComplex log() {
        return new SplitComplex(logabs(), arg());
    }

    public double arg() {
        if (isNaN()) {
            return Double.NaN;
        }
        if (x == 0 && y == 0) {
            return y;
        }
        double absX = rabs(x);
        double absY = rabs(y);
        if (absX == absY) {
            return Double.NaN;
        }

        double slope = (absX < absY) ? (absX / absY) : (absY / absX);
        return ratanh(slope);
    }

    public SplitComplex unit() {
        if (isNaN()) {
            return NaN;
        }
        if (x == 0 && y == 0) {
            return ZERO;
        }
        double absX = rabs(x);
        double absY = rabs(y);
        if (absX < absY) {
            return (y > 0) ? UNIT_POS_J : UNIT_NEG_J;
        }
        if (absX > absY) {
            return (x > 0) ? UNIT_POS_1 : UNIT_NEG_1;
        }
        return NaN;
    }

    /**
     * @return the {@link Complex} dual for this split-complex number.
     * @see Complex#asSplitComplex()
     */
    public Complex asComplex() {
        return Complex.complex(x, y);
    }

    private static SplitComplex polar(double r, double theta) {
        return new SplitComplex(r * rcosh(theta), r * rsinh(theta));
    }
}
