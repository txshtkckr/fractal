package net.fwitz.math.plot.color.palette;

import net.fwitz.math.plot.color.ColorFunction;

import java.awt.*;

public class PaletteEGA16 {
    private static final Color[] COLORS = {
            new Color(0x00, 0x00, 0x00),
            new Color(0x00, 0x00, 0xAA),
            new Color(0x00, 0xAA, 0x00),
            new Color(0x00, 0xAA, 0xAA),
            new Color(0xAA, 0x00, 0x00),
            new Color(0xAA, 0x00, 0xAA),
            new Color(0xAA, 0x55, 0x00),
            new Color(0xAA, 0xAA, 0xAA),
            new Color(0x55, 0x55, 0x55),
            new Color(0x55, 0x55, 0xFF),
            new Color(0x55, 0xFF, 0x55),
            new Color(0x55, 0xFF, 0xFF),
            new Color(0xFF, 0x55, 0x55),
            new Color(0xFF, 0x55, 0xFF),
            new Color(0xFF, 0xFF, 0x55),
            new Color(0xFF, 0xFF, 0xFF)
    };

    public static ColorFunction escapeTime() {
        return (c, z) -> {
            if (z.isNaN()) {
                return COLORS[0];
            }

            int iters = (int) Math.round(z.re());
            int colorIndex = iters % 15;
            return COLORS[(colorIndex != 0) ? colorIndex : 15];
        };
    }
}
