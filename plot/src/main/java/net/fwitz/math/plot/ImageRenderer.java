package net.fwitz.math.plot;

import net.fwitz.math.complex.Complex;

import java.awt.image.BufferedImage;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.function.Function;
import java.util.stream.IntStream;

import static net.fwitz.math.complex.Complex.complex;

public class ImageRenderer extends Thread {
    private static final Runnable NO_OP = () -> {
    };

    private static final AtomicInteger COUNTER = new AtomicInteger();

    private final double[] xScale;
    private final double[] yScale;
    private final AtomicReferenceArray<Complex[]> values;
    private final FunctionPanel functionPanel;

    final int width;
    final int height;
    final BufferedImage image;

    private volatile boolean cancelled;
    private volatile Mode mode = Mode.NORMAL;

    ImageRenderer(FunctionPanel functionPanel, int width, int height) {
        super("ImageRenderer-" + COUNTER.incrementAndGet() + ": " + width + 'x' + height);

        this.functionPanel = functionPanel;
        this.width = width;
        this.height = height;
        this.xScale = partition(width, functionPanel.minRe, functionPanel.maxRe);
        this.yScale = partition(height, functionPanel.maxIm, functionPanel.minIm);  // inverted
        this.values = new AtomicReferenceArray<>(height);
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
    }

    @Override
    public void run() {
        IntStream.range(0, height)
                .mapToObj(this::rowRenderer)
                .forEach(RenderingPipeline::execute);
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

    private Runnable rowRenderer(int y) {
        if (keepGoing()) {
            return () -> {
                if (keepGoing()) {
                    calculateRowWithExceptionsHandled(y);
                }
            };
        }
        return NO_OP;
    }

    private void calculateRowWithExceptionsHandled(int y) {
        try {
            final Complex[] values = calculateRowValues(y);
            this.values.set(y, values);
            final int[] colors = calculateRowColors(y, values);
            applyRowColors(y, colors);
            functionPanel.repaint();
        } catch (Exception e) {
            System.err.println("Unexpected exception while rendering y=" + y);
            e.printStackTrace();
        }
    }

    private Complex[] calculateRowValues(int y) {
        double immPart = yScale[y];
        final Complex[] values = new Complex[xScale.length];

        for (int x = 0; x < xScale.length; ++x) {
            final double realPart = xScale[x];
            final Complex z = complex(realPart, immPart);
            values[x] = functionPanel.computeFunction.apply(z);
        }
        return values;
    }

    private int[] calculateRowColors(int y, Complex[] values) {
        Mode mode = this.mode;
        double immPart = yScale[y];
        final int[] colors = new int[xScale.length];

        for (int x = 0; x < xScale.length; ++x) {
            final double realPart = xScale[x];
            final Complex c = complex(realPart, immPart);
            final Complex z = mode.filter(values[x]);
            colors[x] = functionPanel.colorFunction.apply(c, z).getRGB();
        }

        return colors;
    }

    private void onColorFunctionChanged() {
        for (int y = 0; y < yScale.length; ++y) {
            Complex[] values = this.values.get(y);
            if (values != null) {
                final int[] colors = calculateRowColors(y, values);
                applyRowColors(y, colors);
            }
        }
        functionPanel.repaint();
    }

    void toggleMode() {
        final Mode[] modes = Mode.values();
        mode = modes[(mode.ordinal() + 1) % modes.length];
        onColorFunctionChanged();
    }

    protected void applyRowColors(int y, int[] colors) {
        image.setRGB(0, y, colors.length, 1, colors, 0, 0);
    }

    private double[] partition(int buckets, double valueMin, double valueMax) {
        double valueDomain = valueMax - valueMin;
        return IntStream.range(0, buckets)
                .mapToDouble(bucket -> valueMin + valueDomain * (bucket + 0.5) / buckets)
                .toArray();
    }

    public enum Mode {
        NORMAL(z -> z),
        RE_ONLY(z -> z.im(0)),
        IM_ONLY(z -> z.re(0));

        private final Function<Complex, Complex> modeFilterFn;

        Mode(Function<Complex, Complex> modeFilterFn) {
            this.modeFilterFn = modeFilterFn;
        }

        Complex filter(Complex value) {
            return modeFilterFn.apply(value);
        }
    }
}
