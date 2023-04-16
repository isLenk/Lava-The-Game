package Talban.Final;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class JImage {
    public static PImage getImage(PApplet sketch, String url){
        BufferedImage bi = null;
        try {
            bi = ImageIO.read(PApplet.class.getResourceAsStream(url));
        } catch (IOException e) {
            e.printStackTrace();
        }
        PGraphics g = sketch.createGraphics(bi.getWidth(), bi.getHeight());
        g.beginDraw();
        Graphics2D g2d = (Graphics2D) g.getNative();
        g2d.drawImage(bi, 0, 0, bi.getWidth(), bi.getHeight(), null);
        g.endDraw();
        PImage b = g.copy();
        return b;

    }
}
