package net.fwitz.math.plot.color.filter;

import net.fwitz.math.complex.Complex;

import static net.fwitz.math.complex.Complex.imaginary;
import static net.fwitz.math.complex.Complex.real;

public class ComplexFunctionValueFilters extends ValueFilters<Complex> {
    private static final ComplexFunctionValueFilters INSTANCE = new ComplexFunctionValueFilters();

    public static ComplexFunctionValueFilters getInstance() {
        return INSTANCE;
    }

    private ComplexFunctionValueFilters() {
        super(
                new ValueFilter<>("Real only", z -> real(z.re())),
                new ValueFilter<>("Imaginary only", z -> imaginary(z.im()))
        );
    }
}
