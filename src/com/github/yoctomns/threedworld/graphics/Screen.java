package com.github.yoctomns.threedworld.graphics;

public class Screen extends Bitmap {
    public Bitmap3D perspectiveVision;

    public Screen(int width, int height) {
        super(width, height);
        this.perspectiveVision = new Bitmap3D(width, height);
    }

    public void render() {
        clear();
        perspectiveVision.render();
        perspectiveVision.renderFog();
        render(perspectiveVision, 0, 0);
    }

    public void update() {
    }
}
