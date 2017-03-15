package net.fwitz.math.complex;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Test;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

import static java.lang.Math.PI;
import static java.util.Arrays.asList;
import static net.fwitz.math.complex.Complex.ONE;
import static net.fwitz.math.complex.Complex.ZERO;
import static net.fwitz.math.complex.Complex.complex;
import static net.fwitz.math.complex.Complex.imaginary;
import static net.fwitz.math.complex.Complex.real;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

public class ComplexTest {
    private static final double DEFAULT_DELTA = 0.00000001;

    @Test
    public void isNaN() throws Exception {
        assertThat(real(0).isNaN(), is(false));
        assertThat(real(4).isNaN(), is(false));
        assertThat(real(-4).isNaN(), is(false));
        assertThat(real(Double.NaN).isNaN(), is(true));
        assertThat(real(Double.POSITIVE_INFINITY).isNaN(), is(false));
        assertThat(real(Double.NEGATIVE_INFINITY).isNaN(), is(false));

        assertThat(imaginary(0).isNaN(), is(false));
        assertThat(imaginary(3).isNaN(), is(false));
        assertThat(imaginary(-3).isNaN(), is(false));
        assertThat(imaginary(Double.NaN).isNaN(), is(true));
        assertThat(imaginary(Double.POSITIVE_INFINITY).isNaN(), is(false));
        assertThat(imaginary(Double.NEGATIVE_INFINITY).isNaN(), is(false));

        assertThat(complex(4, 3).isNaN(), is(false));
        assertThat(complex(-4, 3).isNaN(), is(false));
        assertThat(complex(4, -3).isNaN(), is(false));
        assertThat(complex(-4, -3).isNaN(), is(false));
        assertThat(complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).isNaN(), is(false));
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).isNaN(), is(false));
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).isNaN(), is(false));
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY).isNaN(), is(false));
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NaN).isNaN(), is(true));
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NaN).isNaN(), is(true));
        assertThat(complex(Double.NaN, Double.POSITIVE_INFINITY).isNaN(), is(true));
        assertThat(complex(Double.NaN, Double.NEGATIVE_INFINITY).isNaN(), is(true));
        assertThat(complex(Double.NaN, Double.NaN).isNaN(), is(true));
    }

    @Test
    public void isInfinite() throws Exception {
        assertThat(real(0).isInfinite(), is(false));
        assertThat(real(4).isInfinite(), is(false));
        assertThat(real(-4).isInfinite(), is(false));
        assertThat(real(Double.NaN).isInfinite(), is(false));
        assertThat(real(Double.POSITIVE_INFINITY).isInfinite(), is(true));
        assertThat(real(Double.NEGATIVE_INFINITY).isInfinite(), is(true));

        assertThat(imaginary(0).isInfinite(), is(false));
        assertThat(imaginary(3).isInfinite(), is(false));
        assertThat(imaginary(-3).isInfinite(), is(false));
        assertThat(imaginary(Double.NaN).isInfinite(), is(false));
        assertThat(imaginary(Double.POSITIVE_INFINITY).isInfinite(), is(true));
        assertThat(imaginary(Double.NEGATIVE_INFINITY).isInfinite(), is(true));

        assertThat(complex(4, 3).isInfinite(), is(false));
        assertThat(complex(-4, 3).isInfinite(), is(false));
        assertThat(complex(4, -3).isInfinite(), is(false));
        assertThat(complex(-4, -3).isInfinite(), is(false));
        assertThat(complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).isInfinite(), is(true));
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).isInfinite(), is(true));
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).isInfinite(), is(true));
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY).isInfinite(), is(true));
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NaN).isInfinite(), is(false));
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NaN).isInfinite(), is(false));
        assertThat(complex(Double.NaN, Double.POSITIVE_INFINITY).isInfinite(), is(false));
        assertThat(complex(Double.NaN, Double.NEGATIVE_INFINITY).isInfinite(), is(false));
        assertThat(complex(Double.NaN, Double.NaN).isInfinite(), is(false));
    }

    @Test
    public void re() {
        assertThat(real(0).re(), p0());
        assertThat(real(4).re(), apx(4));
        assertThat(real(-4).re(), apx(-4));
        assertThat(real(Double.NaN).re(), nan());
        assertThat(real(Double.POSITIVE_INFINITY).re(), pinf());
        assertThat(real(Double.NEGATIVE_INFINITY).re(), ninf());

        assertThat(imaginary(0).re(), p0());
        assertThat(imaginary(3).re(), p0());
        assertThat(imaginary(-3).re(), p0());
        assertThat(imaginary(Double.NaN).re(), p0());
        assertThat(imaginary(Double.POSITIVE_INFINITY).re(), p0());
        assertThat(imaginary(Double.NEGATIVE_INFINITY).re(), p0());

        assertThat(complex(4, 3).re(), apx(4));
        assertThat(complex(-4, 3).re(), apx(-4));
        assertThat(complex(4, -3).re(), apx(4));
        assertThat(complex(-4, -3).re(), apx(-4));
        assertThat(complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).re(), pinf());
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).re(), pinf());
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).re(), ninf());
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY).re(), ninf());
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NaN).re(), pinf());
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NaN).re(), ninf());
        assertThat(complex(Double.NaN, Double.POSITIVE_INFINITY).re(), nan());
        assertThat(complex(Double.NaN, Double.NEGATIVE_INFINITY).re(), nan());
        assertThat(complex(Double.NaN, Double.NaN).re(), nan());
    }

    @Test
    public void im() {
        assertThat(real(0).im(), p0());
        assertThat(real(4).im(), p0());
        assertThat(real(-4).im(), p0());
        assertThat(real(Double.NaN).im(), p0());
        assertThat(real(Double.POSITIVE_INFINITY).im(), p0());
        assertThat(real(Double.NEGATIVE_INFINITY).im(), p0());

        assertThat(imaginary(0).im(), p0());
        assertThat(imaginary(3).im(), apx(3));
        assertThat(imaginary(-3).im(), apx(-3));
        assertThat(imaginary(Double.NaN).im(), nan());
        assertThat(imaginary(Double.POSITIVE_INFINITY).im(), pinf());
        assertThat(imaginary(Double.NEGATIVE_INFINITY).im(), ninf());

        assertThat(complex(4, 3).im(), apx(3));
        assertThat(complex(-4, 3).im(), apx(3));
        assertThat(complex(4, -3).im(), apx(-3));
        assertThat(complex(-4, -3).im(), apx(-3));
        assertThat(complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).im(), pinf());
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).im(), ninf());
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).im(), pinf());
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY).im(), ninf());
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NaN).im(), nan());
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NaN).im(), nan());
        assertThat(complex(Double.NaN, Double.POSITIVE_INFINITY).im(), pinf());
        assertThat(complex(Double.NaN, Double.NEGATIVE_INFINITY).im(), ninf());
        assertThat(complex(Double.NaN, Double.NaN).im(), nan());
    }

    @Test
    public void abs() {
        assertThat(real(0).abs(), p0());
        assertThat(real(4).abs(), apx(4));
        assertThat(real(-4).abs(), apx(4));
        assertThat(real(Double.NaN).abs(), nan());
        assertThat(real(Double.POSITIVE_INFINITY).abs(), pinf());
        assertThat(real(Double.NEGATIVE_INFINITY).abs(), pinf());

        assertThat(imaginary(0).abs(), p0());
        assertThat(imaginary(3).abs(), apx(3));
        assertThat(imaginary(-3).abs(), apx(3));
        assertThat(imaginary(Double.NaN).abs(), nan());
        assertThat(imaginary(Double.POSITIVE_INFINITY).abs(), pinf());
        assertThat(imaginary(Double.NEGATIVE_INFINITY).abs(), pinf());

        assertThat(complex(4, 3).abs(), apx(5));
        assertThat(complex(-4, 3).abs(), apx(5));
        assertThat(complex(4, -3).abs(), apx(5));
        assertThat(complex(-4, -3).abs(), apx(5));
        assertThat(complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).abs(), pinf());
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).abs(), pinf());
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).abs(), pinf());
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY).abs(), pinf());
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NaN).abs(), pinf());
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NaN).abs(), pinf());
        assertThat(complex(Double.NaN, Double.POSITIVE_INFINITY).abs(), pinf());
        assertThat(complex(Double.NaN, Double.NEGATIVE_INFINITY).abs(), pinf());
        assertThat(complex(Double.NaN, Double.NaN).abs(), nan());
    }

    @Test
    public void abs2() {
        assertThat(real(0).abs2(), p0());
        assertThat(real(4).abs2(), apx(16));
        assertThat(real(-4).abs2(), apx(16));
        assertThat(real(Double.NaN).abs2(), nan());
        assertThat(real(Double.POSITIVE_INFINITY).abs2(), pinf());
        assertThat(real(Double.NEGATIVE_INFINITY).abs2(), pinf());

        assertThat(imaginary(0).abs2(), p0());
        assertThat(imaginary(3).abs2(), apx(9));
        assertThat(imaginary(-3).abs2(), apx(9));
        assertThat(imaginary(Double.NaN).abs2(), nan());
        assertThat(imaginary(Double.POSITIVE_INFINITY).abs2(), pinf());
        assertThat(imaginary(Double.NEGATIVE_INFINITY).abs2(), pinf());

        assertThat(complex(4, 3).abs2(), apx(25));
        assertThat(complex(-4, 3).abs2(), apx(25));
        assertThat(complex(4, -3).abs2(), apx(25));
        assertThat(complex(-4, -3).abs2(), apx(25));
        assertThat(complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).abs2(), pinf());
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).abs2(), pinf());
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).abs2(), pinf());
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY).abs2(), pinf());
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NaN).abs2(), pinf());
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NaN).abs2(), pinf());
        assertThat(complex(Double.NaN, Double.POSITIVE_INFINITY).abs2(), pinf());
        assertThat(complex(Double.NaN, Double.NEGATIVE_INFINITY).abs2(), pinf());
        assertThat(complex(Double.NaN, Double.NaN).abs2(), nan());
    }

    @Test
    public void arg() {
        assertThat(real(0).arg(), p0());
        assertThat(real(4).arg(), p0());
        assertThat(real(-4).arg(), apx(PI));
        assertThat(real(Double.NaN).arg(), nan());
        assertThat(real(Double.POSITIVE_INFINITY).arg(), p0());
        assertThat(real(Double.NEGATIVE_INFINITY).arg(), apx(PI));

        assertThat(imaginary(0).arg(), p0());
        assertThat(imaginary(3).arg(), apx(PI / 2));
        assertThat(imaginary(-3).arg(), apx(-PI / 2));
        assertThat(imaginary(Double.NaN).arg(), nan());
        assertThat(imaginary(Double.POSITIVE_INFINITY).arg(), apx(PI / 2));
        assertThat(imaginary(Double.NEGATIVE_INFINITY).arg(), apx(-PI / 2));

        assertThat(complex(4, 3).arg(), apx(0.643501108793284));
        assertThat(complex(-4, 3).arg(), apx(PI - 0.643501108793284));
        assertThat(complex(4, -3).arg(), apx(-0.643501108793284));
        assertThat(complex(-4, -3).arg(), apx(-PI + 0.643501108793284));
        assertThat(complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).arg(), apx(PI / 4));
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).arg(), apx(-PI / 4));
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).arg(), apx(PI * 3 / 4));
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY).arg(), apx(-PI * 3 / 4));
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NaN).arg(), nan());
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NaN).arg(), nan());
        assertThat(complex(Double.NaN, Double.POSITIVE_INFINITY).arg(), nan());
        assertThat(complex(Double.NaN, Double.NEGATIVE_INFINITY).arg(), nan());
        assertThat(complex(Double.NaN, Double.NaN).arg(), nan());

        // Special cases for branch cut and negative 0
        assertThat(complex(Double.POSITIVE_INFINITY, 4).arg(), p0());
        assertThat(complex(4.0, -0.0).arg(), n0());
        assertThat(complex(Double.POSITIVE_INFINITY, -4).arg(), n0());
        assertThat(complex(Double.NEGATIVE_INFINITY, 4).arg(), apx(PI));
        assertThat(complex(-4.0, -0.0).arg(), apx(-PI));
        assertThat(complex(Double.NEGATIVE_INFINITY, -4).arg(), apx(-PI));
        assertThat(complex(-0.0, 1.0).arg(), apx(PI / 2));
        assertThat(complex(-0.0, -1.0).arg(), apx(-PI / 2));
        assertThat(complex(Double.NEGATIVE_INFINITY, -0.0).arg(), apx(-PI));
    }

    @Test
    public void conjugate() {
        assertThat(real(0).conjugate(), cpx(p0(), n0()));
        assertThat(real(4).conjugate(), cpx(apx(4), n0()));
        assertThat(real(-4).conjugate(), cpx(apx(-4), n0()));
        assertThat(real(Double.NaN).conjugate(), cpx(nan(), n0()));
        assertThat(real(Double.POSITIVE_INFINITY).conjugate(), cpx(pinf(), n0()));
        assertThat(real(Double.NEGATIVE_INFINITY).conjugate(), cpx(ninf(), n0()));

        assertThat(imaginary(0).conjugate(), cpx(p0(), n0()));
        assertThat(imaginary(3).conjugate(), cpx(p0(), apx(-3)));
        assertThat(imaginary(-3).conjugate(), cpx(p0(), apx(3)));
        assertThat(imaginary(Double.NaN).conjugate(), cpx(p0(), nan()));
        assertThat(imaginary(Double.POSITIVE_INFINITY).conjugate(), cpx(p0(), ninf()));
        assertThat(imaginary(Double.NEGATIVE_INFINITY).conjugate(), cpx(p0(), pinf()));

        assertThat(complex(4, 3).conjugate(), cpx(4, -3));
        assertThat(complex(-4, 3).conjugate(), cpx(-4, -3));
        assertThat(complex(4, -3).conjugate(), cpx(4, 3));
        assertThat(complex(-4, -3).conjugate(), cpx(-4, 3));
        assertThat(complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).conjugate(), cpx(pinf(), ninf()));
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).conjugate(), cpx(pinf(), pinf()));
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).conjugate(), cpx(ninf(), ninf()));
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY).conjugate(), cpx(ninf(), pinf()));
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NaN).conjugate(), cpx(pinf(), nan()));
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NaN).conjugate(), cpx(ninf(), nan()));
        assertThat(complex(Double.NaN, Double.POSITIVE_INFINITY).conjugate(), cpx(nan(), ninf()));
        assertThat(complex(Double.NaN, Double.NEGATIVE_INFINITY).conjugate(), cpx(nan(), pinf()));
        assertThat(complex(Double.NaN, Double.NaN).conjugate(), cpx(nan(), nan()));
    }

    @Test
    public void negative() {
        assertThat(real(0).negative(), cpx(n0(), n0()));
        assertThat(real(4).negative(), cpx(apx(-4), n0()));
        assertThat(real(-4).negative(), cpx(apx(4), n0()));
        assertThat(real(Double.NaN).negative(), cpx(nan(), n0()));
        assertThat(real(Double.POSITIVE_INFINITY).negative(), cpx(ninf(), n0()));
        assertThat(real(Double.NEGATIVE_INFINITY).negative(), cpx(pinf(), n0()));

        assertThat(imaginary(0).negative(), cpx(n0(), n0()));
        assertThat(imaginary(3).negative(), cpx(n0(), apx(-3)));
        assertThat(imaginary(-3).negative(), cpx(n0(), apx(3)));
        assertThat(imaginary(Double.NaN).negative(), cpx(n0(), nan()));
        assertThat(imaginary(Double.POSITIVE_INFINITY).negative(), cpx(n0(), ninf()));
        assertThat(imaginary(Double.NEGATIVE_INFINITY).negative(), cpx(n0(), pinf()));

        assertThat(complex(4, 3).negative(), cpx(-4, -3));
        assertThat(complex(-4, 3).negative(), cpx(4, -3));
        assertThat(complex(4, -3).negative(), cpx(-4, 3));
        assertThat(complex(-4, -3).negative(), cpx(4, 3));
        assertThat(complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).negative(), cpx(ninf(), ninf()));
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).negative(), cpx(ninf(), pinf()));
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).negative(), cpx(pinf(), ninf()));
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY).negative(), cpx(pinf(), pinf()));
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NaN).negative(), cpx(ninf(), nan()));
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NaN).negative(), cpx(pinf(), nan()));
        assertThat(complex(Double.NaN, Double.POSITIVE_INFINITY).negative(), cpx(nan(), ninf()));
        assertThat(complex(Double.NaN, Double.NEGATIVE_INFINITY).negative(), cpx(nan(), pinf()));
        assertThat(complex(Double.NaN, Double.NaN).negative(), cpx(nan(), nan()));
    }

    @Test
    public void inverse() {
        assertThat(real(0).inverse(), cpx(nan(), nan()));
        assertThat(real(4).inverse(), cpx(apx(0.25), p0()));
        assertThat(real(-4).inverse(), cpx(apx(-0.25), p0()));
        assertThat(real(Double.NaN).inverse(), cpx(nan(), p0()));
        assertThat(real(Double.POSITIVE_INFINITY).inverse(), cpx(p0(), p0()));
        assertThat(real(Double.NEGATIVE_INFINITY).inverse(), cpx(n0(), p0()));

        assertThat(imaginary(0).inverse(), cpx(nan(), nan()));
        assertThat(imaginary(3).inverse(), cpx(p0(), apx(-1.0 / 3)));
        assertThat(imaginary(-3).inverse(), cpx(p0(), apx(1.0 / 3)));
        assertThat(imaginary(Double.NaN).inverse(), cpx(p0(), nan()));
        assertThat(imaginary(Double.POSITIVE_INFINITY).inverse(), cpx(p0(), n0()));
        assertThat(imaginary(Double.NEGATIVE_INFINITY).inverse(), cpx(p0(), p0()));

        assertThat(complex(4, 3).inverse(), cpx(0.16, -0.12));
        assertThat(complex(-4, 3).inverse(), cpx(-0.16, -0.12));
        assertThat(complex(4, -3).inverse(), cpx(0.16, 0.12));
        assertThat(complex(-4, -3).inverse(), cpx(-0.16, 0.12));
        assertThat(complex(Double.POSITIVE_INFINITY, Double.POSITIVE_INFINITY).inverse(), cpx(nan(), nan()));
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY).inverse(), cpx(nan(), nan()));
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY).inverse(), cpx(nan(), nan()));
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NEGATIVE_INFINITY).inverse(), cpx(nan(), nan()));
        assertThat(complex(Double.POSITIVE_INFINITY, Double.NaN).inverse(), cpx(nan(), nan()));
        assertThat(complex(Double.NEGATIVE_INFINITY, Double.NaN).inverse(), cpx(nan(), nan()));
        assertThat(complex(Double.NaN, Double.POSITIVE_INFINITY).inverse(), cpx(nan(), nan()));
        assertThat(complex(Double.NaN, Double.NEGATIVE_INFINITY).inverse(), cpx(nan(), nan()));
        assertThat(complex(Double.NaN, Double.NaN).inverse(), cpx(nan(), nan()));
    }

    @Test
    public void plus() {
        assertThat(real(0.0).plus(imaginary(0.0)), cpx(p0(), p0()));
        assertThat(real(0.0).plus(imaginary(-0.0)), cpx(p0(), p0()));
        assertThat(real(-0.0).plus(imaginary(0.0)), cpx(p0(), p0()));
        assertThat(real(-0.0).plus(imaginary(-0.0)), cpx(p0(), p0()));
        
    }

    @Test
    public void roots() {
        for (int re = -9; re <= 9; re += 3) {
            for (int im = -9; im <= 9; im += 3) {
                Complex z0 = complex(re, im);
                IntStream.of(3, 7, 20, 1000).forEach(n -> {
                    Complex[] roots = z0.roots(n);
                    Arrays.stream(roots).forEach(z -> assertThat(z.pow(n), cpx(z0)));
                });
            }
        }
        assertThat(asList(ZERO.roots(3)), contains(ZERO));
        assertThat(asList(real(Double.NaN).roots(3)), hasSize(0));
        assertThat(asList(real(Double.POSITIVE_INFINITY).roots(3)), hasSize(0));
        assertThat(asList(real(Double.NEGATIVE_INFINITY).roots(3)), hasSize(0));
        assertThat(asList(imaginary(Double.NaN).roots(3)), hasSize(0));
        assertThat(asList(imaginary(Double.POSITIVE_INFINITY).roots(3)), hasSize(0));
        assertThat(asList(imaginary(Double.NEGATIVE_INFINITY).roots(3)), hasSize(0));
        assertThrows(IllegalArgumentException.class, () -> ONE.roots(0));
        assertThrows(IllegalArgumentException.class, () -> ONE.roots(-2));
        assertThrows(IllegalArgumentException.class, () -> ONE.roots(Integer.MAX_VALUE));
    }


    


    private static Matcher<Complex> cpx(Matcher<? super Double> reMatcher, Matcher<? super Double> imMatcher) {
        return new ComplexMatcher(reMatcher, imMatcher);
    }

    private static Matcher<Complex> cpx(double expectedRe, double expectedIm) {
        return new ComplexMatcher(apx(expectedRe), apx(expectedIm));
    }

    private static Matcher<Complex> cpx(Complex expectedValue) {
        return new ComplexMatcher(apx(expectedValue.re()), apx(expectedValue.im()));
    }


    private static Matcher<Double> apx(double expectedValue) {
        if (Double.isNaN(expectedValue)) {
            return nan();
        }
        if (Double.isInfinite(expectedValue)) {
            return new InfinityMatcher(expectedValue < 0);
        }
        return closeTo(expectedValue, DEFAULT_DELTA);
    }

    private static Matcher<Double> apx(double expectedValue, double delta) {
        return closeTo(expectedValue, delta);
    }

    private static Matcher<Double> p0() {
        return is(0.0);
    }

    private static Matcher<Double> n0() {
        return is(-0.0);
    }

    private static Matcher<Double> nan() {
        return new NanMatcher();
    }

    private static Matcher<Double> inf() {
        return new InfinityMatcher(null);
    }

    private static Matcher<Double> pinf() {
        return new InfinityMatcher(false);
    }

    private static Matcher<Double> ninf() {
        return new InfinityMatcher(true);
    }

    private static <Y> void assertThrows(Class<? extends Exception> exClass, Callable<Y> callable) {
        try {
            Y yield = callable.call();
            fail("Expected exception of type '" + exClass.getName() + "', but got result: " + yield);
        } catch (Exception e) {
            assertThat("Caught exception of wrong type", e, instanceOf(exClass));
        }
    }

    private static class NanMatcher extends TypeSafeMatcher<Double> {
        @Override
        protected boolean matchesSafely(Double x) {
            return x.isNaN();
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("double that is NaN");
        }
    }

    private static class InfinityMatcher extends TypeSafeMatcher<Double> {
        private Boolean expectNegative;

        InfinityMatcher(Boolean expectNegative) {
            this.expectNegative = expectNegative;
        }

        @Override
        protected boolean matchesSafely(Double x) {
            return x.isInfinite() && (expectNegative == null || expectNegative == x < 0);
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("double that is ");
            if (expectNegative == null) {
                description.appendText("infinite");
            } else if (expectNegative) {
                description.appendText("-Infinity");
            } else {
                description.appendText("+Infinity");
            }
        }
    }

    private static class ComplexMatcher extends TypeSafeMatcher<Complex> {
        private final Matcher<? super Double> expectedRe;
        private final Matcher<? super Double> expectedIm;

        private ComplexMatcher(Matcher<? super Double> expectedRe, Matcher<? super Double> expectedIm) {
            this.expectedRe = expectedRe;
            this.expectedIm = expectedIm;
        }

        @Override
        protected boolean matchesSafely(Complex complex) {
            return expectedRe.matches(complex.re()) && expectedIm.matches(complex.im());
        }

        @Override
        public void describeTo(Description description) {
            description.appendText("Complex(re = ")
                    .appendDescriptionOf(expectedRe)
                    .appendText("; im = ")
                    .appendDescriptionOf(expectedIm)
                    .appendText(")");
        }
    }
}