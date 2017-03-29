package net.fwitz.math.plot.binary;

import net.fwitz.math.binary.BinaryNumber;

import javax.swing.*;
import java.awt.*;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.DoubleStream;

import static java.util.Objects.requireNonNull;

/**
 * @param <L> the plot type (sub-classes should specify themselves)
 * @param <P> the panel type
 * @param <T> the binary number type
 * @param <V> the computed value type
 */
public abstract class BinaryNumberFunctionPlot<
        L extends BinaryNumberFunctionPlot<L, P, T, V>,
        P extends BinaryNumberFunctionPanel<T, V>,
        T extends BinaryNumber<T>,
        V> {

    private static final int DEFAULT_HEIGHT = 600;
    private static final int DEFAULT_WIDTH = 600;

    private final String title;

    protected int width = DEFAULT_WIDTH;
    protected int height = DEFAULT_HEIGHT;
    protected double x1 = -5;
    protected double y1 = -5;
    protected double x2 = 5;
    protected double y2 = 5;
    protected Function<? super T, ? extends V> computeFn;
    protected BiFunction<? super T, ? super V, Color> colorFn;

    private Consumer<P> panelDecorator = panel -> {
    };

    public abstract T value(double x, double y);

    protected BinaryNumberFunctionPlot(String title) {
        this.title = requireNonNull(title, "title");
    }


    public final L width(int width) {
        if (width < 100 || width > 16384) {
            throw new IllegalArgumentException("width: " + width);
        }
        this.width = width;
        return self();
    }

    public final int width() {
        return width;
    }

    public final L height(int height) {
        if (height < 100 || height > 16384) {
            throw new IllegalArgumentException("height: " + height);
        }
        this.height = height;
        return self();
    }

    public final int height() {
        return height;
    }

    public final L size(int width, int height) {
        return width(width).height(height);
    }

    public final L domainX(double x1, double x2) {
        this.x1 = x1;
        this.x2 = x2;
        return self();
    }

    public final L domainY(double y1, double y2) {
        this.y1 = y1;
        this.y2 = y2;
        return self();
    }

    public final L computeFn(Function<? super T, ? extends V> computeFn) {
        this.computeFn = requireNonNull(computeFn, "computeFn");
        return self();
    }

    public final L colorFn(BiFunction<? super T, ? super V, Color> colorFn) {
        this.colorFn = requireNonNull(colorFn, "colorFn");
        return self();
    }

    public final L decoratePanel(Consumer<? super P> decorator) {
        this.panelDecorator = panelDecorator.andThen(decorator);
        return self();
    }

    // Compiler is too stupid to understand that "this" is always a <T>.
    @SuppressWarnings("unchecked")
    protected L self() {
        return (L) this;
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

    private static boolean valid(double... values) {
        return DoubleStream.of(values).allMatch(Double::isFinite);
    }
}
