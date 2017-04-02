package net.fwitz.math.plot.renderer.palette;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class EscapeTimePalette implements Palette {
    private static final Color[] COLORS;
    static {
        final List<Color> colors = new ArrayList<>(256);
        colors.add(Color.BLACK);
        for (int b = 0x40; b < 0x100; b += 0x10) {
            colors.add(new Color(0x00, 0x00, b));
        }
        colors.add(new Color(0, 0, 0xFF));
        for (int rg = 0; rg < 0x100; rg += 0x10) {
            colors.add(new Color(rg, rg, 0xFF));
        }
        colors.add(Color.WHITE);
        for (int b = 0xF0; b >= 0; b -= 0x10) {
            colors.add(new Color(0xFF, 0xFF, b));
        }
        for (int g = 0xF0; g >= 0; g -= 0x10) {
            colors.add(new Color(0xFF, g, 0x00));
        }
        for (int r = 0xF0, b = 0x04; r > 0; r -= 0x10, b += 0x04) {
            colors.add(new Color(r, 0x00, b));
        }
        COLORS = colors.toArray(new Color[colors.size()]);
    }

    @Override
    public int size() {
        return COLORS.length;
    }

    @Override
    public Color index(int index) {
        return COLORS[index];
    }
}
