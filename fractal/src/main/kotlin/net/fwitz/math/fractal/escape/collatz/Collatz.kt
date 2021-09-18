package net.fwitz.math.fractal.escape.collatz

import net.fwitz.math.binary.complex.Complex

object Collatz {
    fun collatz(z: Complex): Complex {
        val onePlus4z = z * 4 + 1
        val onePlus2z = z * 2 + 1
        val cosPiZ: Complex = (z * Math.PI).cos
        return (onePlus4z - (onePlus2z * cosPiZ)) * 0.25
    }
}