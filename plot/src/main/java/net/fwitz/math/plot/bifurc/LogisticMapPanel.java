package net.fwitz.math.plot.bifurc;

import net.fwitz.math.plot.ImageRenderer;
import net.fwitz.math.plot.ImageRendererPanel;

import javax.swing.*;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.Optional;
import java.util.function.DoubleFunction;
import java.util.stream.DoubleStream;

import static java.util.Objects.requireNonNull;

public class LogisticMapPanel extends ImageRendererPanel<LogisticMapRenderer> {
    volatile double minr;
    volatile double maxr;
    volatile double minxn;
    volatile double maxxn;

    final DoubleFunction<double[]> computeFn;

    public LogisticMapPanel(double minr, double minxn, double maxr, double maxxn, DoubleFunction<double[]> computeFn) {
        this.computeFn = requireNonNull(computeFn, "computeFn");
        setBounds(minr, minxn, maxr, maxxn);
        addListeners();
    }

    @Override
    protected LogisticMapRenderer createRenderer(int width, int height) {
        return new LogisticMapRenderer(this, width, height);
    }

    private void addListeners() {
        final InputMap keyMap = getInputMap();
        keyMap.put(KeyStroke.getKeyStroke('q'), "quit");
        keyMap.put(KeyStroke.getKeyStroke('\u001B'), "quit");
        keyMap.put(KeyStroke.getKeyStroke(']'), "zoomIn");
        keyMap.put(KeyStroke.getKeyStroke('['), "zoomOut");

        final ActionMap handlers = getActionMap();
        handlers.put("quit", action(e -> {
            getRenderer().ifPresent(ImageRenderer::shutdown);
            System.exit(0);
        }));
        handlers.put("zoomIn", action(e -> zoomIn()));
        handlers.put("zoomOut", action(e -> zoomOut()));
    }

    public void zoomIn() {
        double rAdjust = (maxr - minr) / 4;
        double xnAdjust = (maxxn - minxn) / 4;
        setBounds(minr + rAdjust, minxn + xnAdjust, maxr - rAdjust, maxxn - xnAdjust);
        reset();
    }

    public void zoomOut() {
        double rAdjust = (maxr - minr) / 2;
        double xnAdjust = (maxxn - minxn) / 2;
        setBounds(minr - rAdjust, minxn - xnAdjust, maxr + rAdjust, maxxn + xnAdjust);
        reset();
    }

    public void center(Point2D.Double location) {
        double rDelta = maxr - minr;
        double xnDelta = maxxn - minxn;
        double rOffset = location.getX() - rDelta / 2;
        double xnOffset = location.getY() - xnDelta / 2;
        setBounds(rOffset, xnOffset, rOffset + rDelta, xnOffset + xnDelta);
        reset();
    }

    private static boolean valid(double... values) {
        return DoubleStream.of(values).allMatch(Double::isFinite);
    }

    private void setBounds(double minr, double minxn, double maxr, double maxxn) {
        if (!valid(minr, minxn, maxr, maxxn)) {
            throw new IllegalArgumentException("Invalid bounds: (" + minr + ", " + minxn + ") - (" +
                    maxr + ", " + maxxn + ')');
        }

        if (minr > maxr) {
            this.minr = maxr;
            this.maxr = minr;
        } else {
            this.minr = minr;
            this.maxr = maxr;
        }

        if (minxn > maxxn) {
            this.minxn = maxxn;
            this.maxxn = minxn;
        } else {
            this.minxn = minxn;
            this.maxxn = maxxn;
        }
    }

    // Glue code to make AbstractAction suck less by looking like a functional interface

    public Optional<Point2D.Double> getMouseLocationInMap() {
        return getMouseLocationInMap(Optional.empty());
    }

    public Optional<Point2D.Double> getMouseLocationInMap(Optional<MouseEvent> e) {
        return getMouseLocationAsRelativePoint(e).map(loc -> new Point2D.Double(
                scaleToBounds(loc.getX(), minr, maxr),
                scaleToBounds(1 - loc.getY(), minxn, maxxn)));
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (e.getButton() == 1) {
            getMouseLocationInMap(Optional.of(e))
                    .ifPresent(this::center);
        }
    }
}
