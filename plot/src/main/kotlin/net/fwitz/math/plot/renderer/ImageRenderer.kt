package net.fwitz.math.plot.renderer

import java.awt.Color
import java.awt.Graphics
import java.awt.image.BufferedImage
import java.awt.image.ImageObserver
import java.util.stream.IntStream

abstract class ImageRenderer(
    val width: Int,
    val height: Int,
    val background: Color = Color.DARK_GRAY
) {
    companion object {
        fun partition(buckets: Int, valueMin: Double, valueMax: Double): DoubleArray {
            val valueDomain = valueMax - valueMin
            return IntStream.range(0, buckets)
                .mapToDouble { bucket: Int -> valueMin + valueDomain * (bucket + 0.5) / buckets }
                .toArray()
        }
    }

    val image = BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR)
    val pipeline = RenderingPipeline.create()
    val isWorking get() = pipeline.isWorking

    abstract fun render()

    fun shutdown() {
        pipeline.shutdown()
    }

    fun cancelled() = pipeline.isShutdown

    protected open fun applyRowColors(y: Int, colors: IntArray) {
        image.setRGB(0, y, colors.size, 1, colors, 0, 0)
    }

    fun paint(g: Graphics, observer: ImageObserver?) {
        g.drawImage(image, 0, 0, background, observer)
    }

    override fun toString(): String {
        return "ImageRenderer[" + width + 'x' + height + ']'
    }
}