package net.fwitz.math.plot.renderer.palette;

import java.awt.*;

public class Interpolate {
    public static Color interpolate(Color c1, Color c2, double x) {
        return new Color(
                interpolate(c1.getRed(), c2.getRed(), x),
                interpolate(c1.getGreen(), c2.getGreen(), x),
                interpolate(c1.getBlue(), c2.getBlue(), x));
    }

    public static double interpolate(double c1, double c2, double x) {
        return c1 + ((c2 - c1) * x);
    }

    public static int interpolate(int c1, int c2, double x) {
        return c1 + (int) Math.round((c2 - c1) * x);
    }
}
