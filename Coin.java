package Talban.Final;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Random;

public class Coin extends GameObject {

    // Determine whether the coin object can activate again
    private boolean hit;

    public boolean hit() {
        return hit;
    }
    // Construct coin with a randomized x position and set on top of screen
    public Coin(PApplet sketch) {
        super(sketch);
        rectWidth = 20;
        rectHeight = 20;
        x = new Random().nextInt(sketch.width);
        y = -rectHeight;
        yVelocity = C.objectYVelocity;
        hit = false;
    }

    // Increase y position and check to see if any player is in contact with the coin
    public void pUpdate(ArrayList<Player> playerList) {
        y += yVelocity;
        for (Player player : playerList) {
            if (player.intersect(this)) {
                // Disable self while shrinking, and then add a point and make the player jump a bit
                hit = true;
                rectWidth = 10;
                player.addPoint();
                player.forceJump(5);
            }
        }

    }

    // Draw a yellow coin
    public void display() {
        sketch.stroke(194, 166, 52);
        sketch.fill(255, 218, 69);
        sketch.circle(x, y, rectWidth);
    }

}
