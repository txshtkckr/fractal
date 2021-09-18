package net.fwitz.math.plot.in3d

import java.util.Arrays

object Bresenham {
    fun bresenham(a: Int, b: Int, steps: Int): IntArray {
        if (a <= b) {
            return bresenham(a, b, steps) { it }
        }
        return when {
            a <= b -> bresenham(a, b, steps) { it }
            else -> bresenham(b, a, steps) { (steps - 1) - it }
        }
    }

    private fun bresenham(a: Int, b: Int, steps: Int, xMap: (Int) -> Int): IntArray {
        val result = IntArray(steps)
        if (steps == 0) {
            return result
        }
        if (a == b) {
            Arrays.fill(result, a)
            return result
        }
        val dy = b - a
        var d = 2 * dy - steps
        var y = a
        for (x in 0 until steps) {
            result[xMap(x)] = y
            if (d > 0) {
                ++y
                d -= 2 * steps
            }
            d += 2 * dy
        }
        return result
    }

    fun multiBresenham(a: IntArray, b: IntArray, steps: Int): Array<IntArray?> {
        require(a.size == b.size) { "a.length != b.length: " + a.size + " != " + b.size }
        val aa = IntArray(a.size)
        val bb = IntArray(b.size)
        val limit = steps - 1
        val xMap = Array<(Int) -> Int>(a.size) { i ->
            if (a[i] <= b[i]) {
                aa[i] = a[i]
                bb[i] = b[i]
                { x -> x }
            } else {
                aa[i] = b[i]
                bb[i] = a[i]
                { x -> limit - x }
            }
        }
        return multiBresenham(aa, bb, steps, xMap)
    }

    private fun multiBresenham(a: IntArray, b: IntArray, steps: Int, xMap: Array<(Int) -> Int>): Array<IntArray?> {
        val result = arrayOfNulls<IntArray>(a.size)
        for (i in a.indices) {
            result[i] = IntArray(steps)
        }
        if (steps == 0) {
            return result
        }
        val dy = IntArray(a.size)
        val d = IntArray(a.size)
        val y = a.copyOf()
        for (i in a.indices) {
            dy[i] = b[i] - a[i]
            d[i] = 2 * dy[i] - steps
        }
        for (x in 0 until steps) {
            for (i in a.indices) {
                result[xMap[i](x)] = y
                if (d[i] > 0) {
                    ++y[i]
                    d[i] -= 2 * steps
                }
                d[i] += 2 * dy[i]
            }
        }
        return result
    }
}