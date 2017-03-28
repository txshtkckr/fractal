package net.fwitz.math.plot.complex.split;


import net.fwitz.math.complex.SplitComplex;
import net.fwitz.math.plot.renderer.ImageRenderer;
import net.fwitz.math.plot.renderer.ImageRendererPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.DoubleStream;

import static java.util.Objects.requireNonNull;

public abstract class AbstractSplitComplexFunctionPanel<V>
        extends ImageRendererPanel<SplitComplexFunctionRenderer<V>>
        implements MouseListener {

    protected final Class<V> valueType;

    final Function<SplitComplex, V> computeFunction;
    final BiFunction<SplitComplex, V, Color> colorFunction;

    volatile double minX;
    volatile double maxX;
    volatile double minY;
    volatile double maxY;

    public AbstractSplitComplexFunctionPanel(
            Class<V> valueType,
            AbstractSplitComplexFunctionPlot<?, ?, V> plot) {
        this.valueType = requireNonNull(valueType, "valueType");
        this.computeFunction = requireNonNull(plot.computeFn, "computeFn");
        this.colorFunction = requireNonNull(plot.colorFn, "colorFn");

        setBounds(plot.domainBound1, plot.domainBound2);
        addListeners();
    }

    @Override
    protected abstract SplitComplexFunctionRenderer<V> createRenderer(int width, int height);

    public Optional<SplitComplex> getMouseLocationAsSplitComplexValue() {
        return getMouseLocationAsSplitComplexValue(Optional.empty());
    }

    public Optional<SplitComplex> getMouseLocationAsSplitComplexValue(Optional<MouseEvent> e) {
        return getMouseLocationAsRelativePoint(e).map(loc -> splitComplex(
                scaleToBounds(loc.getX(), minX, maxX),
                scaleToBounds(1 - loc.getY(), minY, maxY)));
    }

    private void setBounds(SplitComplex domainBound1, SplitComplex domainBound2) {
        setBounds(domainBound1.x(), domainBound1.y(), domainBound2.x(), domainBound2.y());
    }

    private void setBounds(double x1, double y1, double x2, double y2) {
        if (!valid(x1, y1, x2, y2)) {
            throw new IllegalArgumentException("Invalid bounds: (" + x1 + ", " + y1 + ") - (" +
                    x2 + ", " + y2 + ')');
        }

        if (x1 > x2) {
            this.minX = x2;
            this.maxX = x1;
        } else {
            this.minX = x1;
            this.maxX = x2;
        }
        if (y1 > y2) {
            this.minY = y2;
            this.maxY = y1;
        } else {
            this.minY = y1;
            this.maxY = y2;
        }
    }


    public void zoomIn() {
        double xAdjust = (maxX - minX) / 4;
        double yAdjust = (maxY - minY) / 4;
        setBounds(minX + xAdjust, minY + yAdjust, maxX - xAdjust, maxY - yAdjust);
        reset();
    }

    public void zoomOut() {
        double xAdjust = (maxX - minX) / 2;
        double yAdjust = (maxY - minY) / 2;
        setBounds(minX - xAdjust, minY - yAdjust, maxX + xAdjust, maxY + yAdjust);
        reset();
    }

    public void center(SplitComplex z) {
        double xDelta = maxX - minX;
        double yDelta = maxY - minY;
        double xOffset = z.x() - xDelta / 2;
        double yOffset = z.y() - yDelta / 2;
        setBounds(xOffset, yOffset, xOffset + xDelta, yOffset + yDelta);
        reset();
    }

    private static boolean valid(double... values) {
        return DoubleStream.of(values).allMatch(Double::isFinite);
    }

    private void addListeners() {
        final InputMap keyMap = getInputMap();
        keyMap.put(KeyStroke.getKeyStroke('q'), "quit");
        keyMap.put(KeyStroke.getKeyStroke('\u001B'), "quit");
        keyMap.put(KeyStroke.getKeyStroke('m'), "filterModeForward");
        keyMap.put(KeyStroke.getKeyStroke('M'), "filterModeBackward");
        keyMap.put(KeyStroke.getKeyStroke(']'), "zoomIn");
        keyMap.put(KeyStroke.getKeyStroke('['), "zoomOut");

        final ActionMap handlers = getActionMap();
        handlers.put("quit", action(e -> {
            getRenderer().ifPresent(ImageRenderer::shutdown);
            System.exit(0);
        }));
        handlers.put("filterModeForward", action(e -> getRenderer()
                .ifPresent(SplitComplexFunctionRenderer::filterModeForward)));
        handlers.put("filterModeBackward", action(e -> getRenderer()
                .ifPresent(SplitComplexFunctionRenderer::filterModeBackward)));
        handlers.put("zoomIn", action(e -> zoomIn()));
        handlers.put("zoomOut", action(e -> zoomOut()));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1) {
            getMouseLocationAsSplitComplexValue(Optional.of(e))
                    .ifPresent(this::center);
        }
    }
}
