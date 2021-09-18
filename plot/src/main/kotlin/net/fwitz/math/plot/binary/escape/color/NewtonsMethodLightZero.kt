package net.fwitz.math.plot.binary.escape.color

import net.fwitz.math.binary.BinaryNumber

class NewtonsMethodLightZero<T : BinaryNumber<T>> : NewtonsMethodDarkZero<T>() {
    override fun value(closeness: Double): Double {
        return 0.2 + 0.8 * closeness
    }
}