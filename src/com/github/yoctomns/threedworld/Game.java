package com.github.yoctomns.threedworld;

import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

import javax.swing.JFrame;

import com.github.yoctomns.threedworld.graphics.Screen;

public class Game extends Canvas implements Runnable {
    private static final int WIDTH = 640;
    private static final int HEIGHT = WIDTH * 3 / 4;
    private static final int SCLAE = 1;
    private static final String TITLE = "3D World";
    private static final double ONE_SEC_NANOTIME = 1000000000.0;
    private static final double FRAME_LIMIT = 60.0;
    private static final double TARGET_FRAME = ONE_SEC_NANOTIME / FRAME_LIMIT;
    private static final BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
    private static final int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();

    private boolean isRunning;
    private Screen screen;

    public Game() {
        this.screen = new Screen(WIDTH, HEIGHT);
    }

    public static void main(String[] args) {
        var frame = new JFrame(TITLE);
        var game = new Game();
        frame.setResizable(false);
        var dimension = new Dimension(WIDTH * SCLAE, HEIGHT * SCLAE);
        game.setMinimumSize(dimension);
        game.setMaximumSize(dimension);
        game.setPreferredSize(dimension);
        frame.add(game);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setAlwaysOnTop(true);
        frame.setVisible(true);
        game.start();
    }

    private void render() {
        var bs = getBufferStrategy();
        if (bs == null) {
            createBufferStrategy(3);
            return;
        }
        var g = bs.getDrawGraphics();
        for (var i = 0; i < pixels.length; i++) {
            pixels[i] = 0;
        }
        screen.render();
        for (var i = 0; i < pixels.length; i++) {
            pixels[i] = screen.getPixel(i);
        }
        g.drawImage(image, 0, 0, WIDTH * SCLAE, HEIGHT * SCLAE, null);
        g.dispose();
        bs.show();
    }

    private void update() {
        screen.update();
    }

    private void start() {
        if (isRunning) {
            return;
        }
        isRunning = true;
        new Thread(this).start();
    }

    @Override
    public void run() {
        var last = System.nanoTime();
        var delta = 0d;
        var timer = 0;
        var updates = 0;
        var frames = 0;
        while (isRunning) {
            var now = System.nanoTime();
            delta += (now - last) / TARGET_FRAME;
            timer += now - last;
            last = now;
            if (delta >= 1) {
                updates++;
                update();
                delta--;
            }
            render();
            frames++;
            if (timer >= ONE_SEC_NANOTIME) {
                System.out.println("Frames : " + frames + ", Updates : " + updates);
                frames = updates = 0;
                timer = 0;
            }
        }
        dispose();
    }

    private void stop() {
        if (!isRunning) {
            return;
        }
        isRunning = false;
    }

    private void dispose() {
        System.exit(0);
    }
}
