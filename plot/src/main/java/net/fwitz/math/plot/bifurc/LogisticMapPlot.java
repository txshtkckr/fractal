package net.fwitz.math.plot.bifurc;

import javax.swing.*;
import java.util.function.Consumer;
import java.util.function.DoubleFunction;

import static java.util.Objects.requireNonNull;

public class LogisticMapPlot {
    private static final int DEFAULT_HEIGHT = 600;
    private static final int DEFAULT_WIDTH = 600;
    private static final double DEFAULT_MINR = 0.0;
    private static final double DEFAULT_MAXR = 4.0;
    private static final double DEFAULT_MINXN = 0.0;
    private static final double DEFAULT_MAXXN = 1.0;

    private final String title;

    protected DoubleFunction<double[]> computeFn;
    protected double minr = DEFAULT_MINR;
    protected double maxr = DEFAULT_MAXR;
    protected double minxn = DEFAULT_MINXN;
    protected double maxxn = DEFAULT_MAXXN;
    protected int width = DEFAULT_WIDTH;
    protected int height = DEFAULT_HEIGHT;

    private Consumer<LogisticMapPanel> panelDecorator = panel -> {
    };

    public LogisticMapPlot(String title) {
        this.title = requireNonNull(title, "title");
    }

    public final LogisticMapPlot domainR(double minr, double maxr) {
        this.minr = minr;
        this.maxr = maxr;
        return this;
    }

    public final LogisticMapPlot minr(double minr) {
        this.minr = minr;
        return this;
    }

    public final LogisticMapPlot maxr(double maxr) {
        this.maxr = maxr;
        return this;
    }

    public final LogisticMapPlot rangeXn(double minxn, double maxxn) {
        this.minxn = minxn;
        this.maxxn = maxxn;
        return this;
    }

    public final LogisticMapPlot minxn(double minxn) {
        this.minxn = minxn;
        return this;
    }

    public final LogisticMapPlot maxxn(double maxxn) {
        this.maxxn = maxxn;
        return this;
    }

    public final LogisticMapPlot width(int width) {
        if (width < 100 || width > 16384) {
            throw new IllegalArgumentException("width: " + width);
        }
        this.width = width;
        return this;
    }

    public final int width() {
        return width;
    }

    public final LogisticMapPlot height(int height) {
        if (height < 100 || height > 16384) {
            throw new IllegalArgumentException("height: " + height);
        }
        this.height = height;
        return this;
    }

    public final int height() {
        return height;
    }

    public final LogisticMapPlot size(int width, int height) {
        return width(width).height(height);
    }

    public final LogisticMapPlot computeFn(DoubleFunction<double[]> computeFn) {
        this.computeFn = requireNonNull(computeFn, "computeFn");
        return this;
    }

    public final LogisticMapPlot decoratePanel(Consumer<? super LogisticMapPanel> decorator) {
        this.panelDecorator = panelDecorator.andThen(decorator);
        return this;
    }

    public final void render() {
        final JFrame frame = new JFrame(title);
        frame.setSize(width(), height());
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        final LogisticMapPanel panel = createPanel();
        frame.setContentPane(panel);

        panelDecorator.accept(panel);
        frame.setVisible(true);
    }

    protected LogisticMapPanel createPanel() {
        return new LogisticMapPanel(minr, minxn, maxr, maxxn, computeFn);
    }
}
