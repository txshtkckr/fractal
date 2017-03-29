package net.fwitz.math.plot.binary.escape;

import net.fwitz.math.complex.BinaryNumber;
import net.fwitz.math.fractal.escape.EscapeTimeResult;
import net.fwitz.math.plot.binary.BinaryNumberFunctionPanel;
import net.fwitz.math.plot.binary.BinaryNumberFunctionRenderer;

public class EscapeTimePanel<T extends BinaryNumber<T>> extends BinaryNumberFunctionPanel<T , EscapeTimeResult> {
    EscapeTimePanel(Class<T> binaryNumberType, EscapeTimePlot<T> plot) {
        super(binaryNumberType, EscapeTimeResult.class, plot);
    }

    @Override
    protected BinaryNumberFunctionRenderer<T, EscapeTimeResult> createRenderer(int width, int height) {
        return new Renderer(this, width, height);
    }

    class Renderer extends BinaryNumberFunctionRenderer<T, EscapeTimeResult> {
        Renderer(BinaryNumberFunctionPanel<T, EscapeTimeResult> panel, int width, int height) {
            super(panel, EscapeTimeResult.class, width, height, EscapeTimeValuesFilters.getInstance());
        }

        @Override
        protected T value(double x, double y) {
            return plot.value(x, y);
        }
    }
}
