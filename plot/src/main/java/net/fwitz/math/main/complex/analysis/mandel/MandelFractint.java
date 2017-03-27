package net.fwitz.math.main.complex.analysis.mandel;

/**
 * Port of fractint's x86 assembly code for Mandelbrot/Julia generation.
 */
public class MandelFractint {


    // externs
    private int fractype;  // false = mandle, else julia
    private int inside;  // incide color, normally 1 (blue)

    // locals
    private long x;
    private long y;
    private long linitx;
    private long linity;
    private long savedx;
    private long savedy;
    private int k;
    private int old;
    private long savedand;
    private long savedincr;
    private short period;


    private void calcmand() {
        long eax = 0;
        long edx = 0;
    }
}
