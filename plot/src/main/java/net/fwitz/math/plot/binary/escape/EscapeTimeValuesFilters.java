package net.fwitz.math.plot.binary.escape;

import net.fwitz.math.fractal.escape.EscapeTimeResult;
import net.fwitz.math.plot.renderer.filter.ValuesFilters;

public class EscapeTimeValuesFilters extends ValuesFilters<EscapeTimeResult> {
    private static final EscapeTimeValuesFilters INSTANCE = new EscapeTimeValuesFilters();

    public static EscapeTimeValuesFilters getInstance() {
        return INSTANCE;
    }

    private EscapeTimeValuesFilters() {
        super();
    }
}
