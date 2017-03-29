package net.fwitz.math.plot.binary;

import net.fwitz.math.complex.BinaryNumber;
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

public abstract class BinaryNumberFunctionPanel<T extends BinaryNumber<T>, V>
        extends ImageRendererPanel<BinaryNumberFunctionRenderer<T, V>>
        implements MouseListener {

    protected final Class<T> binaryNumberType;
    protected final Class<V> valueType;
    protected final BinaryNumberFunctionPlot<?, ?, T, V> plot;

    final Function<? super T, ? extends V> computeFunction;
    final BiFunction<? super T, ? super V, Color> colorFunction;

    volatile double minX;
    volatile double maxX;
    volatile double minY;
    volatile double maxY;

    public BinaryNumberFunctionPanel(
            Class<T> binaryNumberType,
            Class<V> valueType,
            BinaryNumberFunctionPlot<?, ?, T, V> plot) {
        this.binaryNumberType = requireNonNull(binaryNumberType, "binaryNumberType");
        this.valueType = requireNonNull(valueType, "valueType");
        this.plot = requireNonNull(plot, "plot");
        this.computeFunction = requireNonNull(plot.computeFn, "computeFn");
        this.colorFunction = requireNonNull(plot.colorFn, "colorFn");

        setBounds(plot.x1, plot.y1, plot.x2, plot.y2);
        addListeners();
    }

    @Override
    protected abstract BinaryNumberFunctionRenderer<T, V> createRenderer(int width, int height);

    public Optional<T> getMouseLocationAsValue() {
        return getMouseLocationAsValue(Optional.empty());
    }

    public Optional<T> getMouseLocationAsValue(Optional<MouseEvent> e) {
        return getMouseLocationAsRelativePoint(e).map(loc -> plot.value(
                scaleToBounds(loc.getX(), minX, maxX),
                scaleToBounds(1 - loc.getY(), minY, maxY)));
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

    public void center(BinaryNumber z) {
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
                .ifPresent(BinaryNumberFunctionRenderer::filterModeForward)));
        handlers.put("filterModeBackward", action(e -> getRenderer()
                .ifPresent(BinaryNumberFunctionRenderer::filterModeBackward)));
        handlers.put("zoomIn", action(e -> zoomIn()));
        handlers.put("zoomOut", action(e -> zoomOut()));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1) {
            getMouseLocationAsValue(Optional.of(e))
                    .ifPresent(this::center);
        }
    }
}
