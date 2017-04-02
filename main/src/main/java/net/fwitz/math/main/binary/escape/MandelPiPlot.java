package net.fwitz.math.main.binary.escape;

public class MandelPiPlot extends MandelbrotPlot {
    public static void main(String[] args) {
        new MandelcubedPlot().render();
    }

    public MandelPiPlot() {
        super(Math.PI);
    }
}
