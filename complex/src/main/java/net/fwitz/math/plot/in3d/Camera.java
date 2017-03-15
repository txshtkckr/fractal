package net.fwitz.math.plot.in3d;

public class Camera {
    private final double xPos;
    private final double yPos;
    private final double zPos;
    private final double xDir;
    private final double yDir;
    private final double zDir;

    public Camera(double xPos, double yPos, double zPos, double xDir, double yDir, double zDir) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.zPos = zPos;
        this.xDir = xDir;
        this.yDir = yDir;
        this.zDir = zDir;
    }
}
