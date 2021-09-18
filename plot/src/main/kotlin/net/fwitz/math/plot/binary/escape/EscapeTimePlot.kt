package net.fwitz.math.plot.binary.escape

import net.fwitz.math.binary.BinaryNumber
import net.fwitz.math.binary.complex.Complex
import net.fwitz.math.binary.dual.DualNumber
import net.fwitz.math.binary.split.SplitComplex
import net.fwitz.math.fractal.escape.EscapeTimeResult
import net.fwitz.math.plot.binary.BinaryNumberFunctionPlot

class EscapeTimePlot<T : BinaryNumber<T>> private constructor(
    title: String,
    private val binaryNumberType: Class<T>,
    private val valueFactory: (Double, Double) -> T
) : BinaryNumberFunctionPlot<EscapeTimePlot<T>, EscapeTimePanel<T>, T, EscapeTimeResult<T>>(title) {

    companion object {
        fun complex(title: String): EscapeTimePlot<Complex> = EscapeTimePlot(
            title,
            Complex::class.java,
            Complex::complex
        )

        fun splitComplex(title: String): EscapeTimePlot<SplitComplex> = EscapeTimePlot(
            title,
            SplitComplex::class.java,
            SplitComplex::splitComplex
        )

        fun dualNumber(title: String): EscapeTimePlot<DualNumber> = EscapeTimePlot(
            title,
            DualNumber::class.java,
            DualNumber::dualNumber
        )
    }

    override fun value(x: Double, y: Double) = valueFactory(x, y)

    override fun createPanel(): EscapeTimePanel<T> {
        return EscapeTimePanel(binaryNumberType, this)
    }
}