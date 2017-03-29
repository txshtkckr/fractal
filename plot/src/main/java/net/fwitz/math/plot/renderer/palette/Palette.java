package net.fwitz.math.plot.renderer.palette;

import java.awt.*;

public interface Palette {
    int size();

    Color index(int i);
}
