package net.fwitz.math.plot.renderer.palette;

import java.awt.*;

public interface Palette {
    /** The number of colors included in this palette. */
    int size();

    /**
     * Selects a color by index, without wrapping.
     *
     * @param index the color index, which must be in the range {@code [0, size)}; that is, it must be non-negative
     *              and less than {@link #size()}.
     * @return the selected color
     * @throws IndexOutOfBoundsException if the supplied index is out of range
     */
    Color index(int index);

    /**
     * Returns a color from the palette, using all available colors and wrapping as needed.
     *
     * @param index the (non-negative) color index
     * @return the corresponding color
     */
    default Color indexAny(int index) {
        return index(index % size());
    }

    /**
     * Returns a color from the palette, using all available colors except the one at index {@code 0} and wrapping
     * as needed.
     *
     * @param index the (non-negative) color index
     * @return the corresponding color
     */
    default Color indexExcluding0(int index) {
        int max = size() - 1;
        int n = index % max;
        return index((n != 0) ? n : max);
    }

    /**
     * Returns a color constructed from the palette by interpolating between the color that would be selected
     * by {@link #indexAny(int)} using {@link Math#floor(double) floor(x)} and the next color in the palette,
     * wrapping back around to index {@code 0} if necessary.
     *
     * @param x the color index, with the integer portion representing the first color's index as per
     * {@link #index(int)} and the mantissa indicating the amount of the next color that should be mixed in
     * @return the interpolated color value
     */
    default Color interpolateAny(double x) {
        int n = ((int) Math.floor(x));
        double mantissa = x - n;
        n %= size();
        int nPlus1 = n + 1;

        return Interpolate.interpolate(
                index(n),
                index((nPlus1 != size()) ? nPlus1 : 0),
                mantissa);
    }

    /**
     * Returns a color constructed from the palette by interpolating between the color that would be selected
     * by {@link #indexExcluding0(int)} (int)} using {@link Math#floor(double) floor(x)} and the next color
     * in the palette, wrapping back around to index {@code 1} if necessary.
     *
     * @param x the color index, with the integer portion representing the first color's index as per
     * {@link #index(int)} and the mantissa indicating the amount of the next color that should be mixed in
     * @return the interpolated color value
     */
    default Color interpolateExcluding0(double x) {
        int max = size() - 1;
        int n = ((int) Math.floor(x));
        double mantissa = x - n;
        n %= max;

        return Interpolate.interpolate(
                index((n > 0) ? n : max),
                index(n + 1),
                mantissa);
    }
}
