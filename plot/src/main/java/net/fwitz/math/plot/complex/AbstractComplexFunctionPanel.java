package net.fwitz.math.plot.complex;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.plot.ImageRenderer;
import net.fwitz.math.plot.ImageRendererPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.DoubleStream;

import static java.util.Objects.requireNonNull;
import static net.fwitz.math.complex.Complex.complex;

public abstract class AbstractComplexFunctionPanel<V>
        extends ImageRendererPanel<AbstractComplexFunctionPanel<V>, ComplexFunctionRenderer<V>>
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
        return getMouseLocationAsRelativePoint().map(loc -> complex(
                scaleToBounds(loc.re(), minRe, maxRe),
                scaleToBounds(1 - loc.im(), minIm, maxIm)));
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
        keyMap.put(KeyStroke.getKeyStroke('m'), "mode");
        keyMap.put(KeyStroke.getKeyStroke(']'), "zoomIn");
        keyMap.put(KeyStroke.getKeyStroke('['), "zoomOut");

        final ActionMap handlers = getActionMap();
        handlers.put("quit", action(e -> {
            getRenderer().ifPresent(ImageRenderer::shutdown);
            System.exit(0);
        }));
        handlers.put("mode", action(e -> getRenderer()
                .ifPresent(ComplexFunctionRenderer::toggleMode)));
        handlers.put("zoomIn", action(e -> zoomIn()));
        handlers.put("zoomOut", action(e -> zoomOut()));

        addMouseListener(this);
    }

    // Glue code to make AbstractAction suck less by looking like a functional interface

    public static AbstractAction action(ActionHandler handler) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.actionPerformed(e);
            }
        };
    }

    @FunctionalInterface
    public interface ActionHandler {
        void actionPerformed(ActionEvent e);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        getMouseLocationAsComplexValue().ifPresent(this::center);
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }
}
