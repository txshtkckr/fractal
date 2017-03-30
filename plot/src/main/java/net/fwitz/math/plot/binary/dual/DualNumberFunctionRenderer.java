package net.fwitz.math.plot.binary.dual;

import net.fwitz.math.binary.dual.DualNumber;
import net.fwitz.math.plot.binary.BinaryNumberFunctionPanel;
import net.fwitz.math.plot.binary.BinaryNumberFunctionRenderer;
import net.fwitz.math.plot.renderer.filter.ValuesFilters;

import static net.fwitz.math.binary.dual.DualNumber.dualNumber;

public class DualNumberFunctionRenderer<V> extends BinaryNumberFunctionRenderer<DualNumber, V> {
    public DualNumberFunctionRenderer(BinaryNumberFunctionPanel<DualNumber, V> panel, Class<V> valueType,
                                      int width, int height,
                                      ValuesFilters<V> valuesFilters) {
        super(panel, valueType, width, height, valuesFilters);
    }

    @Override
    protected DualNumber value(double x, double y) {
        return dualNumber(x, y);
    }
}
