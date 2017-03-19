package net.fwitz.math.plot.in3d;

import java.util.Comparator;
import java.util.Objects;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

public class Triangle {
    private final Vertex v1;
    private final Vertex v2;
    private final Vertex v3;

    public Triangle(Vertex v1, Vertex v2, Vertex v3) {
        this.v1 = requireNonNull(v1, "v1");
        this.v2 = requireNonNull(v2, "v2");
        this.v3 = requireNonNull(v3, "v3");
    }

    public Vertex v1() {
        return v1;
    }

    public Vertex v2() {
        return v2;
    }

    public Vertex v3() {
        return v3;
    }

    public Stream<Vertex> rasterize() {
        final Vertex[] v = Stream.of(v1, v2, v3)
                .sorted(Comparator.comparingDouble(Vertex::y))
                .toArray(Vertex[]::new);
        /*
        if (v[1].y() == v[2].y()) {
            //fillBottomFlatTriangle(v[0], v[1], v[2]);
        } else if (vt[0].y() == vt[1].y()) {
            //fillTopFlatTriangle(v[0], v[1], v[2]);
        } else {
            double slope = ((v[2].x() - v[0].x()) / v[2].y() - v[0].y()) * v[1].y();
        }
        */
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Triangle)) return false;
        Triangle triangle = (Triangle) o;
        return Objects.equals(v1, triangle.v1) &&
                Objects.equals(v2, triangle.v2) &&
                Objects.equals(v3, triangle.v3);
    }

    @Override
    public int hashCode() {
        return Objects.hash(v1, v2, v3);
    }

    @Override
    public String toString() {
        return "Triangle[v1=" + v1 + ",v2=" + v2 + ",v3=" + v3 + ']';
    }
}
