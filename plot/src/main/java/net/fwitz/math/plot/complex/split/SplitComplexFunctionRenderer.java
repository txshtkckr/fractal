package net.fwitz.math.plot.complex.split;


import net.fwitz.math.complex.SplitComplex;
import net.fwitz.math.numth.numbers.Randomizer;
import net.fwitz.math.plot.renderer.ImageRenderer;
import net.fwitz.math.plot.complex.filter.ValuesFilter;
import net.fwitz.math.plot.complex.filter.ValuesFilters;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicReferenceArray;
import java.util.stream.IntStream;

import static java.util.Objects.requireNonNull;
import static net.fwitz.math.complex.SplitComplex.splitComplex;

public class SplitComplexFunctionRenderer<V> extends ImageRenderer {
    private final double[] xScale;
    private final double[] yScale;

    private final Class<V> valueType;
    private final AtomicReferenceArray<V[]> values;
    private final ValuesFilters<V> valuesFilters;
    private final AbstractSplitComplexFunctionPanel<V> panel;

    private volatile int valuesFilterIndex = -1;
    private volatile ValuesFilter<V> valuesFilter = ValuesFilter.identity();

    public SplitComplexFunctionRenderer(AbstractSplitComplexFunctionPanel<V> panel, Class<V> valueType, int width, int height,
                                   ValuesFilters<V> valuesFilters) {
        super(width, height);

        this.panel = requireNonNull(panel, "panel");
        this.valueType = requireNonNull(valueType, "valueType");
        this.xScale = partition(width, panel.minX, panel.maxX);
        this.yScale = partition(height, panel.maxY, panel.minY);  // inverted
        this.values = new AtomicReferenceArray<>(height);
        this.valuesFilters = requireNonNull(valuesFilters);
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

        double yPart = yScale[y];
        for (int x = 0; x < xScale.length; ++x) {
            final double xPart = xScale[x];
            final SplitComplex z = splitComplex(xPart, yPart);
            values[x] = panel.computeFunction.apply(z);
        }
        return values;
    }

    private int[] calculateRowColors(int y, V[] values) {
        double yPart = yScale[y];
        final V[] filteredValues = valuesFilter.filter(xScale, yPart, values);
        final int[] colors = new int[xScale.length];

        for (int x = 0; x < xScale.length; ++x) {
            final double xPart = xScale[x];
            final SplitComplex c = splitComplex(xPart, yPart);
            final V value = filteredValues[x];
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

    void filterModeForward() {
        final int size = valuesFilters.size();
        if (size == 0) {
            return;
        }

        int index = this.valuesFilterIndex + 1;
        if (index >= size) {
            this.valuesFilterIndex = -1;
            this.valuesFilter = ValuesFilter.identity();
        } else {
            this.valuesFilterIndex = index;
            this.valuesFilter = valuesFilters.filter(index);
        }

        onColorFunctionChanged();
    }

    void filterModeBackward() {
        final int size = valuesFilters.size();
        if (size == 0) {
            return;
        }

        int index = this.valuesFilterIndex - 1;
        if (index == -1) {
            this.valuesFilterIndex = -1;
            this.valuesFilter = ValuesFilter.identity();
        } else {
            if (index < 0) {
                index = size - 1;
            }
            this.valuesFilterIndex = index;
            this.valuesFilter = valuesFilters.filter(index);
        }

        onColorFunctionChanged();
    }

    protected void applyRowColors(int y, int[] colors) {
        image.setRGB(0, y, colors.length, 1, colors, 0, 0);
    }

    @Override
    public String toString() {
        return "SplitComplexFunctionRenderer[" + super.toString() + ']';
    }
}
