package net.fwitz.math.main.binary.escape;

public class MandelcubedPlot extends MandelbrotPlot {
    public static void main(String[] args) {
        new MandelcubedPlot().render();
    }

    public MandelcubedPlot() {
        super(3);
    }
}
