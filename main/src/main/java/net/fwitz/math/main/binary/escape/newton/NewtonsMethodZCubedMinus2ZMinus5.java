package net.fwitz.math.main.binary.escape.newton;

public class NewtonsMethodZCubedMinus2ZMinus5 {
    public static void main(String[] args) {
        NewtonsMethodPlot.render(
                "z^3 -2z - 5  (Newton's Method)",
                z -> z.pow3().minus(z.times(2)).minus(5),
                z -> z.pow2().times(3).minus(2));
    }
}
