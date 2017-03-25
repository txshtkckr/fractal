package net.fwitz.math.plot.complex;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.numth.numbers.Randomizer;
import net.fwitz.math.plot.ImageRenderer;
import net.fwitz.math.plot.color.filter.ValueFilter;
import net.fwitz.math.plot.color.filter.ValueFilters;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;
import static net.fwitz.math.complex.Complex.complex;

public class ComplexFunctionRenderer<V> extends ImageRenderer {
    private final double[] xScale;
    private final double[] yScale;

    private final Class<V> valueType;
    private final AtomicReferenceArray<V[]> values;
    private final ValueFilters<V> valueFilters;
    private final AbstractComplexFunctionPanel<V> panel;

    private volatile int valueFilterIndex = -1;
    private volatile ValueFilter<V> valueFilter = ValueFilter.identity();

    public ComplexFunctionRenderer(AbstractComplexFunctionPanel<V> panel, Class<V> valueType, int width, int height,
                                   ValueFilters<V> valueFilters) {
        super(width, height);

        this.panel = requireNonNull(panel, "panel");
        this.valueType = requireNonNull(valueType, "valueType");
        this.xScale = partition(width, panel.minRe, panel.maxRe);
        this.yScale = partition(height, panel.maxIm, panel.minIm);  // inverted
        this.values = new AtomicReferenceArray<>(height);
        this.valueFilters = requireNonNull(valueFilters);
    }

    public void render() {
        pipeline.flush();
        Arrays.stream(Randomizer.shuffledInts(height))
                .mapToObj(this::rowRenderer)
                .forEach(pipeline::execute);
    }

    private Runnable rowRenderer(int y) {
        return () -> {
            if (!pipeline.isShutdown()) {
                calculateRowWithExceptionsHandled(y);
            }
        };
    }

    private void calculateRowWithExceptionsHandled(int y) {
        try {
            final V[] values = calculateRowValues(y);
            if (pipeline.isShutdown()) {
                return;
            }

            this.values.set(y, values);
            final int[] colors = calculateRowColors(y, values);
            if (pipeline.isShutdown()) {
                return;
            }

            applyRowColors(y, colors);
            if (pipeline.isShutdown()) {
                return;
            }

            panel.repaint();
        } catch (Exception e) {
            System.err.println("Unexpected exception while rendering y=" + y);
            e.printStackTrace();
        }
    }

    private V[] calculateRowValues(int y) {
        @SuppressWarnings("unchecked")
        final V[] values = (V[]) Array.newInstance(valueType, xScale.length);

        double immPart = yScale[y];
        for (int x = 0; x < xScale.length; ++x) {
            final double realPart = xScale[x];
            final Complex z = complex(realPart, immPart);
            values[x] = panel.computeFunction.apply(z);
        }
        return values;
    }

    private int[] calculateRowColors(int y, V[] values) {
        ValueFilter<V> filter = valueFilter;
        double immPart = yScale[y];
        final int[] colors = new int[xScale.length];

        for (int x = 0; x < xScale.length; ++x) {
            final double realPart = xScale[x];
            final Complex c = complex(realPart, immPart);
            final V value = filter.apply(values[x]);
            colors[x] = panel.colorFunction.apply(c, value).getRGB();
        }

        return colors;
    }

    private void onColorFunctionChanged() {
        IntStream.range(0, yScale.length).forEach(y -> {
            V[] values = this.values.get(y);
            if (values != null) {
                final int[] colors = calculateRowColors(y, values);
                applyRowColors(y, colors);
            }
        });
        panel.repaint();
    }

    void toggleMode() {
        final int size = valueFilters.size();
        if (size == 0) {
            return;
        }

        int index = this.valueFilterIndex + 1;
        if (index >= size) {
            this.valueFilterIndex = -1;
            this.valueFilter = ValueFilter.identity();
        } else {
            this.valueFilterIndex = index;
            this.valueFilter = valueFilters.filter(index);
        }

        onColorFunctionChanged();
    }

    protected void applyRowColors(int y, int[] colors) {
        image.setRGB(0, y, colors.length, 1, colors, 0, 0);
    }

    @Override
    public String toString() {
        return "ComplexFunctionRenderer[" + super.toString() + ']';
    }
}
