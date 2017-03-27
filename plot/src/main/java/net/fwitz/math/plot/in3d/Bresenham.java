package net.fwitz.math.plot.in3d;

import java.util.Arrays;

public class Bresenham {
    public static int[] bresenham(int a, int b, int steps) {
        if (a <= b) {
            return bresenham(a, b, steps, x -> x);
        }
        final int limit = steps - 1;
        return (a <= b) ? bresenham(a, b, steps, x -> x) : bresenham(b, a, steps, x -> limit - x);
    }

    private static int[] bresenham(int a, int b, int steps, IntToInt xMap) {
        final int[] result = new int[steps];
        if (steps == 0) {
            return result;
        }
        if (a == b) {
            Arrays.fill(result, a);
            return result;
        }
        int dy = b - a;
        int d = 2 * dy - steps;
        int y = a;
        for (int x = 0; x < steps; ++x) {
            result[xMap.apply(x)] = y;
            if (d > 0) {
                ++y;
                d -= 2 * steps;
            }
            d += 2 * dy;
        }
        return result;
    }

    public static int[][] multiBresenham(int[] a, int[] b, int steps) {
        if (a.length != b.length) {
            throw new IllegalArgumentException("a.length != b.length: " + a.length + " != " + b.length);
        }
        int[] aa = new int[a.length];
        int[] bb = new int[b.length];
        IntToInt[] xMap = new IntToInt[a.length];
        int limit = steps - 1;
        for (int i = 0; i < a.length; ++i) {
            if (a[i] <= b[i]) {
                aa[i] = a[i];
                bb[i] = b[i];
                xMap[i] = x -> x;
            } else {
                aa[i] = b[i];
                bb[i] = a[i];
                xMap[i] = x -> limit - x;
            }
        }
        return multiBresenham(aa, bb, steps, xMap);
    }

    private static int[][] multiBresenham(int[] a, int[] b, int steps, IntToInt[] xMap) {
        final int[][] result = new int[a.length][];
        for (int i = 0; i < a.length; ++i) {
            result[i] = new int[steps];
        }

        if (steps == 0) {
            return result;
        }

        int[] dy = new int[a.length];
        int[] d = new int[a.length];
        int[] y = a.clone();
        for (int i = 0; i < a.length; ++i) {
            dy[i] = b[i] - a[i];
            d[i] = 2 * dy[i] - steps;
        }

        for (int x = 0; x < steps; ++x) {
            for (int i = 0; i < a.length; ++i) {
                result[xMap[i].apply(x)] = y;
                if (d[i] > 0) {
                    ++y[i];
                    d[i] -= 2 * steps;
                }
                d[i] += 2 * dy[i];
            }
        }
        return result;
    }

    private interface IntToInt {
        int apply(int x);
    }
}
