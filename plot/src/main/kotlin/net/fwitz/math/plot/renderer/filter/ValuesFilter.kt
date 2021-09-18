package net.fwitz.math.plot.renderer.filter

class ValuesFilter<V>(
    val name: String,
    private val delegate: ValuesFilterFunction<V>
) : ValuesFilterFunction<V> {
    companion object {
        private val IDENTITY: ValuesFilter<*> = ValuesFilter("Identity") { xv, yv, zv: Array<Any> -> zv }

        @Suppress("UNCHECKED_CAST")
        fun <V> identity(): ValuesFilter<V> = IDENTITY as ValuesFilter<V>
    }

    override fun invoke(xValues: DoubleArray, yValue: Double, zValues: Array<V>): Array<V> {
        return delegate(xValues, yValue, zValues)
    }

    override fun toString(): String {
        return "ValuesFilter[name=$name]"
    }
}