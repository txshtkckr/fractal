package net.fwitz.math.complex.calculus;

import java.util.function.DoubleFunction;
import java.util.stream.IntStream;

import static java.lang.Math.sqrt;
import static java.util.Objects.requireNonNull;

public class Integration {
    public static Integration integrate(double lo, double hi, int terms, DoubleFunction<Double> fn) {
        return new Integration(lo, hi, terms, fn);
    }

    private final double lo;
    private final double hi;
    private final int terms;
    private final DoubleFunction<Double> fn;
    private final double dx;

    private Integration(double lo, double hi, int terms, DoubleFunction<Double> fn) {
        if (lo >= hi) {
            throw new IllegalArgumentException("lo >= hi: " + lo + " >= " + hi);
        }
        if (terms < 2) {
            throw new IllegalArgumentException("terms < 2: " + terms);
        }

        this.lo = lo;
        this.hi = hi;
        this.terms = terms;
        this.fn = requireNonNull(fn, "fn");
        this.dx = (hi - lo) / terms;
    }

    public double trapezium() {
        double sum = 0;
        double prev = fn.apply(lo);

        for (int i = 1; i < terms; ++i) {
            double x = lo + i * dx;
            double fx = fn.apply(x);
            sum = sum + prev + fx;
            prev = fx;
        }

        sum = sum + prev + fn.apply(hi);
        return sum * dx * 0.5;
    }

    public double midpoint() {
        double dxHalf = dx * 0.5;
        double sum = 0;

        for (int i = 1; i < terms; ++i) {
            double x = lo + i * dx + dxHalf;
            double fx = fn.apply(x);
            sum += fx;
        }

        return sum * dx;
    }

    public double simpson() {
        final int stop = ((terms & 1) == 1) ? (terms + 1) : terms;
        double sum = 0;
        double prevA = fn.apply(lo);
        double prevB = fn.apply(lo + dx);

        for (int i = 2; i < stop; i += 2) {
            double x = lo + i * dx;
            double fx = fn.apply(x);
            sum = sum + prevA + prevB * 4 + fx;
            prevA = prevB;
            prevB = fx;
        }

        sum = sum + prevA + prevB * 4 + fn.apply(hi);
        return sum * dx * 0.3333333333;
    }

    public double gauss() {
        double sum = 0;
        double dx1 = dx * (0.5 - sqrt(3) / 6);
        double dxr = dx * (0.5 + sqrt(3) / 6);

        for (int i = 0; i < terms; ++i) {
            double xa = lo + i * dx + dx1;
            double xb = lo + i * dx + dxr;
            double fa = fn.apply(xa);
            double fb = fn.apply(xb);
            sum = sum + fa + fb;
        }

        return sum * dx * 0.5;
    }

    public static void main(String[] args) {
        IntStream.of(4, 8, 16, 32, 64, 128, 256, 512, 1024)
                .forEach(Integration::debug);
    }

    private static void debug(int terms) {
        final Integration in = integrate(0, Math.PI, terms, Math::sin);
        System.out.format("%4d:  trap=%10.7f  midp=%10.7f  simp=%10.7f  gaus=%10.7f\n",
                terms,
                in.trapezium(),
                in.midpoint(),
                in.simpson(),
                in.gauss());
    }
}

