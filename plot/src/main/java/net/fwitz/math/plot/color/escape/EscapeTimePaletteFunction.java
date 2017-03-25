package net.fwitz.math.plot.color.escape;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.main.escape.EscapeTimeResult;
import net.fwitz.math.plot.color.palette.Palette;

import java.awt.*;

import static java.util.Objects.requireNonNull;

public class EscapeTimePaletteFunction implements EscapeTimeColorFunction {
    private final Palette palette;

    public static EscapeTimeColorFunction escapeTime(Palette palette) {
        return new EscapeTimePaletteFunction(palette);
    }

    private EscapeTimePaletteFunction(Palette palette) {
        this.palette = requireNonNull(palette, "palette");
    }

    @Override
    public Color apply(Complex c, EscapeTimeResult result) {
        if (result.escaped()) {
            int max = palette.size() - 1;
            int index = result.iters() % max;
            return palette.index((index > 0) ? index : max);
        }

        return palette.index(0);
    }
}
