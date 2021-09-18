package net.fwitz.math.plot.in3d

import kotlin.math.cos
import kotlin.math.sin

class Matrix3 private constructor(
    r0c0: Double, r0c1: Double, r0c2: Double,
    r1c0: Double, r1c1: Double, r1c2: Double,
    r2c0: Double, r2c1: Double, r2c2: Double
) {
    private val values: Array<Vector3> = arrayOf(
        Vector3(r0c0, r0c1, r0c2),
        Vector3(r1c0, r1c1, r1c2),
        Vector3(r2c0, r2c1, r2c2)
    )

    private constructor(values: DoubleArray) : this(
        values[0],
        values[1],
        values[2],
        values[3],
        values[4],
        values[5],
        values[6],
        values[7],
        values[8]
    ) {
        require(values.size == 9) { "Must have exactly 9 entries" }
    }

    fun multiply(other: Matrix3): Matrix3 {
        val result = DoubleArray(9)
        for (row in 0..2) {
            for (col in 0..2) {
                result[row * 3 + col] =
                    values[row].index(0) * other.values[0].index(col) + values[row].index(1) * other.values[1].index(col) + values[row].index(
                        2
                    ) * other.values[2].index(col)
            }
        }
        return Matrix3(result)
    }

    fun transpose(): Matrix3 {
        return Matrix3(
            values[0].index(0), values[1].index(0), values[2].index(0),
            values[0].index(1), values[1].index(1), values[2].index(1),
            values[0].index(2), values[1].index(2), values[2].index(2)
        )
    }

    fun transform(v: Vector3?): Vector3 {
        return Vector3(
            v!!.x() * values[0].x() + v.y() * values[1].x() + v.z() * values[2].x(),
            v.x() * values[0].y() + v.y() * values[1].y() + v.z() * values[2].y(),
            v.x() * values[0].z() + v.y() * values[1].z() + v.z() * values[2].z()
        )
    }

    fun transform(v: Vertex): Vertex {
        return Vertex(transform(v.v()), v.rgb())
    }

    override fun equals(other: Any?): Boolean {
        return this === other || other is Matrix3 && values.contentEquals(other.values)
    }

    override fun hashCode(): Int {
        return values.contentHashCode()
    }

    override fun toString(): String {
        return "Matrix3" + values.contentToString()
    }

    companion object {
        val IDENTITY = Matrix3(
            1.0, 0.0, 0.0,
            0.0, 1.0, 0.0,
            0.0, 0.0, 1.0
        )

        fun rotateX(theta: Double): Matrix3 {
            val cos = cos(theta)
            val sin = sin(theta)
            return Matrix3(
                1.0, 0.0, 0.0,
                0.0, cos, -sin,
                0.0, sin, cos
            )
        }

        fun rotateY(theta: Double): Matrix3 {
            val cos = cos(theta)
            val sin = sin(theta)
            return Matrix3(
                cos, 0.0, sin,
                0.0, 1.0, 0.0,
                -sin, 0.0, cos
            )
        }

        fun rotateZ(theta: Double): Matrix3 {
            val cos = cos(theta)
            val sin = sin(theta)
            return Matrix3(
                cos, -sin, 0.0,
                sin, cos, 0.0,
                0.0, 0.0, 1.0
            )
        }

        fun rotate(rx: Double, ry: Double, rz: Double): Matrix3 {
            return compose(rotateX(rx), rotateY(ry), rotateZ(rz))
        }

        fun compose(vararg operations: Matrix3): Matrix3 {
            if (operations.isEmpty()) return IDENTITY
            var result = operations[0]
            for (i in 1..operations.lastIndex) {
                result = result.multiply(operations[i])
            }
            return result
        }
    }
}