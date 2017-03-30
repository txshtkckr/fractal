package net.fwitz.math.main.binary.dual.functions.exponential;

import net.fwitz.math.binary.dual.DualNumber;
import net.fwitz.math.plot.binary.dual.DualNumberFunctionPlot;

public class DualLogZ {
    public static void main(String[] args) {
        new DualNumberFunctionPlot("log(z)")
                .computeFn(DualNumber::log)
                .render();
    }
}
