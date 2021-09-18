package net.fwitz.math.plot.in3d

import kotlin.math.cos
import kotlin.math.sin

class Matrix4 private constructor(
    vararg values: Double
) {
    companion object {
        val IDENTITY = Matrix4(
            1.0, 0.0, 0.0, 0.0,
            0.0, 1.0, 0.0, 0.0,
            0.0, 0.0, 1.0, 0.0,
            0.0, 0.0, 0.0, 1.0
        )

        fun matrix4(vararg values: Double): Matrix4 {
            require(values.size == 16) { "Must have exactly 16 entries" }
            return Matrix4(*values.copyOf())
        }

        fun rotateX(theta: Double): Matrix4 {
            val cos = cos(theta)
            val sin = sin(theta)
            return Matrix4(
                1.0, 0.0, 0.0, 0.0,
                0.0, cos, -sin, 0.0,
                0.0, sin, cos, 0.0,
                0.0, 0.0, 0.0, 1.0
            )
        }

        fun rotateY(theta: Double): Matrix4 {
            val cos = cos(theta)
            val sin = sin(theta)
            return Matrix4(
                cos, 0.0, sin, 0.0,
                0.0, 1.0, 0.0, 0.0,
                -sin, 0.0, cos, 0.0,
                0.0, 0.0, 0.0, 1.0
            )
        }

        fun rotateZ(theta: Double): Matrix4 {
            val cos = cos(theta)
            val sin = sin(theta)
            return Matrix4(
                cos, -sin, 0.0, 0.0,
                sin, cos, 0.0, 0.0,
                0.0, 0.0, 1.0, 0.0,
                0.0, 0.0, 0.0, 1.0
            )
        }

        fun compose(vararg operations: Matrix4): Matrix4 {
            if (operations.isEmpty()) return IDENTITY
            var result = operations[0]
            for (i in 1 until operations.size) {
                result = result.multiply(operations[i])
            }
            return result
        }
    }

    private val values = values.copyOf()

    fun rotate(rx: Double, ry: Double, rz: Double): Matrix4 {
        return compose(rotateX(rx), rotateY(ry), rotateZ(rz))
    }

    fun value(row: Int, col: Int): Double {
        if (row < 0 || col < 0 || row > 3 || col > 3) {
            throw IndexOutOfBoundsException("row=$row; col=$col")
        }
        return v(row, col)
    }

    private fun v(row: Int, col: Int) = values[(row shl 2) + col]

    fun multiply(other: Matrix4): Matrix4 {
        val result = DoubleArray(16)
        for (row in 0..3) {
            for (col in 0..3) {
                result[(row shl 2) + col] =
                    v(row, 0) * other.v(0, col) + v(row, 1) * other.v(1, col) + v(
                        row,
                        2
                    ) * other.v(2, col) + v(row, 3) * other.v(3, col)
            }
        }
        return Matrix4(*result)
    }

    fun transpose(): Matrix4 {
        return Matrix4(
            v(0, 0), v(1, 0), v(2, 0), v(3, 0),
            v(0, 1), v(1, 1), v(2, 1), v(3, 1),
            v(0, 2), v(1, 2), v(2, 2), v(3, 2),
            v(0, 3), v(1, 3), v(2, 3), v(3, 3)
        )
    }

    fun transform(v: Vector3?): Vector3 {
        return Vector3(
            v!!.x() * v(0, 0) + v.y() * v(1, 0) + v.z() * v(2, 0) + v(3, 0),
            v.x() * v(0, 1) + v.y() * v(1, 1) + v.z() * v(2, 1) + v(3, 1),
            v.x() * v(0, 2) + v.y() * v(1, 2) + v.z() * v(2, 2) + v(3, 2)
        )
    }

    fun transform(v: Vertex): Vertex {
        return Vertex(transform(v.v()), v.rgb())
    }

    override fun equals(other: Any?): Boolean {
        return this === other || other is Matrix4 && values.contentEquals(other.values)
    }

    override fun hashCode(): Int {
        return values.contentHashCode()
    }

    override fun toString(): String {
        return "Matrix4" + values.contentToString()
    }
}
