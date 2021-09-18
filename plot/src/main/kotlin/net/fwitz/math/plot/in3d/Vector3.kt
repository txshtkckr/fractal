package net.fwitz.math.plot.in3d

import kotlin.math.acos
import kotlin.math.sqrt

class Vector3 {
    companion object {
        val ZERO = Vector3(0.0, 0.0, 0.0)
        fun dot(a: Vector3, b: Vector3): Double {
            return a.v[0] * b.v[0] + a.v[1] * b.v[1] + a.v[2] * b.v[2]
        }

        fun plus(a: Vector3, b: Vector3): Vector3 {
            return Vector3(a.v[0] + b.v[0], a.v[1] + b.v[1], a.v[2] + b.v[2])
        }

        fun minus(a: Vector3, b: Vector3): Vector3 {
            return Vector3(a.v[0] - b.v[0], a.v[1] - b.v[1], a.v[2] - b.v[2])
        }

        fun cross(a: Vector3, b: Vector3): Vector3 {
            val x = a.v[1] * b.v[2] - a.v[2] * b.v[1]
            val y = a.v[2] * b.v[0] - a.v[0] * b.v[2]
            val z = a.v[0] * b.v[1] - a.v[1] * b.v[0]
            return Vector3(x, y, z)
        }

        fun angle(a: Vector3, b: Vector3): Double {
            val cosTheta = dot(a, b) / sqrt(a.abs2() * b.abs2())
            return acos(cosTheta)
        }

        fun scale(a: Vector3, b: Vector3): Vector3 {
            return Vector3(a.v[0] * b.v[0], a.v[1] * b.v[1], a.v[2] * b.v[2])
        }

        fun project(a: Vector3, b: Vector3): Vector3 {
            val aDotB = a.dot(b)
            val bDotB = b.dot(b)
            return if (aDotB == 0.0 || bDotB == 0.0) {
                ZERO
            } else b.scale(aDotB / bDotB)
        }
    }

    private val v: DoubleArray

    constructor(vararg values: Double) {
        v = DoubleArray(3)
        System.arraycopy(values, 0, v, 0, values.size)
    }

    constructor(x: Double, y: Double, z: Double) {
        v = doubleArrayOf(x, y, z)
    }

    fun x() = v[0]
    fun y() = v[1]
    fun z() = v[2]

    private fun abs2() = dot(this)
    fun abs() = sqrt(abs2())
    fun dot(b: Vector3) = dot(this, b)
    operator fun plus(b: Vector3) = plus(this, b)
    operator fun minus(b: Vector3) = minus(this, b)
    fun cross(b: Vector3) = cross(this, b)
    fun angle(b: Vector3) = angle(this, b)
    fun scale(scale: Double) = Vector3(v[0] * scale, v[1] * scale, v[2] * scale)
    fun scale(b: Vector3) = scale(this, b)

    fun normalize(): Vector3 {
        val scale = abs()
        return when {
            scale == 0.0 -> this
            else -> Vector3(v[0] / scale, v[1] / scale, v[2] / scale)
        }
    }

    fun project(b: Vector3) = project(this, b)
    fun index(i: Int): Double = v[i]

    override fun equals(other: Any?): Boolean {
        return this === other || other is Vector3 && v.contentEquals(other.v)
    }

    override fun hashCode() = v.contentHashCode()

    override fun toString() = "Vector3" + v.contentToString()
}
