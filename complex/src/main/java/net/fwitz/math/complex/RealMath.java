package net.fwitz.math.complex;

import net.fwitz.math.complex.analysis.Erf;

/**
 * Utility static class that repackages many of the methods from {@link Math} under different names to
 * avoid clashes and adds a few that were missing.
 * <p>
 * <strong>WARNING</strong>: All of the methods in this class are purely real-valued functions.  For example,
 * {@link #rsqrt(double) rsqrt(-1)} returns {@link Double#NaN}, not {@link Complex#I}.  For those functions
 * like square root whose domain gets extended under Complex analysis, look for a corresponding static method
 * on {@link Complex}, such as {@link Complex#sqrt(double)}.
 * </p>
 */
public class RealMath {

    static double rabs(double a) {
        return Math.abs(a);
    }

    static double racos(double a) {
        return Math.acos(a);
    }

    static double racosh(double a) {
        return rlog(a + rsqrt(a * a - 1));
    }

    static double rasin(double a) {
        return Math.asin(a);
    }

    static double ratan(double a) {
        return Math.atan(a);
    }

    // atanh(x) = 0.5 * log[ (1+x) / (1-x) ]
    //          = 0.5 * (log(1 + x) - log(1 - x))
    //          = 0.5 * (log1p(x) - log1p(-x))
    //          = 0.5 * log1p(x) - 0.5 * log1p(-x)
    static double ratanh(double x) {
        return 0.5 * rlog1p(x) - 0.5 * rlog1p(-x);
    }

    static double rcos(double a) {
        return Math.cos(a);
    }

    static double rcosh(double a) {
        return Math.cosh(a);
    }

    static double rexp(double a) {
        return Math.exp(a);
    }

    static double rhypot(double x, double y) {
        return Math.hypot(x, y);
    }

    static double rlog(double a) {
        return Math.log(a);
    }

    static double rlog1p(double x) {
        return Math.log1p(x);
    }

    static double rsin(double a) {
        return Math.sin(a);
    }

    static double rsinh(double a) {
        return Math.sinh(a);
    }

    /**
     * @see Complex#sqrt(double)
     */
    static double rsqrt(double a) {
        return Math.sqrt(a);
    }

    static double rtan(double a) {
        return Math.tan(a);
    }

    static double rtanh(double a) {
        return Math.tanh(a);
    }

    static double rerf(double a) {
        return Erf.erf(a);
    }

    static double rerfc(double a) {
        return Erf.erfc(a);
    }

    static double rerfi(double a) {
        return Erf.erfi(a);
    }
}
