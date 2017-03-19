package net.fwitz.math.plot.in3d;

import java.util.Arrays;

import static java.lang.Math.acos;
import static java.lang.Math.sqrt;

public class Vector3 {
    public static final Vector3 ZERO = new Vector3(0, 0, 0);
    
    private final double v[];

    public Vector3(double... values) {
        this.v = new double[3];
        System.arraycopy(values, 0, v, 0, values.length);
    }
    public Vector3(double x, double y, double z) {
        this.v = new double[]{x, y, z};
    }

    public double x() {
        return v[0];
    }

    public double y() {
        return v[1];
    }

    public double z() {
        return v[2];
    }

    private double abs2() {
        return dot(this);
    }

    public double abs() {
        return sqrt(abs2());
    }

    public double dot(Vector3 b) {
        return dot(this, b);
    }

    public static double dot(Vector3 a, Vector3 b) {
        return a.v[0] * b.v[0] + a.v[1] * b.v[1] + a.v[2] * b.v[2];
    }

    public Vector3 plus(Vector3 b) {
        return plus(this, b);
    }

    public static Vector3 plus(Vector3 a, Vector3 b) {
        return new Vector3(a.v[0] + b.v[0], a.v[1] + b.v[1], a.v[2] + b.v[2]);
    }

    public Vector3 minus(Vector3 b) {
        return minus(this, b);
    }

    public static Vector3 minus(Vector3 a, Vector3 b) {
        return new Vector3(a.v[0] - b.v[0], a.v[1] - b.v[1], a.v[2] - b.v[2]);
    }

    public Vector3 cross(Vector3 b) {
        return cross(this, b);
    }

    public static Vector3 cross(Vector3 a, Vector3 b) {
        double x = a.v[1] * b.v[2] - a.v[2] * b.v[1];
        double y = a.v[2] * b.v[0] - a.v[0] * b.v[2];
        double z = a.v[0] * b.v[1] - a.v[1] * b.v[0];
        return new Vector3(x, y, z);
    }

    public double angle(Vector3 b) {
        return angle(this, b);
    }

    public static double angle(Vector3 a, Vector3 b) {
        double cosTheta = dot(a, b) / sqrt(a.abs2() * b.abs2());
        return acos(cosTheta);
    }

    public Vector3 scale(double scale) {
        return new Vector3(v[0] * scale, v[1] * scale, v[2] * scale);
    }

    public Vector3 scale(Vector3 b) {
        return scale(this, b);
    }

    public static Vector3 scale(Vector3 a, Vector3 b) {
        return new Vector3(a.v[0] * b.v[0], a.v[1] * b.v[1], a.v[2] * b.v[2]);
    }

    public Vector3 normalize() {
        double scale = abs();
        if (scale == 0.0) {
            return this;
        }
        return new Vector3(v[0] / scale, v[1] / scale, v[2] / scale);
    }

    public Vector3 project(Vector3 b) {
        return project(this, b);
    }

    public static Vector3 project(Vector3 a, Vector3 b) {
        double aDotB = a.dot(b);
        double bDotB = b.dot(b);
        if (aDotB == 0.0 || bDotB == 0.0) {
            return ZERO;
        }
        return b.scale(aDotB / bDotB);
    }

    public double index(int i) {
        return v[i];
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof Vector3 && Arrays.equals(v, ((Vector3) o).v));
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(v);
    }

    @Override
    public String toString() {
        return "Vector3" + Arrays.toString(v);
    }
}
