package com.github.yoctomns.threedworld.graphics;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;

public class Textures {
    public static Bitmap floor = loadTexture("/textures/floors.png");

    public static Bitmap loadTexture(String path) {
        try {
            var image = ImageIO.read(Textures.class.getResourceAsStream(path));
            var resource = new Bitmap(image.getWidth(), image.getHeight());
            image.getRGB(0, 0, resource.width, resource.height, resource.pixels, 0, resource.width);
            for (var i = 0; i < resource.pixels.length; i++) {
                // alphaを削除: 0x 'ff' ffffff
                resource.pixels[i] = resource.pixels[i] & 0xffffff;
            }
            return resource;
        } catch (FileNotFoundException e) {
            System.err.println("指定されたパスのファイルが見つかりませんでした。 Path: " + path);
            e.printStackTrace();
        } catch (IOException e) {
            System.err.println("指定されたパスのファイルを正しく読み込めませんでした。 Path: " + path);
            e.printStackTrace();
        }
        return null;
    }
}
