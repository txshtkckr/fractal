package net.fwitz.math.binary.dual;

import net.fwitz.math.binary.BinaryNumber;

import java.util.function.Function;

import static net.fwitz.math.binary.RealMath.rcos;
import static net.fwitz.math.binary.RealMath.rcosh;
import static net.fwitz.math.binary.RealMath.rexp;
import static net.fwitz.math.binary.RealMath.rhypot;
import static net.fwitz.math.binary.RealMath.rlog;
import static net.fwitz.math.binary.RealMath.rsin;
import static net.fwitz.math.binary.RealMath.rsinh;
import static net.fwitz.math.binary.RealMath.rtan;
import static net.fwitz.math.binary.RealMath.rtanh;

public class DualNumber extends BinaryNumber<DualNumber> {
    private static final DualNumber NaN = new DualNumber(Double.NaN, Double.NaN);
    private static final DualNumber ZERO = new DualNumber(0, 0);
    private static final DualNumber ONE = new DualNumber(1, 0);

    private final double x;
    private final double y;

    public static DualNumber dualNumber(double x, double y) {
        return new DualNumber(x, y);
    }

    private DualNumber(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    protected DualNumber z(double x, double y) {
        return new DualNumber(x, y);
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
    public double abs() {
        return rhypot(x, y);
    }

    @Override
    public double arg() {
        return y / x;
    }

    @Override
    public DualNumber inverse() {
        double xInv = 1 / x;
        return z(xInv, -y * xInv * xInv);
    }

    @Override
    public DualNumber timesY() {
        //noinspection SuspiciousNameCombination
        return z(0, x);
    }

    @Override
    public DualNumber timesY(double y) {
        return z(0, x * y);
    }

    @Override
    public DualNumber timesNegativeY() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public DualNumber times(BinaryNumber<? extends DualNumber> c) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public DualNumber divY() {
        return NaN;
    }

    @Override
    public DualNumber divY(double y) {
        return NaN;
    }

    @Override
    public DualNumber divNegativeY() {
        return NaN;
    }

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
    @Override
    public DualNumber log() {
        return z(rlog(x), y / x);
    }

    @Override
    public DualNumber exp() {
        double ex = rexp(x);
        return z(ex, ex * y);
    }

    @Override
    public DualNumber pow(double a) {
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

        double xam1 = Math.pow(x, a);
        return z(x * xam1, a * xam1 * y);
    }

    @Override
    public DualNumber pow(BinaryNumber<? extends DualNumber> c) {
        return (c.y() == 0.0) ? pow(c.x()) : log().times(c).exp();
    }

    /**
     * {@inheritDoc}
     * <p>
     * For dual numbers, this means that negative {@code x values return a function to reflect over the line
     * {@code y = 0}; that is, {@code x} gets negated, with {@code y} left unmodified.
     * </p>
     */
    @Override
    public Function<? super DualNumber, ? extends DualNumber> region1dual() {
        return (x < 0) ? w -> z(-w.x(), w.y()) : Function.identity();
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
    @Override
    public DualNumber sin() {
        if (y == 0) {
            return z(rsin(x), 0);
        }
        if (x == 0) {
            return z(0, y);
        }
        return z(rsin(x), y * rcos(x));
    }

    //  Using the double-angle formula for sin:
    //    cos(a + b) = cos(a) cos(b) - sin(a) sin(b)
    //    cos(x + jy) = cos(x) cos(jy) - sin(x) sin(jy)
    //    cos(x + jy) = cos(x) - jy sin(x)
    @Override
    public DualNumber cos() {
        if (y == 0) {
            return z(rcos(x), 0);
        }
        if (x == 0) {
            return ONE;
        }
        return z(rcos(x), -y * rsin(x));
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
    @Override
    public DualNumber tan() {
        if (y == 0) {
            return z(rtan(x), 0);
        }
        if (x == 0) {
            return z(0, y);
        }

        double secx = 1 / rcos(x);
        return z(rtan(x), y * secx * secx);
    }

    // The Talyor expansions of sinh(jy) and cosh(jy) turn out exactly like they did for their corresponding
    // trig functions, as the only difference is the sign of high-powered terms that are all 0 anyway.
    //    sinh(jy) = jy
    //    cosh(jy) = 1
    // So
    //    sinh(a + b) = sinh(a) cosh(b) + cosh(a) sinh(b)
    //    sinh(x + jy) = sinh(x) cosh(jy) + cosh(x) sinh(jy)
    //    sinh(x + jy) = sinh(x) + jy cosh(x)

    @Override
    public DualNumber sinh() {
        if (y == 0) {
            return z(rsinh(x), 0);
        }
        if (x == 0) {
            return z(0, y * rcosh(x));
        }
        return z(rsinh(x), y * rcosh(x));
    }

    //    cosh(a + b) = cosh(a) cosh(b) + sinh(a) sinh(b)
    //    cosh(x + jy) = cosh(x) cosh(jy) + sinh(x) sinh(jy)
    //    cosh(x + jy) = cosh(x) + jy sinh(x)
    @Override
    public DualNumber cosh() {
        if (y == 0) {
            return z(rcosh(x), 0);
        }
        if (x == 0) {
            return ONE;
        }
        return z(rcosh(x), y * rsinh(x));
    }

    //   tanh(jy) = sinh(jy) / cosh(jy)
    //   tanh(jy) = jy
    // So,,,
    //   tanh(a + b) = (tanh(a) + tanh(b)) / (1 + tanh(a) tanh(b))
    //   tanh(x + jy) = (tanh(x) + tanh(jy)) / (1 + tanh(x) tanh(jy))
    //   tanh(x + jy) = (tanh(x) + jy) / (1 + jy tanh(x))
    //   tanh(x + jy) = (tanh(x) + jy) (1 - jy tanh(x)) / 1
    //   tanh(x + jy) = tanh(x) - jy tanh2(x) + jy - jjyy tanh(x)
    //   tanh(x + jy) = tanh(x) + jy (1 - tanh2(x))
    //   tanh(x + jy) = tanh(x) + jy sech2 (x)

    @Override
    public DualNumber tanh() {
        if (y == 0) {
            return z(rtanh(x), 0);
        }
        if (x == 0) {
            return z(0, y);
        }
        double sechx = 1 / rcosh(x);
        return z(rtanh(x), y * sechx * sechx);
    }
}
