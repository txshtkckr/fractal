package net.fwitz.math.plot.bifurc;

import net.fwitz.math.numth.numbers.Randomizer;
import net.fwitz.math.plot.ImageRenderer;

import java.awt.*;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.stream.DoubleStream;

import static java.util.Objects.requireNonNull;

public class LogisticMapRenderer extends ImageRenderer {
    private final double[] rValues;
    private final int[] blackColumn;
    private final double minxn;
    private final double maxxn;
    private final double xnScale;

    private final AtomicReferenceArray<double[]> values;
    private final LogisticMapPanel panel;

    public LogisticMapRenderer(LogisticMapPanel panel, int width, int height) {
        super(width, height);

        this.panel = requireNonNull(panel, "panel");
        this.rValues = partition(width, panel.minr, panel.maxr);
        if (panel.minxn > panel.maxxn) {
            this.minxn = panel.maxxn;
            this.maxxn = panel.minxn;
        } else {
            this.minxn = panel.minxn;
            this.maxxn = panel.maxxn;
        }
        this.xnScale = height / (this.maxxn - this.minxn);
        this.values = new AtomicReferenceArray<>(width);
        this.blackColumn = new int[height];

        Arrays.fill(blackColumn, Color.BLACK.getRGB());
    }

    public void render() {
        pipeline.flush();
        Arrays.stream(Randomizer.shuffledInts(width))
                .mapToObj(this::colRenderer)
                .forEach(pipeline::execute);
    }

    private Runnable colRenderer(int x) {
        return () -> {
            if (!pipeline.isShutdown()) {
                calculateColWithExceptionsHandled(x);
            }
        };
    }

    private void calculateColWithExceptionsHandled(int x) {
        try {
            final double[] values = calculateColValues(x);
            if (pipeline.isShutdown()) {
                return;
            }

            this.values.set(x, values);
            if (pipeline.isShutdown()) {
                return;
            }

            paintCol(x, values);
            if (pipeline.isShutdown()) {
                return;
            }

            panel.repaint();
        } catch (Exception e) {
            System.err.println("Unexpected exception while rendering x=" + x + "; r=" + rValues[x]);
            e.printStackTrace();
        }
    }

    private double[] calculateColValues(int x) {
        return panel.computeFn.apply(rValues[x]);
    }

    protected void paintCol(int x, double[] values) {
        if (values == null) {
            return;
        }

        image.setRGB(x, 0, 1, height, blackColumn, 0, 0);
        if (values.length == 0) {
            return;
        }

        float val = 1.0f / values.length;
        int color = new Color(1 - val, val, val / 2).getRGB();
        DoubleStream.of(values)
                .mapToInt(xn -> height - (int)Math.round((xn - minxn) * xnScale))
                .filter(y -> y >= 0 && y < height)
                .forEach(y -> image.setRGB(x, y, color));
    }

    @Override
    public String toString() {
        return "ComplexFunctionRenderer[" + super.toString() + ']';
    }
}
