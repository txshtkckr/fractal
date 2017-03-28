package net.fwitz.math.plot.renderer;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.util.stream.IntStream;

public abstract class ImageRenderer {
    protected final int width;
    protected final int height;
    protected final BufferedImage image;
    protected final RenderingPipeline pipeline;

    public ImageRenderer(int width, int height) {
        this.width = width;
        this.height = height;
        this.image = new BufferedImage(width, height, BufferedImage.TYPE_4BYTE_ABGR);
        this.pipeline = RenderingPipeline.create();
    }

    public int width() {
        return width;
    }

    public int height() {
        return height;
    }

    public boolean isWorking() {
        return pipeline.isWorking();
    }

    public abstract void render();

    public void shutdown() {
        pipeline.shutdown();
    }

    public boolean cancelled() {
        return pipeline.isShutdown();
    }

    protected void applyRowColors(int y, int[] colors) {
        image.setRGB(0, y, colors.length, 1, colors, 0, 0);
    }

    protected double[] partition(int buckets, double valueMin, double valueMax) {
        double valueDomain = valueMax - valueMin;
        return IntStream.range(0, buckets)
                .mapToDouble(bucket -> valueMin + valueDomain * (bucket + 0.5) / buckets)
                .toArray();
    }

    public void paint(Graphics g, ImageObserver observer) {
        g.drawImage(image, 0, 0, Color.DARK_GRAY, observer);
    }

    @Override
    public String toString() {
        return "ImageRenderer[" + width + 'x' + height + ']';
    }
}
