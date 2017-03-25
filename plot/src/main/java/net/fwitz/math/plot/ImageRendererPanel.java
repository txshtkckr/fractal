package net.fwitz.math.plot;

import net.fwitz.math.complex.Complex;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static net.fwitz.math.complex.Complex.complex;

public abstract class ImageRendererPanel<P extends ImageRendererPanel<P, R>, R extends ImageRenderer> extends JPanel {
    private final AtomicReference<R> rendererRef = new AtomicReference<>();

    protected ImageRendererPanel() {
    }

    @Override
    protected void paintComponent(Graphics g) {
        getRenderer().ifPresent(r -> r.paint(g, this));
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
        final boolean incomplete = getRenderer().map(ImageRenderer::isWorking).orElse(true);
        return super.imageUpdate(img, infoflags, x, y, w, h) || incomplete;
    }

    public Optional<R> getRenderer() {
        for (; ; ) {
            final int width = getWidth();
            final int height = getHeight();
            if (width == 0 || height == 0) {
                return Optional.empty();
            }

            R renderer = rendererRef.get();
            if (renderer != null && renderer.width() == width && renderer.height() == height && !renderer.cancelled()) {
                return Optional.of(renderer);
            }

            final R oldRenderer = renderer;
            renderer = createRenderer(width, height);
            if (rendererRef.compareAndSet(oldRenderer, renderer)) {
                if (oldRenderer != null) {
                    oldRenderer.shutdown();
                }

                renderer.render();
                return Optional.of(renderer);
            }

            // Oops... lost the race, so make sure this renderer gets shutdown properly before we abandon it
            renderer.shutdown();
        }
    }

    protected abstract R createRenderer(int width, int height);

    public Optional<Complex> getMouseLocationAsRelativePoint() {
        int width = getWidth();
        int height = getHeight();
        if (width <= 0 || height <= 0) {
            // Panel dimensions unknown (hidden?)
            System.err.println("ImageRendererPanel: Panel dimensions unknown");
            return Optional.empty();
        }

        Point mouseLoc = MouseInfo.getPointerInfo().getLocation();
        if (mouseLoc == null) {
            // Mouse location unknown (moving it too fast?)
            System.err.println("ImageRendererPanel: Mouse location unknown");
            return Optional.empty();
        }

        Point panelLoc = getLocation();
        double x = (mouseLoc.getX() - panelLoc.getX()) / width;
        double y = (mouseLoc.getY() - panelLoc.getY()) / height;
        if (x < 0 || x > 1 || y < 0 || y > 1) {
            // Mouse was somewhere else
            System.err.println("ImageRendererPanel: Mouse location out-of-bounds: (" + x + ", " + y + ')');
            return Optional.empty();
        }

        return Optional.of(complex(x, y));
    }

    public void reset() {
        R newRenderer = createRenderer(getWidth(), getHeight());
        R oldRenderer = rendererRef.getAndSet(newRenderer);
        if (oldRenderer != null) {
            oldRenderer.shutdown();
        }
        newRenderer.render();
    }

    protected static double scaleToBounds(double value, double min, double max) {
        return value * max - value * min + min;
    }
}
