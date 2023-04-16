package Talban.Final;

import processing.core.PApplet;

public class GrappleHead extends GameObject {

    // Create a 10x10 object
    public GrappleHead(PApplet sketch) {
        super(sketch);
        rectWidth = 10;
        rectHeight = 10;
    }

    // Set position of grapple head to the given ones. Used to check collision
    public void update(float newX, float newY) {
        this.x = newX;
        this.y = newY;
    }

}
