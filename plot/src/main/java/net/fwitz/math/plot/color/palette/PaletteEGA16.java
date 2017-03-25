package net.fwitz.math.plot.color.palette;

import java.awt.*;

public class PaletteEGA16 implements Palette {
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

    @Override
    public int size() {
        return COLORS.length;
    }

    @Override
    public Color index(int i) {
        return COLORS[i];
    }
}
