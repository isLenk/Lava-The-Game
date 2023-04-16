package Talban.Final;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Random;

public class SlimeMob extends GameObject{

    // The main platform that the mob lives on
    private Platform base;

    // How fast the little mob can travel, used in addition to the platforms x velocity
    private float walkSpeed;

    // Random used for the player bounce and walkspeed
    private Random rng;

    // Randomly generate mob data and assign it a base
    public SlimeMob(PApplet sketch, Platform base) {
        super(sketch);
        this.base = base;
        rng = new Random();
        rectWidth = 30;
        rectHeight = 30;
        x = base.x;
        y = base.y - rectHeight;
        walkSpeed = rng.nextFloat() * 3 + 0.5f;
    }

    // Draw the little mob as a small rectangle
    public void display() {
        sketch.fill(sketch.color(0, 160, 50));
        sketch.rect(x, y, rectWidth, rectHeight);
    }

    public void mobUpdate(ArrayList<Player> playerList) {
        y += base.yVelocity;

        // Check for any player collision, make them bounce the player
        for (Player player : playerList) {
            if (this.intersect(player)) {
                player.forceJump(15);
                player.addHitXVelocity((rng.nextInt(2) == 1 ? -1 : 1f) * (rng.nextFloat() * 4f));
                player.y = top() - player.rectHeight;
            }
        }

        x += base.xVelocity + walkSpeed;

        // Flip directions if they are reaching the edges of the platform
        if (right() > base.right() || left() < base.left()) {
            walkSpeed *= -1;
        }

        // Push the player to the direction that the slime is travelling in
        for (Player player : playerList) {
            if (this.intersect(player)) {
                player.addHitXVelocity(base.xVelocity + walkSpeed);
            }
        }

    }
}
