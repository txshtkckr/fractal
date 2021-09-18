package net.fwitz.math.fractal.escape

import net.fwitz.math.binary.BinaryNumber

class EscapeTimeResult<T : BinaryNumber<T>> private constructor(
    val escaped: Boolean,
    val iters: Int,
    val z: T,
    val smoothing: Double?
) {
    companion object {
        fun <T : BinaryNumber<T>> contained(z: T, smoothing: Double?): EscapeTimeResult<T> {
            return EscapeTimeResult(false, 0, z, smoothing)
        }

        fun <T : BinaryNumber<T>> escaped(iters: Int, z: T, smoothing: Double?): EscapeTimeResult<T> {
            return EscapeTimeResult(true, iters, z, smoothing)
        }
    }

    val contained get() = !escaped

    override fun toString() = "EscapeFunction.Result[escaped=$escaped; iters=$iters; z=$z; smoothing=$smoothing]"
}