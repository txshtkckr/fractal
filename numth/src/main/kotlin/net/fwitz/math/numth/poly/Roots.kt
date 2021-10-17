package net.fwitz.math.numth.poly

import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.numth.numbers.Divisibility

class Roots(val coeffs: List<Int>): () -> List<Complex> {
    constructor(coeffs: IntArray) : this(coeffs.asList())

    override fun invoke(): List<Complex> {
        val rcoeffs = ArrayList(coeffs.reversed())
        var order = rcoeffs.lastIndex
        while (order >= 0 && rcoeffs[order] == 0) --order
        if (order <= 0) return emptyList()

        val gcd = Divisibility.gcd(rcoeffs)
        if (gcd == 0) return listOf(Complex.ZERO)
        if (gcd != 1) rcoeffs.indices.forEach { rcoeffs[it] /= gcd }
        TODO()
    }
}