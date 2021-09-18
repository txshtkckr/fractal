package net.fwitz.math.plot.renderer

import java.lang.reflect.Array.newInstance

inline fun <X> Class<X>.newArray(
    size: Int,
    init: (Int) -> X
): Array<X> {
    @Suppress("UNCHECKED_CAST")
    val array = newInstance(this, size) as Array<X>
    array.indices.forEach { array[it] = init(it) }
    return array
}
