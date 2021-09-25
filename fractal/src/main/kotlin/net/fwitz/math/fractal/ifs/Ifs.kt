package net.fwitz.math.fractal.ifs

object Ifs {
    val SIERPINSKI_TRIANGLE = IfsParams.ifs(
        "Sierpinski Triangle", 0.0, 0.25, 0.5,
        IfsTransform(a = 0.5, b = 0.0, c = 0.0, d = 0.5, e = 0.0, f = 0.0, p = 1),
        IfsTransform(a = 0.5, b = 0.0, c = 0.0, d = 0.5, e = 1.0, f = 0.0, p = 1),
        IfsTransform(a = 0.5, b = 0.0, c = 0.0, d = 0.5, e = 0.5, f = 0.5, p = 1),
    )

    val SIERPINSKI_CARPET = IfsParams.ifs(
        "Sierpinski Carpet", 0.0, 0.0, 0.99,
        IfsTransform(a = 0.333, b = 0.0, c = 0.0, d = 0.333, e = 0.0, f = 0.0, p = 1),
        IfsTransform(a = 0.333, b = 0.0, c = 0.0, d = 0.333, e = 0.333, f = 0.0, p = 1),
        IfsTransform(a = 0.333, b = 0.0, c = 0.0, d = 0.333, e = 0.667, f = 0.0, p = 1),
        IfsTransform(a = 0.333, b = 0.0, c = 0.0, d = 0.333, e = 0.0, f = 0.333, p = 1),
        IfsTransform(a = 0.333, b = 0.0, c = 0.0, d = 0.333, e = 0.667, f = 0.333, p = 1),
        IfsTransform(a = 0.333, b = 0.0, c = 0.0, d = 0.333, e = 0.0, f = 0.667, p = 1),
        IfsTransform(a = 0.333, b = 0.0, c = 0.0, d = 0.333, e = 0.333, f = 0.667, p = 1),
        IfsTransform(a = 0.333, b = 0.0, c = 0.0, d = 0.333, e = 0.667, f = 0.667, p = 1),
    )

    val FERN_LEAF = IfsParams.ifs(
        "Fern leaf", 0.5, 0.05, 0.75,
        IfsTransform(a = 0.0, b = 0.0, c = 0.0, d = 0.16, e = 0.0, f = 0.0, p = 1),
        IfsTransform(a = 0.2, b = -0.26, c = 0.23, d = 0.22, e = 0.0, f = 0.2, p = 7),
        IfsTransform(a = -0.15, b = 0.28, c = 0.26, d = 0.24, e = 0.0, f = 0.2, p = 7),
        IfsTransform(a = 0.85, b = 0.04, c = -0.04, d = 0.85, e = 0.0, f = 0.2, p = 85)
    )

    val TREE = IfsParams.ifs(
        "Tree", 0.5, 0.25, 1.0,
        IfsTransform(a = 0.0, b = 0.0, c = 0.0, d = 0.5, e = 0.0, f = 0.0, p = 1),
        IfsTransform(a = 0.1, b = 0.0, c = 0.0, d = 0.1, e = 0.0, f = 0.2, p = 3),
        IfsTransform(a = 0.42, b = -0.42, c = 0.42, d = 0.42, e = 0.0, f = 0.2, p = 8),
        IfsTransform(a = 0.42, b = 0.42, c = -0.42, d = 0.42, e = 0.0, f = 0.2, p = 8),
    )

    val CANTOR_TREE = IfsParams.ifs(
        "Cantor Tree", -0.5, -0.5, 1.0,
        IfsTransform(a = 0.333, b = 0.0, c = 0.0, d = 0.333, e = 0.0, f = 0.0, p = 1),
        IfsTransform(a = 0.333, b = 0.0, c = 0.0, d = 0.333, e = 1.0, f = 0.0, p = 1),
        IfsTransform(a = 0.667, b = 0.0, c = 0.0, d = 0.667, e = 0.5, f = 0.5, p = 1),
    )
}

data class IfsParams(
    val title: String,
    val xOff: Double,
    val yOff: Double,
    val scale: Double,
    val transforms: List<IfsTransform>
) {
    companion object {
        fun ifs(
            title: String,
            xOff: Double,
            yOff: Double,
            scale: Double,
            vararg ifsTransforms: IfsTransform
        ): IfsParams {
            val list = listOf(*ifsTransforms)
            require(list.isNotEmpty()) { "An IFS with no transforms wouldn't do much, eh?" }
            return IfsParams(title, xOff, yOff, scale, list)
        }
    }
}

data class IfsTransform(
    val a: Double,
    val b: Double,
    val c: Double,
    val d: Double,
    val e: Double,
    val f: Double,
    val p: Int
)