package net.fwitz.math.plot.binary.escape.color;

public class NewtonsMethodLightZero extends NewtonsMethodDarkZero {
    @Override
    double val(double closeness) {
        return 0.2 + 0.8 * closeness;
    }
}
