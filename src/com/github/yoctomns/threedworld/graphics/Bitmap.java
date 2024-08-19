package com.github.yoctomns.threedworld.graphics;

public class Bitmap {
    protected int width;
    protected int height;
    protected int[] pixels;

    public Bitmap(int width, int height) {
        this.width = width;
        this.height = height;
        this.pixels = new int[width * height];
    }

    public void render(Bitmap b, int ox, int oy) {
        for (int y = 0; y < b.height; y++) {
            int yy = y + oy;
            if (yy < 0 || yy >= height) {
                continue;
            }
            for (int x = 0; x < b.width; x++) {
                int xx = x + ox;
                if (xx < 0 || xx >= width) {
                    continue;
                }
                int alpha = b.pixels[x + y * b.width];
                if (alpha > 0) {
                    pixels[xx + yy * width] = alpha;
                }
            }
        }
    }

    public int getPixel(int idx) {
        return pixels[idx];
    }

    public void clear() {
        for (var i = 0; i < pixels.length; i++) {
            pixels[i] = 0;
        }
    }
}
