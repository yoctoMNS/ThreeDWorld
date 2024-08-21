package com.github.yoctomns.threedworld.graphics;

import java.util.Random;

import com.github.yoctomns.threedworld.Game;

public class Bitmap3D extends Bitmap {
    private double fov;
    private double xCam;
    private double yCam;
    private double zCam;
    private double rot;
    private double rSin;
    private double rCos;
    private double[] depthBuffer;

    public Bitmap3D(int width, int height) {
        super(width, height);
        this.fov = height;
        this.depthBuffer = new double[width * height];
    }

    public void render(Game game) {
        // xCam = 0;
        // yCam = game.getTime() % 100.0 / 50;
        // zCam = Math.sin(game.getTime() / 10.0) * 2;
        rot = Math.sin(game.getTime() / 30.0) * 0.3;
        rSin = Math.sin(rot);
        rCos = Math.cos(rot);
        for (int y = 0; y < height; y++) {
            double yd = (y - (height / 2)) / fov;
            double zd = (4 + zCam) / yd;
            if (yd < 0) {
                zd = (4 - zCam) / -yd;
            }
            for (int x = 0; x < width; x++) {
                double xd = (x - (width / 2)) / fov;
                xd *= zd;
                double xx = xd * rCos - zd * rSin + (xCam + 1) * 4;
                double yy = xd * rSin + zd * rCos + (yCam) * 4;
                int xPix = (int) xx * 2;
                int yPix = (int) yy * 2;
                if (xx < 0) {
                    xPix--;
                }
                if (yy < 0) {
                    yPix--;
                }
                depthBuffer[x + y * width] = zd;
                pixels[x + y * width] = Textures.floor.pixels[(xPix & 0xf) + 16 | (yPix & 0xf) * Textures.floor.width];
            }
        }
        renderWall(-2, 2, 2, 2);
    }

    Random random = new Random(100);

    /*
     * Algorithm that is used to render a wall is 'Linear Interpolation'.
     * y = y0 + (x - x0) * (y1 - y0) / (x1 - x0)
     * More information about 'Linear Interpolation' :
     * https://ja.wikipedia.org/wiki/%E7%B7%9A%E5%BD%A2%E8%A3%9C%E9%96%93
     */
    // TODO Refactoring.
    private void renderWall(double x0, double y0, double x1, double y1) {
        for (var i = 0; i < 10000; i++) {
            double xo0 = x0 - 0.5 - xCam;
            double u0 = 0 - 0.5 + zCam / 4;
            double d0 = 0 + 0.5 + zCam / 4;
            double zo0 = y0 - yCam * 2;
            double xx0 = xo0 * rCos + zo0 * rSin;
            double zz0 = -xo0 * rSin + zo0 * rCos;

            double xo1 = x1 - 0.5 - xCam;
            double u1 = 0 - 0.5 + zCam / 4;
            double d1 = 0 + 0.5 + zCam / 4;
            double zo1 = y1 - yCam * 2;
            double xx1 = xo1 * rCos + zo1 * rSin;
            double zz1 = -xo1 * rSin + zo1 * rCos;

            double xPix0 = xx0 / zz0 * fov + width / 2.0;
            double xPix1 = xx1 / zz1 * fov + width / 2.0;

            if (xPix0 > xPix1) {
                return;
            }

            int xp0 = (int) xPix0;
            int xp1 = (int) xPix1;

            if (xp0 < 0) {
                xp0 = 0;
            }
            if (xp1 > width) {
                xp1 = width;
            }

            double yPix00 = u0 / zz0 * fov + height / 2.0;
            double yPix01 = d0 / zz0 * fov + height / 2.0;
            double yPix10 = u1 / zz1 * fov + height / 2.0;
            double yPix11 = d1 / zz1 * fov + height / 2.0;

            for (var x = xp0; x < xp1; x++) {
                double yPix0 = yPix00 + (x - xPix0) * (yPix10 - yPix00) / (xPix1 - xPix0);
                double yPix1 = yPix01 + (x - xPix0) * (yPix11 - yPix01) / (xPix1 - xPix0);
                if (yPix0 > yPix1) {
                    return;
                }

                int yp0 = (int) yPix0;
                int yp1 = (int) yPix1;

                if (xp0 < 0) {
                    xp0 = 0;
                }
                if (xp1 > width) {
                    xp1 = width;
                }

                for (var y = yp0; y < yp1; y++) {
                    depthBuffer[x + y * width] = 0;
                    pixels[x + y * width] = 0xff00ff;
                }
            }
        }
    }

    // テストメソッド
    private void drawFloor(int x, int y, int posX, int posY, int xPix, int yPix, double xx, double yy, double yd) {
        if (yd >= 0 && xx >= posX * 16 && xx < posX * 16 + 16 && yy >= posY * 16 && yy < posY * 16 + 16) {
            pixels[x + y * width] = Textures.floor.pixels[(xPix & 0xf) + 16 | (yPix & 0xf) * Textures.floor.width];
        }
    }

    // テストメソッド
    private void drawCeiling(int x, int y, int posX, int posY, int xPix, int yPix, double xx, double yy, double yd) {
        if (yd <= 0 && xx >= posX * 16 && xx < posX * 16 + 16 && yy >= posY * 16 && yy < posY * 16 + 16) {
            pixels[x + y * width] = Textures.floor.pixels[(xPix & 0xf) + 16 | (yPix & 0xf) * Textures.floor.width];
        }
    }

    public void renderFog() {
        for (int i = 0; i < depthBuffer.length; i++) {
            int color = pixels[i];
            /*
             * 0x ff ff ff
             * 0x R_ G_ B_
             */
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
