package net.fwitz.math.main.escape.halley;

public class HalleysMethodZCubedMinus2ZMinus5 {
    public static void main(String[] args) {
        HalleysMethodPlot.render(
                "z^3 -2z - 5  (Halley's Method)",
                z -> z.pow3().minus(z.times(2)).minus(5),
                z -> z.pow2().times(3).minus(2),
                z -> z.times(6));
    }
}
