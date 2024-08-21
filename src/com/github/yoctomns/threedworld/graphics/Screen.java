package com.github.yoctomns.threedworld.graphics;

import com.github.yoctomns.threedworld.Game;

public class Screen extends Bitmap {
    public Bitmap3D perspectiveVision;

    public Screen(int width, int height) {
        super(width, height);
        this.perspectiveVision = new Bitmap3D(width, height);
    }

    public void render(Game game) {
        clear();
        perspectiveVision.render(game);
        perspectiveVision.renderFog();
        render(perspectiveVision, 0, 0);
    }

    public void update() {
    }
}
