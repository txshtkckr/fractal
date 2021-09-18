package net.fwitz.math.plot.renderer.filter

open class ValuesFilters<V> private constructor(
    private val filters: List<ValuesFilter<V>>
) {
    constructor(vararg filters: ValuesFilter<V>) : this(listOf<ValuesFilter<V>>(*filters))

    val size = filters.size

    operator fun get(index: Int) = filters[index]
    override fun toString() = filters.toString()
}