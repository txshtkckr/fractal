package net.fwitz.math.fractal.bifurc;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.IntStream;

public class BifurcLogisticMapFunction {
    private static final int WARM_UP = 900;
    private static final int SAMPLES = 100;

    public static double[] evaluate(double r) {
        if (r < 0 || r > 4) {
            return null;
        }

        double x = 0.1;
        for (int i = 1; i <= WARM_UP; ++i) {
            x = r * x * (1 - x);
        }

        Set<Double> set = new HashSet<>(SAMPLES);
        for (int i = 1; i <= SAMPLES; ++i) {
            x = r * x * (1 - x);
            set.add(Math.floor(1e6 * x) / 1e6);
        }

        return set.stream()
                .mapToDouble(Double::doubleValue)
                .toArray();
    }

    public static void main(String[] args) {
        IntStream.range(0, 410)
                .mapToDouble(r100 -> (double) r100 / 100)
                .forEach(r -> {
                    double[] values = evaluate(r);
                    if (values == null) {
                        System.out.format("%3.2f: (out-of-bounds)", r);
                    } else {
                        System.out.format("%3.2f: %4d: ", r, values.length);
                        Arrays.stream(values)
                                .limit(10)
                                .forEach(x -> System.out.format("  %8.6f", x));
                    }
                    System.out.println();
                });
    }
}