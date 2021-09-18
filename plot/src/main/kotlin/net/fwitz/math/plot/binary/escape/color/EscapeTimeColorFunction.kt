package net.fwitz.math.plot.binary.escape.color

import net.fwitz.math.fractal.escape.EscapeTimeResult
import java.awt.Color

typealias EscapeTimeColorFunction<T> = (T, EscapeTimeResult<T>) -> Color