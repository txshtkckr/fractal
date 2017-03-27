package net.fwitz.math.main.escape.halley;

import net.fwitz.math.main.complex.analysis.examples.simple.Z8p15Z4m16;
import net.fwitz.math.main.escape.newton.NewtonsMethodZ8p15Z4m16;

public class HalleysMethodZ8p15Z4m16 extends NewtonsMethodZ8p15Z4m16 {
    public static void main(String[] args) {
        HalleysMethodPlot.render(
                "z^8 + 15z^4 - 16  (Halley's Method)",
                Z8p15Z4m16::f,
                Z8p15Z4m16::df,
                Z8p15Z4m16::d2f);
    }
}
