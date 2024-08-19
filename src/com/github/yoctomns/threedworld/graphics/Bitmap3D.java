package com.github.yoctomns.threedworld.graphics;

public class Bitmap3D extends Bitmap {
    private double fov;
    private double xCam;
    private double yCam;
    private double zCam;
    private double rot;
    private int t;
    private double[] depthBuffer;

    public Bitmap3D(int width, int height) {
        super(width, height);
        this.fov = height;
        this.depthBuffer = new double[width * height];
    }

    public void render() {
        // t++;
        // xCam = t / 50.0;
        // yCam = t / 50.0;
        // zCam = Math.sin(t / 100.0);
        // rot = t / 800.0;
        double rSin = Math.sin(rot);
        double rCos = Math.cos(rot);
        for (int y = 0; y < height; y++) {
            double yd = (y - (height / 2)) / fov;
            double zd = (8 + zCam) / yd;
            if (yd < 0) {
                zd = (8 - zCam) / -yd;
            }
            for (int x = 0; x < width; x++) {
                double xd = (x - (width / 2)) / fov;
                xd *= zd;
                double xx = (xd * rCos - zd * rSin + xCam);
                double yy = (xd * rSin + zd * rCos + yCam);
                int xPix = (int) xx;
                int yPix = (int) yy;
                if (xx < 0) {
                    xPix--;
                }
                if (yy < 0) {
                    yPix--;
                }
                depthBuffer[x + y * width] = zd;
                pixels[x + y * width] = Textures.floor.pixels[(xPix & 0xf) | (yPix & 0xf) * Textures.floor.width];
                drawFloor(x, y, 0, 2, xPix, yPix, xd, yd, zd);
                drawFloor(x, y, 0, 3, xPix, yPix, xd, yd, zd);
                drawFloor(x, y, 1, 2, xPix, yPix, xd, yd, zd);
                drawFloor(x, y, 1, 3, xPix, yPix, xd, yd, zd);
                drawCeiling(x, y, -1, 2, xPix, yPix, xd, yd, zd);
                drawCeiling(x, y, -1, 3, xPix, yPix, xd, yd, zd);
                drawCeiling(x, y, 1, 2, xPix, yPix, xd, yd, zd);
                drawCeiling(x, y, 1, 3, xPix, yPix, xd, yd, zd);
            }
        }
    }

    private void drawFloor(int x, int y, int posX, int posY, int xPix, int yPix, double xd, double yd, double zd) {
        if (yd >= 0 && xd >= posX * 16 && xd < posX * 16 + 16 && zd >= posY * 16 && zd < posY * 16 + 16) {
            pixels[x + y * width] = Textures.floor.pixels[(xPix & 0xf) + 16 | (yPix & 0xf) * Textures.floor.width];
        }
    }

    private void drawCeiling(int x, int y, int posX, int posY, int xPix, int yPix, double xd, double yd, double zd) {
        if (yd <= 0 && xd >= posX * 16 && xd < posX * 16 + 16 && zd >= posY * 16 && zd < posY * 16 + 16) {
            pixels[x + y * width] = Textures.floor.pixels[(xPix & 0xf) + 16 | (yPix & 0xf) * Textures.floor.width];
        }
    }

    public void renderFog() {
        for (int i = 0; i < depthBuffer.length; i++) {
            int color = pixels[i];
            //    R  G  B
            // 0x ff ff ff
            int r = (color >> 16) & 0xff;
            int g = (color >> 8) & 0xff;
            int b = (color) & 0xff;
            double brightness = 255 - depthBuffer[i] * 2;
            r = (int) (r / 255.0 * brightness);
            g = (int) (g / 255.0 * brightness);
            b = (int) (b / 255.0 * brightness);
            pixels[i] = r << 16 | g << 8 | b;
        }
    }
}
