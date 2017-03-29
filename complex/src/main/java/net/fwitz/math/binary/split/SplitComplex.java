package net.fwitz.math.binary.split;

import net.fwitz.math.binary.BinaryNumber;
import net.fwitz.math.binary.complex.Complex;

import static net.fwitz.math.binary.RealMath.rabs;
import static net.fwitz.math.binary.RealMath.ratanh;
import static net.fwitz.math.binary.RealMath.rcos;
import static net.fwitz.math.binary.RealMath.rcosh;
import static net.fwitz.math.binary.RealMath.rexp;
import static net.fwitz.math.binary.RealMath.rlog;
import static net.fwitz.math.binary.RealMath.rlog1p;
import static net.fwitz.math.binary.RealMath.rsin;
import static net.fwitz.math.binary.RealMath.rsinh;
import static net.fwitz.math.binary.RealMath.rtan;
import static net.fwitz.math.binary.RealMath.rtanh;

public class SplitComplex extends BinaryNumber<SplitComplex> {
    private static final SplitComplex NaN = new SplitComplex(Double.NaN, Double.NaN);
    private static final SplitComplex ZERO = new SplitComplex(0, 0);
    private static final SplitComplex ONE = new SplitComplex(1, 0);

    private final double x;
    private final double y;

    private SplitComplex(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public static SplitComplex splitComplex(double x, double y) {
        return new SplitComplex(x, y);
    }

    @Override
    protected SplitComplex z(double x, double y) {
        return new SplitComplex(x, y);
    }

    @Override
    public double x() {
        return x;
    }

    @Override
    public double y() {
        return y;
    }

    @Override
    public SplitComplex timesY() {
        //noinspection SuspiciousNameCombination
        return z(y, x);
    }

    @Override
    public SplitComplex timesY(double y) {
        //noinspection SuspiciousNameCombination
        return z(this.y * y, x * y);
    }

    @Override
    public SplitComplex timesNegativeY() {
        return z(-y, -x);
    }

    @Override
    public SplitComplex times(BinaryNumber<? extends SplitComplex> c) {
        return new SplitComplex(x * c.x() + y * c.y(), x * c.y() + y * c.x());
    }

    @Override
    public SplitComplex divY() {
        return timesY();
    }

    @Override
    public SplitComplex divY(double y) {
        return new SplitComplex(this.y / y, x / y);
    }

    @Override
    public SplitComplex divNegativeY() {
        return timesNegativeY();
    }

    @Override
    public SplitComplex inverse() {
        double modulus = modulus2();
        if (modulus == 0) {
            return NaN;
        }
        return new SplitComplex(x / modulus, -y / modulus);
    }

    /**
     * {@inheritDoc}
     * <p>
     * <strong>WARNING</strong>: See terminology note on {@link #modulus2()}!
     */
    public double abs() {
        return Math.sqrt(Math.abs(modulus2()));
    }

    // The naive approach is to return
    //     log(sqrt(abs(x * x - y + y)))
    // but this risks an excessive loss of precision for small values.  To prevent this, we rewrite the expression
    // with a substitution of variables.  First, signs do not matter, but since we need to take a square root and
    // and we'x taking the different of two squares, we need to decide which one is smaller up front.  To keep the
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

    @Override
    public SplitComplex exp() {
        // Ensure we don't end up with -0.0 for either part
        if (x == 0.0 && !Double.isNaN(y)) {
            return ZERO;
        }
        double r = Math.exp(x);

        //noinspection SuspiciousNameCombination
        return new SplitComplex(r * Math.cosh(y), y * Math.sinh(y));
    }

    @Override
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

    @Override
    public SplitComplex pow(BinaryNumber<? extends SplitComplex> c) {
        if (c.y() == 0) {
            return pow(c.x());
        }
        if (x == 0.0 && y == 0.0) {
            return ZERO;
        }

        // Probably possible to shortcut this, like Complex.pow(Complex) does?
        return log().times(c).exp();
    }

    @Override
    public SplitComplex log() {
        return new SplitComplex(logabs(), arg());
    }

    // From the taylor expansion of sin:
    //    sin(a) = a - a^3/3! + a^5/5! - a^7/7! + ...
    //    sin(jy) = jy - (jy)^3/3! + (jy)^5/5! - (jy)^7/7! + ...   [ substitute a = jy ]
    //    sin(jy) = jy - j(y^3)/3! + j(y^5)/5! - j(y^7)/7! + ...   [ j^n = j for any odd n ]
    //    sin(jy) = j[y - y^3/3! + y^5/5! - y^7/7! + ...           [ factor out j ]
    //    sin(jy) = j sin y                                        [ def. of sin ]
    // Doing the same for cos, we find that it has only even powers, so all terms use an even power of j,
    // which is just 1, with the result that:
    //    cos(jy) = cos y
    // Then we can apply the angle addition formula:
    //    sin(a + b) = sin(a) cos(b) + cos(a) sin(b)
    //    sin(x + jy) = sin(x) cos(jy) + cos(x) sin(jy)            [ substitute a = x, b = jy ]
    //    sin(x + jy) = sin(x) cos(y) + j cos(x) sin(y)            [ sin(jy) = j sin(y), cos(jy) = cos(y) ]
    @Override
    public SplitComplex sin() {
        if (y == 0) {
            return splitComplex(rsin(x), 0);
        }
        if (x == 0) {
            return splitComplex(0, rsin(y));
        }
        return new SplitComplex(rsin(x) * rcos(y), rcos(x) * rsin(y));
    }

    // Reusing the ground work for sin(), we can apply the angle addition formula:
    //    cos(a + b) = cos(a) cos(b) - sin(a) sin(b)
    //    cos(x + jy) = cos(x) cos(jy) - sin(x) sin(jy)            [ substitute a = x, b = jy ]
    //    cos(x + jy) = cos(x) cos(y) - j sin(x) sin(y)            [ sin(jy) = j sin(y), cos(jy) = cos(y) ]
    @Override
    public SplitComplex cos() {
        if (y == 0) {
            return splitComplex(rcos(x), 0);
        }
        if (x == 0) {
            return splitComplex(rcos(y), 0);
        }
        return new SplitComplex(rcos(x) * rcos(y), -rsin(x) * rsin(y));
    }

    // Now we can work out how the tangent is affected:
    //    tan(a) = sin(a) / cos(a)
    //    tan(jy) = sin(jy) / cos(jy)                              [ substitute a = jy ]
    //    tan(jy) = j sin(y) / cos(y)                              [ sin(jy) = j sin(y), cos(jy) = cos(y) ]
    //    tan(jy) = j tan(y)                                       [ def. of tangent ]
    // The double angle formula is messy, so not messing with it for now.
    @Override
    public SplitComplex tan() {
        if (y == 0) {
            return splitComplex(rtan(x), 0);
        }
        if (x == 0) {
            return splitComplex(0, rtan(y));
        }

        // Inline sin and cos to avoid calculating sin and cos values multiple times
        double sx = rsin(x);
        double cx = rcos(x);
        double sy = rsin(y);
        double cy = rcos(y);
        SplitComplex numer = new SplitComplex(sx * cy, cx * sy);
        SplitComplex denom = new SplitComplex(cx * cy, -sx * sy);
        return numer.div(denom);
    }

    @Override
    public SplitComplex cot() {
        if (y == 0) {
            return splitComplex(1 / rtan(x), 0);
        }
        if (x == 0) {
            return splitComplex(0, 1 / rtan(y));
        }

        // Inline sin and cos to avoid calculating sin and cos values multiple times
        double sx = rsin(x);
        double cx = rcos(x);
        double sy = rsin(y);
        double cy = rcos(y);
        SplitComplex numer = new SplitComplex(cx * cy, -sx * sy);
        SplitComplex denom = new SplitComplex(sx * cy, cx * sy);
        return numer.div(denom);
    }


    // For sinh, the arguments are similar to those for sin and cos.
    // For sinh, all of the powers of j are odd, so there is a single common j that can be factored out, giving:
    //    sinh(jy) = j sinh(y)
    // For cosh, all of the powers of j are even, so all the j coefficients have no effect, giving:
    //    cosh(jy) = cosh(y)
    // Applying the angle addition formula gives us:
    //    sinh(a + b) = sinh(a) cosh(b) + cosh(a) sinh(b)
    //    sinh(x + jy) = sinh(x) cosh(jy) + cosh(x) sinh(jy)   [ Substitute a = x, b = jy ]
    //    sinh(x + jy) = sinh(x) cosh(y) + j cosh(x) sinh(y)   [ sinh(jy) = j sinh(y), cosh(jy) = cosh(y) ]
    @Override
    public SplitComplex sinh() {
        if (y == 0) {
            return splitComplex(rsinh(x), 0);
        }
        if (x == 0) {
            return splitComplex(0, rsinh(y));
        }
        return splitComplex(rsinh(x) * rcosh(y), rcosh(x) * rsinh(y));
    }

    // Applying the angle addition formula gives:
    //    cosh(a + b) = cosh(a) cosh(b) + sinh(a) sinh(b)
    //    cosh(x + jy) = cosh(x) cosh(jy) + sinh(x) sinh(jy)   [ Substitute a = x, b = jy ]
    //    cosh(x + jy) = cosh(x) cosh(y) + j sinh(x) sinh(y)   [ sinh(jy) = j sinh(y), cosh(jy) = cosh(y) ]
    @Override
    public SplitComplex cosh() {
        if (y == 0) {
            return splitComplex(rcosh(x), 0);
        }
        if (x == 0) {
            return splitComplex(rcosh(y), 0);
        }
        return splitComplex(rcosh(x) * rcosh(y), rsinh(x) * rsinh(y));
    }

    // Since you can factor j out of sinh and drop it from cosh, this implies that j can be factored out of tanh.
    // Not going beyond this for now, though.
    @Override
    public SplitComplex tanh() {
        if (y == 0) {
            return splitComplex(rtanh(x), 0);
        }
        if (x == 0) {
            return splitComplex(0, rtanh(y));
        }

        // Inline sinh and cosh to avoid calculating sinh and cosh values multiple times
        double sx = rsinh(x);
        double cx = rcosh(x);
        double sy = rsinh(y);
        double cy = rcosh(y);
        SplitComplex numer = new SplitComplex(sx * cy, cx * sy);
        SplitComplex denom = new SplitComplex(cx * cy, sx * sy);
        return numer.div(denom);
    }

    @Override
    public SplitComplex coth() {
        if (y == 0) {
            return splitComplex(1 / rtanh(x), 0);
        }
        if (x == 0) {
            return splitComplex(0, 1 / rtanh(y));
        }

        // Inline sinh and cosh to avoid calculating sinh and cosh values multiple times
        double sx = rsinh(x);
        double cx = rcosh(x);
        double sy = rsinh(y);
        double cy = rcosh(y);
        SplitComplex numer = new SplitComplex(cx * cy, sx * sy);
        SplitComplex denom = new SplitComplex(sx * cy, cx * sy);
        return numer.div(denom);
    }

    @Override
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

        double slope = (absX < absY) ? (x / y) : (y / x);
        return ratanh(slope);
    }

    public Classification classify() {
        if (isNaN()) {
            return Classification.NAN;
        }
        if (x == 0 && y == 0) {
            return Classification.ZERO;
        }
        double absX = rabs(x);
        double absY = rabs(y);
        if (absX > absY) {
            return (x > 0) ? Classification.POS_X : Classification.NEG_X;
        }
        if (absX < absY) {
            return (y > 0) ? Classification.POS_Y : Classification.NEG_Y;
        }
        return (x == y) ? Classification.POS_NULL_VECTOR : Classification.NEG_NULL_VECTOR;
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


    /**
     * Identifies the broad categorization of the point provided
     */
    public enum Classification {
        /**
         * The X value, Y value, or both were {@link Double#NaN}.
         */
        NAN(SplitComplex.NaN),

        /**
         * True zero (both X and Y have that value).
         */
        ZERO(SplitComplex.ZERO),

        /**
         * Not {@link #ZERO}, but on the line {@code y = x}.
         */
        POS_NULL_VECTOR(splitComplex(1, 1)),

        /**
         * Not {@link #ZERO}, but on the line {@code y = -x}.
         */
        NEG_NULL_VECTOR(splitComplex(1, -1)),

        /**
         * In the positive X-axis's quadrant, as divided by the lines {@code y = x} and {@code y = -x}.
         */
        POS_X(splitComplex(1, 0)),

        /**
         * In the negative X-axis's quadrant, as divided by the lines {@code y = x} and {@code y = -x}.
         */
        NEG_X(splitComplex(-1, 0)),

        /**
         * In the positive Y-axis's quadrant, as divided by the lines {@code y = x} and {@code y = -x}.
         */
        POS_Y(splitComplex(0, 1)),

        /**
         * In the negative Y-axis's quadrant, as divided by the lines {@code y = x} and {@code y = -x}.
         */
        NEG_Y(splitComplex(0, -1));

        private final SplitComplex basis;
        private final boolean nullVector;

        Classification(SplitComplex basis) {
            this.basis = basis;
            this.nullVector = basis.x() == basis.y() || basis.x() == -basis.y();
        }

        /**
         * A representative point that is included in this region.
         * With the exception of {@link #NAN}, all {@code basis()} values have X and Y coordinates of
         * that are {@code -1}, {@code 0}, or {@code 1}.
         */
        public SplitComplex basis() {
            return basis;
        }

        public boolean isNullVector() {
            return nullVector;
        }
    }
}
