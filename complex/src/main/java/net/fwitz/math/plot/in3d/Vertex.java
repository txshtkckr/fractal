package net.fwitz.math.plot.in3d;

import static java.util.Objects.requireNonNull;

public class Vertex {
    private final Vector3 v;
    private final int rgb;

    public Vertex(Vector3 v, int rgb) {
        this.v = requireNonNull(v, "v");
        this.rgb = rgb;
    }

    public Vertex(double x, double y, double z, int rgb) {
        this(new Vector3(x, y, z), rgb);
    }

    public Vector3 v() {
        return v;
    }

    public double x() {
        return v.x();
    }

    public double y() {
        return v.y();
    }

    public double z() {
        return v.z();
    }

    public int rgb() {
        return rgb;
    }

    @Override
    public boolean equals(Object o) {
        return this == o || (o instanceof Vertex && equalTo((Vertex) o));
    }

    private boolean equalTo(Vertex other) {
        return rgb == other.rgb && v.equals(other.v);
    }

    @Override
    public int hashCode() {
        return v.hashCode() * 31 + rgb;
    }

    @Override
    public String toString() {
        return "Vertex[v=" + v + ",rgb=" + hexColor(rgb) + ']';
    }

    private static String hexColor(int rgb) {
        return String.format("#%02X%02X%02X",
                (rgb >> 16) & 0xFF,
                (rgb >> 8) & 0xFF,
                rgb & 0xFF);
    }
}
