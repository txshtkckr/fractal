package net.fwitz.math.plot.in3d;

import java.util.Arrays;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Matrix4 {
    public static final Matrix4 IDENTITY = new Matrix4(
            1, 0, 0, 0,
            0, 1, 0, 0,
            0, 0, 1, 0,
            0, 0, 0, 1
    );

    private final double[] values;

    private Matrix4(double... values) {
        this.values = values.clone();
    }

    public static Matrix4 matrix4(double... values) {
        if (values.length != 16) {
            throw new IllegalArgumentException("Must have exactly 16 entries");
        }
        return new Matrix4(values.clone());
    }

    public static Matrix4 rotateX(double theta) {
        double cos = cos(theta);
        double sin = sin(theta);
        return new Matrix4(
                1, 0, 0, 0,
                0, cos, -sin, 0,
                0, sin, cos, 0,
                0, 0, 0, 1);
    }

    public static Matrix4 rotateY(double theta) {
        double cos = cos(theta);
        double sin = sin(theta);
        return new Matrix4(
                cos, 0, sin, 0,
                0, 1, 0, 0,
                -sin, 0, cos, 0,
                0, 0, 0, 1);
    }

    public static Matrix4 rotateZ(double theta) {
        double cos = cos(theta);
        double sin = sin(theta);
        return new Matrix4(
                cos, -sin, 0, 0,
                sin, cos, 0, 0,
                0, 0, 1, 0,
                0, 0, 0, 1);
    }

    public Matrix4 rotate(double rx, double ry, double rz) {
        return compose(rotateX(rx), rotateY(ry), rotateZ(rz));
    }

    public double value(int row, int col) {
        if (row < 0 || col < 0 || row > 3 || col > 3) {
            throw new IndexOutOfBoundsException("row=" + row + "; col=" + col);
        }
        return val(row, col);
    }

    private double val(int row, int col) {
        return values[(row << 2) + col];
    }

    public Matrix4 multiply(Matrix4 other) {
        double[] result = new double[16];
        for (int row = 0; row < 4; row++) {
            for (int col = 0; col < 4; col++) {
                result[(row << 2) + col] =
                        val(row, 0) * other.val(0, col) +
                                val(row, 1) * other.val(1, col) +
                                val(row, 2) * other.val(2, col) +
                                val(row, 3) * other.val(3, col);
            }
        }
        return new Matrix4(result);
    }

    public static Matrix4 compose(Matrix4... operations) {
        if (operations.length == 0) {
            return IDENTITY;
        }
        Matrix4 result = operations[0];
        for (int i = 1; i < operations.length; ++i) {
            result = result.multiply(operations[i]);
        }
        return result;
    }

    public Matrix4 transpose() {
        return new Matrix4(
                val(0, 0), val(1, 0), val(2, 0), val(3, 0),
                val(0, 1), val(1, 1), val(2, 1), val(3, 1),
                val(0, 2), val(1, 2), val(2, 2), val(3, 2),
                val(0, 3), val(1, 3), val(2, 3), val(3, 3));
    }

    public Vector3 transform(Vector3 v) {
        return new Vector3(
                v.x() * val(0, 0) + v.y() * val(1, 0) + v.z() * val(2, 0) + val(3, 0),
                v.x() * val(0, 1) + v.y() * val(1, 1) + v.z() * val(2, 1) + val(3, 1),
                v.x() * val(0, 2) + v.y() * val(1, 2) + v.z() * val(2, 2) + val(3, 2));
    }

    public Vertex transform(Vertex v) {
        return new Vertex(transform(v.v()), v.rgb());
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof Matrix4 && Arrays.equals(values, ((Matrix4) o).values));
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public String toString() {
        return "Matrix4" + Arrays.toString(values);
    }
}
