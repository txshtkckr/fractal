# fractal

## Intro

This is just a playground for messing around with fractals. I'm really not much of a UI guy, so there is nothing in the way of professional polish here. I'm just doing fun stuff for the hell of it. I originally wrote this in Java, but when I started picking up Kotlin, I decided to put this through a major rewrite to take advantage of how much nicer Kotlin is.

Look in the "main" module for all the programs that you can run. Many of them have keyboard/mouse shortcuts for playing around with the image. Pretty much none of this is documented in the classes, so I guess I'll try to put a bit here in the readme...

## I just want a Mandelbrot Set!

Fine, run `MandelbrotPlot`.

## Quick tour of the code

The code is divided into 5 main modules:

* complex – Complex numbers and their close relatives (split-complex and dual numbers). I'm not a professional mathemetician, so I'm not claiming that I got all of this exactly right. Much of it is taken from translations of C code from various places.
* fractal – Most of the supporting logic for calculating various fractals. In particular, there is support for the Logistic Map / Bifurcation diagram, various escape time plots, and chaos-driven iterated functions.
* main – The various main programs that actually do something. If you want to see pretty pictures, start here.
* numth – A couple of tools related to number theory that I haven't really done much with yet.
* plot – Most of the UI code, generally split across a "panel" (direct Swing stuff), a plot (connects Swing to the rendering), and a renderer (directs calculations and the drawing process)

Renderers generally do their job in background threads and will usually abort as quickly as possible if they need to start over for some reason (like a changed window size). Some of the plotters know how to divide and conquer the problems (Mandelbrot goes row-at-a-time, for example) while others don't (Plasma makes multiple sequential passes over the whole picture). All of the panels will close if you press "q" or "Escape". The complex number renderers generally have a bit more power, with the ability to use various filters (press "m" or "M" to cycle through them) and zoom out or in with "[" and "]".

## Notable main programs:

* `BifurcLogisticMap` – The classic bifurcation diagram for `f(x) = rx(1-x)`. Mouse clicks center the image and "[" and "]" zoom out and in, respectively.
* `ZetaPlot` – The Reimann Zeta function. Eta, Xi, Erf, Erfc, and Erfi also have plots available.
* Complex number functions, like `SinZ` or `CubeMinus1`
* The same for split-complex numbers and dual numbers, like `SplitExpZ` or `DualExp1OverZ`
* Mandelbrot-style plots, like `MandelbrotPlot` and `MandelPiPlot`, that can be switched to/from Julia Set mode at the current mouse position by pressing "j".
* Other assorted escape-time plots, like `BurningShipPlot`, `CollatzPlot`, and `SignOfTheSunGod`.
* Interated function systems based on affine transformation matrices, with names like `IfsSierpinskiTriangle`, `IfsFern`, and `IfsVicsek`.
* Regular polygon chaos game fractals, with names like `ChaosTri`, `ChaosPentRejectSame`, and `ChaosDodec`.
* `Plasma` – Diamond-square algorithm.
