package net.fwitz.math.plot;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.geom.Point2D;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public abstract class ImageRendererPanel<R extends ImageRenderer>
        extends JPanel
        implements MouseListener {

    private final AtomicReference<R> rendererRef = new AtomicReference<>();
    private final AtomicBoolean registeredMouseListener = new AtomicBoolean();

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

                if (!registeredMouseListener.getAndSet(true)) {
                    addMouseListener(this);
                }

                renderer.render();
                return Optional.of(renderer);
            }

            // Oops... lost the race, so make sure this renderer gets shutdown properly before we abandon it
            renderer.shutdown();
        }
    }

    protected abstract R createRenderer(int width, int height);

    public Optional<Point2D.Double> getMouseLocationAsRelativePoint() {
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

        Point panelLoc = getLocationOnScreen();
        double x = (mouseLoc.getX() - panelLoc.getX()) / width;
        double y = (mouseLoc.getY() - panelLoc.getY()) / height;
        if (x < 0 || x > 1 || y < 0 || y > 1) {
            // Mouse was somewhere else
            System.err.println("ImageRendererPanel: Mouse location out-of-bounds: (" + x + ", " + y + ')');
            return Optional.empty();
        }

        return Optional.of(new Point2D.Double(x, y));
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

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    // Glue code to make AbstractAction suck less by looking like a functional interface

    public static AbstractAction action(ActionHandler handler) {
        return new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handler.actionPerformed(e);
            }
        };
    }

    @FunctionalInterface
    public interface ActionHandler {
        void actionPerformed(ActionEvent e);
    }
}
