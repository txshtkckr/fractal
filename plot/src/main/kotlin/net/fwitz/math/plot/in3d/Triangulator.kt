package net.fwitz.math.plot.in3d

class Triangulator(width: Int, height: Int) {
    /*
    private val points: Array<Array<Vertex>>
    private val triangles: Array<Array<Triangle>>
    fun addRow(y: Int, values: Array<Complex>, colors: IntArray) {
        points[y] = IntStream.range(0, colors.size)
            .mapToObj<Vertex>(IntFunction<Vertex> { x: Int ->
                Vertex(
                    x.toDouble(), y.toDouble(), values[x].abs(), colors[x]
                )
            })
            .toArray<Vertex>(IntFunction<Array<Vertex>> { _Dummy_.__Array__() })
        if (y > 0) {
            refreshTriangles(y - 1)
        }
        if (y < points.size - 1) {
            refreshTriangles(y)
        }
    }

    private fun refreshTriangles(y: Int) {
        val above = points[y]
        val below = points[y + 1]
        triangles[y] = IntStream.range(0, above.size)
            .mapToObj<Stream<Triangle>>(IntFunction<Stream<Triangle>> { x: Int ->
                Stream.of(
                    Triangle(above[x], below[x], above[x + 1]),
                    Triangle(above[x + 1], below[x], below[x + 1])
                )
            })
            .flatMap<Triangle>(Function<Stream<Triangle?>, Stream<out Triangle?>?> { s: Stream<Triangle?>? -> s })
            .toArray<Triangle>(IntFunction<Array<Triangle>> { _Dummy_.__Array__() })
    }

    fun triangles(): Stream<Triangle> {
        return Arrays.stream<Array<Triangle>>(triangles)
            .flatMap<Triangle>(Function<Array<Triangle?>, Stream<out Triangle>> { array: Array<Triangle?>? ->
                Arrays.stream(array)
            })
    }

    init {
        points = arrayOfNulls(height)
        for (y in 0 until height) {
            val yd = y.toDouble()
            points[y] = IntStream.range(0, width)
                .mapToObj<Vertex>(IntFunction<Vertex> { x: Int ->
                    Vertex(
                        x.toDouble(), yd, 0.0, 0
                    )
                })
                .toArray<Vertex>(IntFunction<Array<Vertex>> { _Dummy_.__Array__() })
        }
        triangles = arrayOfNulls(height - 1)
        for (y in 0 until height - 1) {
            refreshTriangles(y)
        }
    }
    */
}