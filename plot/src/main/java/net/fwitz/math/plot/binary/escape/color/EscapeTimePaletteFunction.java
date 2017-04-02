package net.fwitz.math.plot.binary.escape.color;

import net.fwitz.math.binary.complex.Complex;
import net.fwitz.math.fractal.escape.EscapeTimeResult;
import net.fwitz.math.plot.renderer.palette.Palette;

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
        return result.escaped() ? palette.indexExcluding0(result.iters()) : palette.index(0);
    }
}
