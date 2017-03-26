package net.fwitz.math.plot.color.escape;

public class NewtonsMethodLightZero extends NewtonsMethodDarkZero {
    @Override
    double val(double closeness) {
        return 0.2 + 0.8 * closeness;
    }
}
