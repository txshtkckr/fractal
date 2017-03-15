package net.fwitz.math.plot;

import net.fwitz.math.complex.Complex;

import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static net.fwitz.math.complex.Complex.complex;

public class ImageRenderer extends Thread {
    private static final AtomicInteger COUNTER = new AtomicInteger();

    private final double[] xScale;
    private final double[] yScale;

    private final FunctionPanel functionPanel;
    final int width;
    final int height;
    final BufferedImage image;

    volatile boolean cancelled;

    ImageRenderer(FunctionPanel functionPanel, int width, int height) {
        super("ImageRenderer-" + COUNTER.incrementAndGet() + ": " + width + 'x' + height);
        this.functionPanel = functionPanel;

        this.width = width;
        this.height = height;
        this.xScale = partition(width, functionPanel.minRe, functionPanel.maxRe);
        this.yScale = partition(height, functionPanel.maxIm, functionPanel.minIm);  // inverted
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    }

    @Override
    public void run() {
        IntStream.range(0, height).parallel()
                .filter(row -> keepGoing())
                .forEach(this::renderRowWithExceptionsHandled);
    }

    private boolean keepGoing() {
        if (cancelled) {
            return false;
        }
        if (ImageRenderer.this.isInterrupted()) {
            cancelled = true;
            return false;
        }
        return true;
    }

    private void renderRowWithExceptionsHandled(int y) {
        try {
            renderRow(y);
        } catch (Exception e) {
            System.err.println("Unexpected exception while rendering y=" + y);
            e.printStackTrace();
        }
    }

    private void renderRow(int y) {
        double immPart = yScale[y];
        final Complex[] values = new Complex[xScale.length];
        final int[] colors = new int[xScale.length];

        for (int x = 0; x < xScale.length; ++x) {
            final double realPart = xScale[x];
            final Complex z = complex(realPart, immPart);
            values[x] = functionPanel.computeFunction.apply(z);
            colors[x] = functionPanel.colorFunction.apply(z, values[x]).getRGB();
        }
        onRowComplete(y, values, colors);
    }

    protected void onRowComplete(int y, Complex[] values, int[] colors) {
        image.setRGB(0, y, colors.length, 1, colors, 0, 0);
        functionPanel.repaint();
    }

    private double[] partition(int buckets, double valueMin, double valueMax) {
        double valueDomain = valueMax - valueMin;
        return IntStream.range(0, buckets)
                .mapToDouble(bucket -> valueMin + valueDomain * (bucket + 0.5) / buckets)
                .toArray();
    }
}
