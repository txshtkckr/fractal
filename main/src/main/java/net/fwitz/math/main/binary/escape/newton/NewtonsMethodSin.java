package net.fwitz.math.main.binary.escape.newton;

import net.fwitz.math.binary.complex.Complex;

public class NewtonsMethodSin {
    public static void main(String[] args) {
        NewtonsMethodPlot.render("sin(z) (Newton's Method)",
                Complex::sin,
                Complex::cos);
    }
}
