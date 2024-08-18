package com.github.yoctomns.threedworld.graphics;

import java.util.Random;

public class Screen extends Bitmap {
    private Random random = new Random();
    public Bitmap test;
    public Bitmap3D perspectiveVision;

    public Screen(int width, int height) {
        super(width, height);
        test = new Bitmap(50, 50);
        for (var i = 0; i < test.pixels.length; i++) {
            test.pixels[i] = random.nextInt();
        }
        this.perspectiveVision = new Bitmap3D(width, height);
    }

    public void render() {
        perspectiveVision.render();
        render(perspectiveVision, 0, 0);
    }

    public void update() {
    }
}
