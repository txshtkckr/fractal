package net.fwitz.math.main.escape.newton;

import net.fwitz.math.main.complex.functions.examples.simple.JedSoftExample1;

public class NewtonsMethodJedSoftExample1 {
    public static void main(String[] args) {
        NewtonsMethodPlot.render(JedSoftExample1.FN_NAME + " (Newton's Method)", JedSoftExample1::fn);
    }
}
