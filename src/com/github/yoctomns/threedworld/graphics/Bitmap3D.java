package com.github.yoctomns.threedworld.graphics;

public class Bitmap3D extends Bitmap {
    private static final int IMAGE_WIDTH = 16;
    private static final int IMAGE_HEIGHT = 16;
    private static final int ONE_BYTE = 8;
    private double fov;
    private double xCam;
    private double yCam;
    private double zCam;
    private double rot;
    private int t;

    public Bitmap3D(int width, int height) {
        super(width, height);
        this.fov = height;
    }

    public void render() {
        t++;
        xCam = t / 100.0;
        yCam = t / 100.0;
        zCam = Math.sin(t / 100.0);
        rot = t / 300.0;
        double rSin = Math.sin(rot);
        double rCos = Math.cos(rot);
        for (var y = 0; y < height; y++) {
            double yd = (y - (height / 2)) / fov;
            double zd = (6 + zCam) / yd;
            if (yd < 0) {
                zd = (6 - zCam) / -yd;
            }
            for (var x = 0; x < width; x++) {
                double xd = (x - (width / 2)) / fov;
                xd *= zd;
                int xPix = (int) (xd * rCos - zd * rSin + xCam);
                int yPix = (int) (xd * rSin + zd * rCos + yCam);
                pixels[x + y * width] = ((yPix & 0xff) * IMAGE_HEIGHT) << ONE_BYTE | ((xPix & 0xff) * IMAGE_WIDTH);
            }
        }
    }
}
