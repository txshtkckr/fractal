package net.fwitz.math.plot;

import net.fwitz.math.complex.Complex;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiFunction;
import java.util.function.Function;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class FunctionPanel extends JPanel {
    private final AtomicReference<ImageRenderer> rendererRef = new AtomicReference<>();

    final double minRe;
    final double maxRe;
    final double minIm;
    final double maxIm;
    final Function<Complex, Complex> computeFunction;
    final BiFunction<Complex, Complex, Color> colorFunction;


    public FunctionPanel(
            Complex domainBound1,
            Complex domainBound2,
            Function<Complex, Complex> computeFunction,
            BiFunction<Complex, Complex, Color> colorFunction) {
        this.minRe = min(domainBound1.re(), domainBound2.re());
        this.maxRe = max(domainBound1.re(), domainBound2.re());
        this.minIm = min(domainBound1.im(), domainBound2.im());
        this.maxIm = max(domainBound1.im(), domainBound2.im());
        this.computeFunction = computeFunction;
        this.colorFunction = colorFunction;
    }


    @Override
    protected void paintComponent(Graphics g) {
        final ImageRenderer renderer = getRenderer();
        if (renderer != null) {
            g.drawImage(getRenderer().image, 0, 0, Color.DARK_GRAY, this);
        }
    }

    @Override
    public boolean imageUpdate(Image img, int infoflags, int x, int y, int w, int h) {
        final ImageRenderer renderer = getRenderer();
        final boolean incomplete = renderer == null || renderer.isAlive();
        return super.imageUpdate(img, infoflags, x, y, w, h) || incomplete;
    }

    void registerListeners(JFrame frame) {
        getInputMap().put(KeyStroke.getKeyStroke('q'), "quit");
        getInputMap().put(KeyStroke.getKeyStroke('m'), "mode");

        getActionMap().put("quit", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                RenderingPipeline.shutdown();
                System.exit(0);
            }
        });
        getActionMap().put("mode", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final ImageRenderer renderer = getRenderer();
                if (renderer != null) {
                    renderer.toggleMode();
                }
            }
        });
    }

    private ImageRenderer getRenderer() {
        for (; ; ) {
            final int width = getWidth();
            final int height = getHeight();
            if (width == 0 || height == 0) {
                return null;
            }

            ImageRenderer renderer = rendererRef.get();
            if (renderer != null && renderer.width == width && renderer.height == height) {
                return renderer;
            }

            final ImageRenderer oldRenderer = renderer;
            renderer = new ImageRenderer(this, width, height);
            if (rendererRef.compareAndSet(oldRenderer, renderer)) {
                if (oldRenderer != null) {
                    oldRenderer.interrupt();
                }

                renderer.start();
                return renderer;
            }
        }
    }
}
