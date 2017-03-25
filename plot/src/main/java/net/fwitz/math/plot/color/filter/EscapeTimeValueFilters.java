package net.fwitz.math.plot.color.filter;

import net.fwitz.math.fractal.escape.EscapeTimeResult;

public class EscapeTimeValueFilters extends ValueFilters<EscapeTimeResult> {
    private static final EscapeTimeValueFilters INSTANCE = new EscapeTimeValueFilters();

    public static EscapeTimeValueFilters getInstance() {
        return INSTANCE;
    }

    private EscapeTimeValueFilters() {
        super();
    }
}
