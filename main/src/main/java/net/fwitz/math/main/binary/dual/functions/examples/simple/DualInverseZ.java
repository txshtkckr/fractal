package net.fwitz.math.main.binary.dual.functions.examples.simple;

import net.fwitz.math.binary.dual.DualNumber;
import net.fwitz.math.plot.binary.dual.DualNumberFunctionPlot;

public class DualInverseZ {
    public static void main(String[] args) {
        new DualNumberFunctionPlot("1/z")
                .computeFn(DualNumber::inverse)
                .render();
    }
}
