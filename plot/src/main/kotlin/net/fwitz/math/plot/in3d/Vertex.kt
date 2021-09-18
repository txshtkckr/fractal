package net.fwitz.math.plot.in3d

import java.util.Objects

class Vertex(v: Vector3, rgb: Int) {
    private val v: Vector3
    private val rgb: Int

    constructor(x: Double, y: Double, z: Double, rgb: Int) : this(Vector3(x, y, z), rgb) {}

    fun v(): Vector3 {
        return v
    }

    fun x(): Double {
        return v.x()
    }

    fun y(): Double {
        return v.y()
    }

    fun z(): Double {
        return v.z()
    }

    fun rgb(): Int {
        return rgb
    }

    override fun equals(o: Any?): Boolean {
        return this === o || o is Vertex && equalTo(o)
    }

    private fun equalTo(other: Vertex): Boolean {
        return rgb == other.rgb && v == other.v
    }

    override fun hashCode(): Int {
        return v.hashCode() * 31 + rgb
    }

    override fun toString(): String {
        return "Vertex[v=" + v + ",rgb=" + hexColor(rgb) + ']'
    }

    companion object {
        private fun hexColor(rgb: Int): String {
            return String.format(
                "#%02X%02X%02X",
                rgb shr 16 and 0xFF,
                rgb shr 8 and 0xFF,
                rgb and 0xFF
            )
        }
    }

    init {
        this.v = Objects.requireNonNull(v, "v")
        this.rgb = rgb
    }
}