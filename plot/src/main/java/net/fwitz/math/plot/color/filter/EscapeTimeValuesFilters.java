package net.fwitz.math.plot.color.filter;

import net.fwitz.math.fractal.escape.EscapeTimeResult;

public class EscapeTimeValuesFilters extends ValuesFilters<EscapeTimeResult> {
    private static final EscapeTimeValuesFilters INSTANCE = new EscapeTimeValuesFilters();

    public static EscapeTimeValuesFilters getInstance() {
        return INSTANCE;
    }

    private EscapeTimeValuesFilters() {
        super();
    }
}
