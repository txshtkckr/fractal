package net.fwitz.math.main.escape.newton;

import net.fwitz.math.complex.Complex;

public class NewtonsMethodSin {
    public static void main(String[] args) {
        NewtonsMethodPlot.render("sin(z) (Newton's Method)",
                Complex::sin,
                Complex::cos);
    }
}
