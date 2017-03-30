package net.fwitz.math.main.binary.dual.functions.trig;

import net.fwitz.math.binary.dual.DualNumber;
import net.fwitz.math.plot.binary.dual.DualNumberFunctionPlot;

public class DualCosZ {
    public static void main(String[] args) {
        new DualNumberFunctionPlot("cos(z)")
                .computeFn(DualNumber::cos)
                .render();
    }
}
