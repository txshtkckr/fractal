package net.fwitz.math.plot;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.color.domain.DomainColoringAdvanced;

import javax.swing.*;
import java.awt.*;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static net.fwitz.math.complex.Complex.complex;

public class FunctionPlot {
    private static final int DEFAULT_HEIGHT = 600;
    private static final int DEFAULT_WIDTH = 600;
    private static final Complex DEFAULT_DOMAIN_BOUND_1 = complex(-5, -5);
    private static final Complex DEFAULT_DOMAIN_BOUND_2 = complex(5, 5);

    private final String title;

    private Complex domainBound1 = DEFAULT_DOMAIN_BOUND_1;
    private Complex domainBound2 = DEFAULT_DOMAIN_BOUND_2;
    private int width = DEFAULT_WIDTH;
    private int height = DEFAULT_HEIGHT;
    private Function<Complex, Complex> fn = z -> z;
    private BiFunction<Complex, Complex, Color> colorFn = new DomainColoringAdvanced();

    public FunctionPlot(String title) {
        this.title = requireNonNull(title, "title");
    }

    public final FunctionPlot domainBound(double re1, double im1, double re2, double im2) {
        this.domainBound1 = complex(re1, im1);
        this.domainBound2 = complex(re2, im2);
        return this;
    }

    public final FunctionPlot domainBound1(double re1, double im1) {
        this.domainBound1 = complex(re1, im1);
        return this;
    }

    public final FunctionPlot domainBound1(Complex domainBound1) {
        this.domainBound1 = requireNonNull(domainBound1, "domainBound1");
        return this;
    }

    public final FunctionPlot domainBound2(double re2, double im2) {
        this.domainBound2 = complex(re2, im2);
        return this;
    }

    public final FunctionPlot domainBound2(Complex domainBound2) {
        this.domainBound2 = requireNonNull(domainBound2, "domainBound2");
        return this;
    }

    public final FunctionPlot width(int width) {
        if (width < 100 || width > 16384) {
            throw new IllegalArgumentException("width: " + width);
        }
        this.width = width;
        return this;
    }

    public final FunctionPlot height(int height) {
        if (height < 100 || height > 16384) {
            throw new IllegalArgumentException("height: " + height);
        }
        this.height = height;
        return this;
    }

    public final FunctionPlot size(int width, int height) {
        return width(width).height(height);
    }

    public final FunctionPlot domainRe(double re1, double re2) {
        this.domainBound1 = complex(re1, domainBound1.im());
        this.domainBound2 = complex(re2, domainBound2.im());
        return this;
    }

    public final FunctionPlot domainIm(double im1, double im2) {
        this.domainBound1 = complex(domainBound1.re(), im1);
        this.domainBound2 = complex(domainBound2.re(), im2);
        return this;
    }

    public final FunctionPlot fn(Function<Complex, Complex> fn) {
        this.fn = requireNonNull(fn, "fn");
        return this;
    }

    public final FunctionPlot colorFn(BiFunction<Complex, Complex, Color> colorFn) {
        this.colorFn = requireNonNull(colorFn, "colorFn");
        return this;
    }

    public final void render() {
        final JFrame frame = new JFrame(title);
        frame.setSize(width, height);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setContentPane(new FunctionPanel(domainBound1, domainBound2, fn, colorFn));
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        new FunctionPlot("Simple test analysis").render();
    }
}
