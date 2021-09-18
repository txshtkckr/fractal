package net.fwitz.math.plot.in3d

import java.util.Objects
import java.util.Comparator
import java.util.stream.Stream

class Triangle(v1: Vertex, v2: Vertex, v3: Vertex) {
    private val v1: Vertex
    private val v2: Vertex
    private val v3: Vertex
    fun v1(): Vertex {
        return v1
    }

    fun v2(): Vertex {
        return v2
    }

    fun v3(): Vertex {
        return v3
    }

    fun rasterize(): Stream<Vertex>? {
        val v = sequenceOf(v1, v2, v3)
            .sortedBy { vtx -> vtx.y() }
            .toList()

        /*
        if (v[1].y() == v[2].y()) {
            //fillBottomFlatTriangle(v[0], v[1], v[2]);
        } else if (vt[0].y() == vt[1].y()) {
            //fillTopFlatTriangle(v[0], v[1], v[2]);
        } else {
            double slope = ((v[2].x() - v[0].x()) / v[2].y() - v[0].y()) * v[1].y();
        }
        */
        return null
    }

    override fun equals(o: Any?): Boolean {
        if (this === o) return true
        if (o !is Triangle) return false
        val triangle = o
        return v1 == triangle.v1 &&
                v2 == triangle.v2 &&
                v3 == triangle.v3
    }

    override fun hashCode(): Int {
        return Objects.hash(v1, v2, v3)
    }

    override fun toString(): String {
        return "Triangle[v1=$v1,v2=$v2,v3=$v3]"
    }

    init {
        this.v1 = Objects.requireNonNull(v1, "v1")
        this.v2 = Objects.requireNonNull(v2, "v2")
        this.v3 = Objects.requireNonNull(v3, "v3")
    }
}