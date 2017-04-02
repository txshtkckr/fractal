package net.fwitz.math.binary.split;

import net.fwitz.math.binary.BinaryNumber;
import net.fwitz.math.binary.complex.Complex;

import java.util.function.Function;

import static net.fwitz.math.binary.RealMath.rabs;
import static net.fwitz.math.binary.RealMath.ratanh;
import static net.fwitz.math.binary.RealMath.rcos;
import static net.fwitz.math.binary.RealMath.rcosh;
import static net.fwitz.math.binary.RealMath.rexp;
import static net.fwitz.math.binary.RealMath.rlog;
import static net.fwitz.math.binary.RealMath.rlog1p;
import static net.fwitz.math.binary.RealMath.rsin;
import static net.fwitz.math.binary.RealMath.rsinh;

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


    // See comments on abs() to get started.  This tells us that for the polar decomposition
    //    x + jy = r exp(ja)
    // We know that
    //    x = r cosh a
    //    y = r sinh a
    // From this we can get a as follows::
    //   y/x = (r sinh a) / (r cosh a)
    //   y/x = tanh a
    //     a = atanh(y/x)  [ take atanh of both sides, then swap ]
    @Override
    public double arg() {
        if (isNaN()) {
            return Double.NaN;
        }

        SplitComplex z = region1dual().apply(this);

        // If x is 0, then y must also be 0 or the region mapping would have swapped them.
        return (x == 0) ? 0 : ratanh(z.y() / z.x());
    }

    // What is 'r' for a polar coordinates conversion in split-complex numbers?
    // Assume that x + jy has a polar decomposition as r exp(ja).  Then:
    //     x + jy = r exp(ja)
    //     x + jy =  r [cosh a + j sinh a]
    // 1 and j are orthogonal, so separating the real and imaginary components gives:
    //     x = r cosh a
    //     y = r sinh a
    // To get r, we square both x and y, then subtract, using the identity that (cosh a)^2 - (sinh a)^2 = 1:
    //   x^2 = r^2 (cosh a)^2
    //   y^2 = r^2 (sinh a)^2
    //   r^2 = x^2 - y^2    [ swapped sides, factored out r^2, and used the identity ]
    //   r = sqrt(|x^2 - y^2|)

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
     * when {@code |y| > |x|}.  Calling this method {@code abs2()} is therefore obviously not a good idea.  So to
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
        return new SplitComplex(r * Math.cosh(y), r * Math.sinh(y));
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

        return region1mapped(z -> {
            double r = rexp(z.logabs() * a);
            double theta = z.arg() * a;
            return polar(r, theta);
        });
    }

    /**
     * {@inheritDoc}
     * ><p>
     * The function that is returned is self-inverting, but it is only valid for this point and other points
     * with the same {@link Classification Classification}.  Points in other regions will be mapped consistently,
     * but not to {@code REGION_1}.  For example, if {@code region1dual()} is called on the value {@code 2 - j5},
     * that means it is in {@link Classification#REGION_IV REGION_IV}, where negative {@code y} values dominate.
     * The returned function is then {@link SplitComplex#timesNegativeY()}, which reflects values over the line
     * {@code y = -x}, swapping regions {@code I} and {@code IV}.  Calling it on the original value will resul
     * in {@code 5 - 2j} (the values are swapped and negated).  Repeating this operation restores the original
     * value, {@code 2 - j5}.
     * </p><p>
     * However, this operation also swaps regions {@code II} and {@code III}, so if you hold onto the function
     * and call it on {@code -5 + 2j}, the result will be {@code -2 + 5j}, not {@code 5 - 2j} as would be
     * expected when mapping that value to {@code REGION_I} directly.
     * </p><p>
     * Note that any value that isn't in region {@code II}, {@code III}, or {@code IV} just returns
     * {@link Function#identity()}, as either the value is already in {@code I} or there is no way to
     * map it there.
     * </p>
     */
    public Function<SplitComplex, SplitComplex> region1dual() {
        // For speed, test directly instead of using classify()
        if (y > x) {
            if (y > -x) {
                // REGION_II
                return SplitComplex::timesY;
            }
            if (y < -x) {
                // REGION_III
                return SplitComplex::negative;
            }
        } else if (y < x) {
            if (y < -x) {
                // REGION_IV
                return SplitComplex::timesNegativeY;
            }
        }

        // Already in REGION_I, or something else that can't be mapped onto it
        return Function.identity();
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
        return new SplitComplex(rsin(x) * rcos(y), rcos(x) * rsin(y));
    }

    // Reusing the ground work for sin(), we can apply the angle addition formula:
    //    cos(a + b) = cos(a) cos(b) - sin(a) sin(b)
    //    cos(x + jy) = cos(x) cos(jy) - sin(x) sin(jy)            [ substitute a = x, b = jy ]
    //    cos(x + jy) = cos(x) cos(y) - j sin(x) sin(y)            [ sin(jy) = j sin(y), cos(jy) = cos(y) ]
    @Override
    public SplitComplex cos() {
        return new SplitComplex(rcos(x) * rcos(y), -rsin(x) * rsin(y));
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
        return splitComplex(rsinh(x) * rcosh(y), rcosh(x) * rsinh(y));
    }

    // Applying the angle addition formula gives:
    //    cosh(a + b) = cosh(a) cosh(b) + sinh(a) sinh(b)
    //    cosh(x + jy) = cosh(x) cosh(jy) + sinh(x) sinh(jy)   [ Substitute a = x, b = jy ]
    //    cosh(x + jy) = cosh(x) cosh(y) + j sinh(x) sinh(y)   [ sinh(jy) = j sinh(y), cosh(jy) = cosh(y) ]
    @Override
    public SplitComplex cosh() {
        return splitComplex(rcosh(x) * rcosh(y), rsinh(x) * rsinh(y));
    }

    /**
     * @see Classification
     */
    public Classification classify() {
        if (y > -x) {
            if (y < x) {
                return Classification.REGION_I;
            }
            if (y > x) {
                return Classification.REGION_II;
            }
        } else if (y < -x) {
            if (y > x) {
                return Classification.REGION_III;
            }
            if (y < x) {
                return Classification.REGION_IV;
            }
        }

        if (isNaN()) {
            return Classification.NAN;
        }

        // We are on y = x or y = -x
        if (x == 0) {
            return Classification.ZERO;
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
     * Identifies the broad categorization of the point provided.
     * <p>
     * The hyperbolic plane is divided into for regions by the null vector axes y = x and y = -x
     * </p>
     * <pre>
     *    \  II   / (positive null vectors)
     *     \     / y = x
     *      \   /
     *       \ /
     *  III   0   I
     *       / \
     *      /   \
     *     /     \ y = -x
     *    /  IV   \ (negative null vectors)
     *
     *  </pre>
     * <p>
     * The following tests are used to determine which region the point is in:
     * </p>
     * <ul>
     * <li><strong>{@code y > -x}</strong> &mdash; to the upper-right of {@code y = -x}, so in {@link #REGION_I} or
     * {@link #REGION_II} (or on {@link #POS_NULL_VECTOR y = x} between them).</li>
     * <li><strong>{@code y > x}</strong> &mdash; to the upper-left of {@code y = x}, so in {@link #REGION_II} or
     * {@link #REGION_III} (or on {@link #NEG_NULL_VECTOR y = -x} between them).</li>
     * <li><strong>{@code y < -x}</strong> &mdash; to the lower-left of {@code y = -x}, so in {@link #REGION_III} or
     * {@link #REGION_IV} (or on {@link #POS_NULL_VECTOR y = x} between them).</li>
     * <li><strong>{@code y < x}</strong> &mdash; to the lower-right of {@code y = x}, so in {@link #REGION_I} or
     * {@link #REGION_IV} (or on {@link #NEG_NULL_VECTOR y = -x} between them).</li>
     * </ul>
     * If no pair of these conditions is met, then the point does not belong to any region.  The possibilities are:
     * <ul>
     * <li><strong>{@link #NAN}</strong> &mdash; either {@code x} or {@code y} is {@link Double#NaN}</li>
     * <li><strong>{@link #ZERO}</strong> &mdash; both {@code x} and {@code y} are {@code 0}</li>
     * <li><strong>{@link #POS_NULL_VECTOR}</strong> &mdash; on the line {@code y = x}, but not at {@code 0}.</li>
     * <li><strong>{@link #NEG_NULL_VECTOR}</strong> &mdash; on the line {@code y = -x}, but not at {@code 0}.</li>
     * </ul>
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
         * In Region I, {@code x > 0} and {@code |x| > |y|}, so positive {@code x} values dominate.
         * This is the "primary region" selected by {@link SplitComplex#region1mapped(Function)} .
         */
        REGION_I(splitComplex(1, 0)),

        /**
         * In the positive Y-axis's quadrant, as divided by the lines {@code y = x} and {@code y = -x}.
         * In Region II, {@code y > 0} and {@code |y| > |x|}, so positive {@code y} values dominate.
         */
        REGION_II(splitComplex(0, 1)),

        /**
         * In the negative X-axis's quadrant, as divided by the lines {@code y = x} and {@code y = -x}.
         * In Region III, {@code x < 0} and {@code |x| > |y|}, so negative {@code x} values dominate.
         */
        REGION_III(splitComplex(-1, 0)),

        /**
         * In the negative Y-axis's quadrant, as divided by the lines {@code y = x} and {@code y = -x}.
         * In Region IV, {@code y < 0} and {@code |y| > |x|}, so negative {@code y} values dominate.
         */
        REGION_IV(splitComplex(0, -1));


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
