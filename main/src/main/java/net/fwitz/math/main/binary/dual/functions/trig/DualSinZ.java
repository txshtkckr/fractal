package net.fwitz.math.main.binary.dual.functions.trig;

import net.fwitz.math.binary.dual.DualNumber;
import net.fwitz.math.plot.binary.dual.DualNumberFunctionPlot;

public class DualSinZ {
    public static void main(String[] args) {
        new DualNumberFunctionPlot("sin(z)")
                .computeFn(DualNumber::sin)
                .render();
    }
}
