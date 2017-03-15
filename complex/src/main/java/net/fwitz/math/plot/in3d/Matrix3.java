package net.fwitz.math.plot.in3d;

import java.util.Arrays;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

public class Matrix3 {
    public static final Matrix3 IDENTITY = new Matrix3(1, 0, 0, 0, 1, 0, 0, 0, 1);
    
    private Vector3[] values;

    private Matrix3(double[] values) {
        this(values[0], values[1], values[2], values[3], values[4], values[5], values[6], values[7], values[8]);
        if (values.length != 9) {
            throw new IllegalArgumentException("Must have exactly 9 entries");
        }
    }

    private Matrix3(double r0c0, double r0c1, double r0c2,
                    double r1c0, double r1c1, double r1c2,
                    double r2c0, double r2c1, double r2c2) {
        this.values = new Vector3[]{
                new Vector3(r0c0, r0c1, r0c2),
                new Vector3(r1c0, r1c1, r1c2),
                new Vector3(r2c0, r2c1, r2c2)
        };
    }

    public Matrix3 rotateX(double theta) {
        double cos = cos(theta);
        double sin = sin(theta);
        return new Matrix3(1, 0, 0, 0, cos, -sin, 0, sin, cos);
    }

    public Matrix3 rotateY(double theta) {
        double cos = cos(theta);
        double sin = sin(theta);
        return new Matrix3(cos, 0, sin, 0, 1, 0, -sin, 0, cos);
    }

    public Matrix3 rotateZ(double theta) {
        double cos = cos(theta);
        double sin = sin(theta);
        return new Matrix3(cos, -sin, 0, sin, cos, 0, 0, 0, 1);
    }

    public Matrix3 rotate(double rx, double ry, double rz) {
        return compose(rotateX(rx), rotateY(ry), rotateZ(rz));
    }

    public Matrix3 multiply(Matrix3 other) {
        double[] result = new double[9];
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                result[row * 3 + col] =
                        this.values[row].index(0) * other.values[0].index(col) +
                        this.values[row].index(1) * other.values[1].index(col) +
                        this.values[row].index(2) * other.values[2].index(col);
            }
        }
        return new Matrix3(result);
    }

    public static Matrix3 compose(Matrix3... operations) {
        if (operations.length == 0) {
            return IDENTITY;
        }
        Matrix3 result = operations[0];
        for (int i = 1; i < operations.length; ++i) {
            result = result.multiply(operations[i]);
        }
        return result;
    }

    public Matrix3 transpose() {
        return new Matrix3(
                values[0].index(0), values[1].index(0), values[2].index(0),
                values[0].index(1), values[1].index(1), values[2].index(1),
                values[0].index(2), values[1].index(2), values[2].index(2));
    }

    public Vector3 transform(Vector3 v) {
        return new Vector3(
                v.x() * values[0].x() + v.y() * values[1].x() + v.z() * values[2].x(),
                v.x() * values[0].y() + v.y() * values[1].y() + v.z() * values[2].y(),
                v.x() * values[0].z() + v.y() * values[1].z() + v.z() * values[2].z());
    }

    public Vertex transform(Vertex v) {
        return new Vertex(transform(v.v()), v.rgb());
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof Matrix3 && Arrays.equals(values, ((Matrix3) o).values));
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(values);
    }

    @Override
    public String toString() {
        return "Matrix3" + Arrays.toString(values);
    }
}
