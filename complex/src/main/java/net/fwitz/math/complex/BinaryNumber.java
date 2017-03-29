package net.fwitz.math.complex;

import java.io.Serializable;

/**
 * A binary number has two components with some relationship defines as to how they interact under elementary
 * operations.  These take the form {@code x + ey}, where the two components are linearly independent under
 * addition, but where multiplicative operations produce {@code e^2} terms that behave in some special way.
 * For example:
 * <ul>
 * <li>{@code e^2 = -1} &mdash; Complex numbers ({@code e} is the square root of {@code -1}, and normally called
 *      {@code i}</li>
 * <li>{@code e^2 = 0} &mdash; "Dual" numbers</li>
 * <li>{@code e^2 = 1} &mdash; "Split-complex" or "hyperbolic" numbers</li>
 * </ul>
 * <p>
 * In each case, the standard arithmetic, exponential, logarithmic, trigonometric, and hyperbolic operations
 * can be extended into this new domain on the basis of the relationships derived from applying the Taylor
 * series expansions to the exponential function.  For complex numbers in particular, this leads to the incredibly
 * rich mathematics called Complex Analysis.
 * </p><p>
 * As all of these binary numbers share some common characteristics, it made sense to represent those capabilities
 * in the same way for each of them.
 * </p>
 */
public interface BinaryNumber<T extends BinaryNumber<T>> extends Serializable {
}
