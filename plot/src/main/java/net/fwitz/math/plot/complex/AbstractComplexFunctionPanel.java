package net.fwitz.math.plot.complex;

import net.fwitz.math.complex.Complex;
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
import static net.fwitz.math.complex.Complex.complex;

public abstract class AbstractComplexFunctionPanel<V>
        extends ImageRendererPanel<ComplexFunctionRenderer<V>>
        implements MouseListener {

    protected final Class<V> valueType;

    final Function<Complex, V> computeFunction;
    final BiFunction<Complex, V, Color> colorFunction;

    volatile double minRe;
    volatile double maxRe;
    volatile double minIm;
    volatile double maxIm;

    public AbstractComplexFunctionPanel(
            Class<V> valueType,
            AbstractComplexFunctionPlot<?, ?, V> plot) {
        this.valueType = requireNonNull(valueType, "valueType");
        this.computeFunction = requireNonNull(plot.computeFn, "computeFn");
        this.colorFunction = requireNonNull(plot.colorFn, "colorFn");

        setBounds(plot.domainBound1, plot.domainBound2);
        addListeners();
    }

    @Override
    protected abstract ComplexFunctionRenderer<V> createRenderer(int width, int height);

    public Optional<Complex> getMouseLocationAsComplexValue() {
        return getMouseLocationAsComplexValue(Optional.empty());
    }

    public Optional<Complex> getMouseLocationAsComplexValue(Optional<MouseEvent> e) {
        return getMouseLocationAsRelativePoint(e).map(loc -> complex(
                scaleToBounds(loc.getX(), minRe, maxRe),
                scaleToBounds(1 - loc.getY(), minIm, maxIm)));
    }

    private void setBounds(Complex domainBound1, Complex domainBound2) {
        setBounds(domainBound1.re(), domainBound1.im(), domainBound2.re(), domainBound2.im());
    }

    private void setBounds(double re1, double im1, double re2, double im2) {
        if (!valid(re1, im1, re2, im2)) {
            throw new IllegalArgumentException("Invalid bounds: (" + re1 + ", " + im1 + ") - (" +
                    re2 + ", " + im2 + ')');
        }

        if (re1 > re2) {
            this.minRe = re2;
            this.maxRe = re1;
        } else {
            this.minRe = re1;
            this.maxRe = re2;
        }
        if (im1 > im2) {
            this.minIm = im2;
            this.maxIm = im1;
        } else {
            this.minIm = im1;
            this.maxIm = im2;
        }
    }


    public void zoomIn() {
        double reAdjust = (maxRe - minRe) / 4;
        double imAdjust = (maxIm - minIm) / 4;
        setBounds(minRe + reAdjust, minIm + imAdjust, maxRe - reAdjust, maxIm - imAdjust);
        reset();
    }

    public void zoomOut() {
        double reAdjust = (maxRe - minRe) / 2;
        double imAdjust = (maxIm - minIm) / 2;
        setBounds(minRe - reAdjust, minIm - imAdjust, maxRe + reAdjust, maxIm + imAdjust);
        reset();
    }

    public void center(Complex z) {
        double xDelta = maxRe - minRe;
        double yDelta = maxIm - minIm;
        double xOffset = z.re() - xDelta / 2;
        double yOffset = z.im() - yDelta / 2;
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
                .ifPresent(ComplexFunctionRenderer::filterModeForward)));
        handlers.put("filterModeBackward", action(e -> getRenderer()
                .ifPresent(ComplexFunctionRenderer::filterModeBackward)));
        handlers.put("zoomIn", action(e -> zoomIn()));
        handlers.put("zoomOut", action(e -> zoomOut()));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1) {
            getMouseLocationAsComplexValue(Optional.of(e))
                    .ifPresent(this::center);
        }
    }
}
