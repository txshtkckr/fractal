package net.fwitz.math.main.bifurc;

import net.fwitz.math.fractal.bifurc.BifurcLogisticMapFunction;
import net.fwitz.math.plot.bifurc.LogisticMapPlot;

public class BifurcLogisticMap {
    public static void main(String[] args) {
        new LogisticMapPlot("rx(1-x)")
                .computeFn(BifurcLogisticMapFunction::evaluate)
                .render();
    }
}
