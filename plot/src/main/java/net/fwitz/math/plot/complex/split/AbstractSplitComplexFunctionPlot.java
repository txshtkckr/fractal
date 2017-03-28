package net.fwitz.math.plot.complex.split;

import net.fwitz.math.complex.SplitComplex;

import javax.swing.*;
import java.awt.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.util.Objects.requireNonNull;
import static net.fwitz.math.complex.SplitComplex.splitComplex;

public abstract class AbstractSplitComplexFunctionPlot<
        T extends AbstractSplitComplexFunctionPlot<T, P, V>,
        P extends AbstractSplitComplexFunctionPanel<V>,
        V> {

    private static final int DEFAULT_HEIGHT = 600;
    private static final int DEFAULT_WIDTH = 600;
    private static final SplitComplex DEFAULT_DOMAIN_BOUND_1 = splitComplex(-5, -5);
    private static final SplitComplex DEFAULT_DOMAIN_BOUND_2 = splitComplex(5, 5);

    private final String title;

    protected Function<SplitComplex, V> computeFn;
    protected BiFunction<SplitComplex, V, Color> colorFn;
    protected SplitComplex domainBound1 = DEFAULT_DOMAIN_BOUND_1;
    protected SplitComplex domainBound2 = DEFAULT_DOMAIN_BOUND_2;
    protected int width = DEFAULT_WIDTH;
    protected int height = DEFAULT_HEIGHT;

    private Consumer<P> panelDecorator = panel -> {
    };

    protected AbstractSplitComplexFunctionPlot(String title) {
        this.title = requireNonNull(title, "title");
    }

    public final T domainBound(double re1, double im1, double re2, double im2) {
        this.domainBound1 = splitComplex(re1, im1);
        this.domainBound2 = splitComplex(re2, im2);
        return self();
    }

    public final T domainBound1(double re1, double im1) {
        this.domainBound1 = splitComplex(re1, im1);
        return self();
    }

    public final T domainBound1(SplitComplex domainBound1) {
        this.domainBound1 = requireNonNull(domainBound1, "domainBound1");
        return self();
    }

    public final T domainBound2(double re2, double im2) {
        this.domainBound2 = splitComplex(re2, im2);
        return self();
    }

    public final T domainBound2(SplitComplex domainBound2) {
        this.domainBound2 = requireNonNull(domainBound2, "domainBound2");
        return self();
    }

    public final T width(int width) {
        if (width < 100 || width > 16384) {
            throw new IllegalArgumentException("width: " + width);
        }
        this.width = width;
        return self();
    }

    public final int width() {
        return width;
    }

    public final T height(int height) {
        if (height < 100 || height > 16384) {
            throw new IllegalArgumentException("height: " + height);
        }
        this.height = height;
        return self();
    }

    public final int height() {
        return height;
    }

    public final T size(int width, int height) {
        return width(width).height(height);
    }

    public final T domainRe(double re1, double re2) {
        this.domainBound1 = splitComplex(re1, domainBound1.y());
        this.domainBound2 = splitComplex(re2, domainBound2.y());
        return self();
    }

    public final T domainIm(double im1, double im2) {
        this.domainBound1 = splitComplex(domainBound1.x(), im1);
        this.domainBound2 = splitComplex(domainBound2.x(), im2);
        return self();
    }

    public final T computeFn(Function<SplitComplex, V> computeFn) {
        this.computeFn = requireNonNull(computeFn, "computeFn");
        return self();
    }

    public final T colorFn(BiFunction<SplitComplex, V, Color> colorFn) {
        this.colorFn = requireNonNull(colorFn, "colorFn");
        return self();
    }

    public final T decoratePanel(Consumer<? super P> decorator) {
        this.panelDecorator = panelDecorator.andThen(decorator);
        return self();
    }

    // Compiler is too stupid to understand that "this" is always a <T>.
    @SuppressWarnings("unchecked")
    protected T self() {
        return (T) this;
    }

    public final void render() {
        final JFrame frame = new JFrame(title);
        frame.setSize(width(), height());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final P panel = createPanel();
        frame.setContentPane(panel);

        panelDecorator.accept(panel);
        frame.setVisible(true);
    }

    protected abstract P createPanel();

}
