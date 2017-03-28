package net.fwitz.math.plot.palette;

import java.awt.*;
import java.util.stream.IntStream;

// rrrgggbb
public class PaletteVGA8bitRGB implements Palette {
    private static final int[] BITS2 = {
            0x00, 0x48, 0x90, 0xFF
    };
    private static final int[] BITS3 = {
            0x00, 0x24, 0x48, 0x6C,
            0x90, 0xB4, 0xD8, 0xFF
    };

    private static final Color[] COLORS;

    static {
        COLORS = IntStream.range(0, 256)
                .mapToObj(x -> {
                    int r = BITS3[(x >> 5) & 7];
                    int g = BITS3[(x >> 2) & 7];
                    int b = BITS2[x & 3];
                    return new Color(r, g, b);
                })
                .toArray(Color[]::new);
    }

    @Override
    public int size() {
        return COLORS.length;
    }

    @Override
    public Color index(int i) {
        return COLORS[i];
    }
}
