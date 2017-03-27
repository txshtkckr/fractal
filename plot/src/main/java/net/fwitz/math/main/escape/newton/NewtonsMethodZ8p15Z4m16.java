package net.fwitz.math.main.escape.newton;

import net.fwitz.math.main.complex.analysis.examples.simple.Z8p15Z4m16;

public class NewtonsMethodZ8p15Z4m16 {
    public static void main(String[] args) {
        NewtonsMethodPlot.render(
                "z^8 + 15z^4 - 16  (Newton's Method)",
                Z8p15Z4m16::f,
                Z8p15Z4m16::df);
    }

}
