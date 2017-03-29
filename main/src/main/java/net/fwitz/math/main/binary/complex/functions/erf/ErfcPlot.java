package net.fwitz.math.main.binary.complex.functions.erf;

import net.fwitz.math.binary.complex.functions.Erf;
import net.fwitz.math.plot.binary.complex.ComplexFunctionPlot;

public class ErfcPlot {
    public static void main(String[] args) {
        new ComplexFunctionPlot("erfc(z)")
                .computeFn(Erf::erfc)
                .domainX(-5, 5)
                .domainY(-5, 5)
                .render();
    }
}
