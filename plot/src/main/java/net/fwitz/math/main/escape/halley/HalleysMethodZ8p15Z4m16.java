package net.fwitz.math.main.escape.halley;

import net.fwitz.math.complex.Complex;
import net.fwitz.math.main.escape.newton.NewtonsMethodZ8p15Z4m16;

public class HalleysMethodZ8p15Z4m16 extends NewtonsMethodZ8p15Z4m16 {

    public static void main(String[] args) {
        HalleysMethodPlot.render(
                "z^8 + 15z^4 - 16  (Newton's Method)",
                HalleysMethodZ8p15Z4m16::f,
                HalleysMethodZ8p15Z4m16::df,
                HalleysMethodZ8p15Z4m16::d2f);
    }

    public static Complex f(Complex z) {
        return z.pow(8)
                .plus(z.pow(4).times(15))
                .minus(16);
    }

    public static Complex df(Complex z) {
        return z.pow(7).times(8)
                .plus(z.pow3().times(60));
    }
    
    public static Complex d2f(Complex z) {
        return z.pow(6).times(56)
                .plus(z.pow2().times(180));
    }
}
