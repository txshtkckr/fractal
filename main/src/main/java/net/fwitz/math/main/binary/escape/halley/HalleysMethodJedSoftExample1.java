package net.fwitz.math.main.binary.escape.halley;

import net.fwitz.math.main.binary.complex.functions.examples.simple.JedSoftExample1;

public class HalleysMethodJedSoftExample1 {
    public static void main(String[] args) {
        HalleysMethodPlot.render(JedSoftExample1.FN_NAME + " (Halley's Method)", JedSoftExample1::fn);
    }
}
