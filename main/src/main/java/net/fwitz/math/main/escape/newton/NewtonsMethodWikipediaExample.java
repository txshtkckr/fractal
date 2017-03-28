package net.fwitz.math.main.escape.newton;

import net.fwitz.math.main.complex.functions.examples.simple.WikipediaExample;

public class NewtonsMethodWikipediaExample {
    public static void main(String[] args) {
        NewtonsMethodPlot.render(WikipediaExample.FN_NAME + " (Newton's Method)", WikipediaExample::fn);
    }
}
