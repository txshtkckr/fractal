package net.fwitz.math.plot.in3d;

import net.fwitz.math.complex.Complex;

import java.util.Arrays;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Triangulator {
    private final Vertex[][] points;
    private final Triangle[][] triangles;

    public Triangulator(int width, int height) {
        this.points = new Vertex[height][];
        for (int y = 0; y < height; y++) {
            final double yd = y;
            points[y] = IntStream.range(0, width)
                    .mapToObj(x -> new Vertex(x, yd, 0.0, 0))
                    .toArray(Vertex[]::new);
        }
        this.triangles = new Triangle[height - 1][];
        for (int y = 0; y < height - 1; y++) {
            refreshTriangles(y);
        }
    }

    public void addRow(int y, Complex[] values, int[] colors) {
        points[y] = IntStream.range(0, colors.length)
                .mapToObj(x -> new Vertex(x, y, values[x].abs(), colors[x]))
                .toArray(Vertex[]::new);
        if (y > 0) {
            refreshTriangles(y - 1);
        }
        if (y < points.length - 1) {
            refreshTriangles(y);
        }
    }

    private void refreshTriangles(int y) {
        Vertex[] above = points[y];
        Vertex[] below = points[y + 1];
        triangles[y] = IntStream.range(0, above.length)
                .mapToObj(x -> Stream.of(
                    new Triangle(above[x], below[x], above[x + 1]),
                    new Triangle(above[x + 1], below[x], below[x + 1])))
                .flatMap(s -> s)
                .toArray(Triangle[]::new);
    }

    public Stream<Triangle> triangles() {
        return Arrays.stream(triangles).flatMap(Arrays::stream);
    }
}
