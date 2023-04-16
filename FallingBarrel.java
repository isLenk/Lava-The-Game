package Talban.Final;

import processing.core.PApplet;
import processing.core.PImage;

import java.util.ArrayList;
import java.util.Random;

public class FallingBarrel extends GameObject {

    // A variable that turns on when the player collides with the barrel
    private boolean hit;

    // How far the kaboom will reach
    private float explosionRadius;

    // How far the kaboom is at
    private float currExplosionRadius;

    // Boolean that determined whether or not the players have been thrusted
    private boolean exploded;

    // Indicates when it is appropriate to remove the barrel from the main ArrayList
    private boolean doneAnimation;

    // The maximum thrusts in each direction that the barrel can force the player
    private float xMaxThrust;
    private float yMaxThrust;

    // Animation sequence of the barrel
    private static PImage[] barrelTexture;

    // Current frame in the animation sequence
    private int currentFrame;

    // Tick counter that determines when to go to next frame in animation
    private int nextFrameTick;

    public FallingBarrel(PApplet sketch) {
        super(sketch);
        Random rng = new Random();
        rectWidth = 50;
        rectHeight = 50;

        x = 100 + rng.nextInt(sketch.width - 200);
        y = -rectHeight - 250 - rng.nextInt(500);

        yVelocity = 6;
        explosionRadius = 200;

        doneAnimation = false;
        exploded = false;
        hit = false;

        xMaxThrust = C.fallingBarrelXThrust + rng.nextInt(C.fallingBarrelXThrust*2);
        yMaxThrust = C.fallingBarrelYThrust + rng.nextInt(C.fallingBarrelYThrust*2);

        currentFrame = 0;
        nextFrameTick = 6;

        // Load in the barrel's textures
        barrelTexture = new PImage[4];
        for (int i = 0; i < 4; i++) {
            barrelTexture[i] = JImage.getImage(sketch, "/Talban/Final/Sprites/Falling_Barrel/ExplodingBarrel" + i + ".png");
        }
    }

    // Checks to see if the barrel object can be removed from the list
    public boolean canDelete() {
        return doneAnimation;
    }

    // Checks if the barrel has already thrusted the players in a direction
    public boolean hasExploded() {
        return exploded;
    }

    // Throws the player into a random direction according to their position relative to the center of the barrel
    public void exploded(ArrayList<Player> playerList) {
        exploded = true;

        // Thrust players in direction
        for (Player player : playerList) {
            if (player.getDistanceFromTarget(this) < explosionRadius) {

                // Calculate how much thrust to give the player and then multiply by -1 if the player is to the left of the barrel
                float xThrust = xMaxThrust/2 + (player.getDistanceFromTarget(this) / explosionRadius) * (xMaxThrust) * ((this.x - player.x < 0) ? -1 : 1);

                // Same calculation as the xThrust except there is no check to see if the are to the left of the barrel
                float yThrust = yMaxThrust/2 + (player.getDistanceFromTarget(this) / explosionRadius) * yMaxThrust;


                // The barrel is underneath the player
                if (this.y - player.y > 0) {
                    player.forceJump((int)yThrust);
                }
                else {
                    player.forceFall((int)yThrust/2);
                }

                player.addHitXVelocity(xThrust);
                player.giveJump(1);
            }
        }
    }

    // Returns if the barrel has already been in contact with a player
    public boolean isHit() {
        return hit;
    }

    // Sets the hit variable to true, called when in contact with player
    public void setHitTrue() {
        hit = true;
    }

    // Collision Free. Gravity parameter: If set to true, apply gravity acceleration
    public void pUpdate(ArrayList<Player> playerList) {
        lastY = y;
        lastX = x;

        y += yVelocity + C.gravityAcceleration;
        x += xVelocity;

        // Checks to see if the barrel is on screen
        if (y > 0) {
            for (GameObject player : playerList) {
                // Checks if the player is within close proximity to the barrel
                if (getDistanceFromTarget(player) < 50 + rectWidth) {
                    hit = true;
                }
            }
        }
    }

    public void display() {
        // Draw arrow indicating the position of the barrel and draw a line showing the path the barrel will take
        if (y < 0) {
            sketch.fill(255, 50, 50, 200);
            sketch.noStroke();
            sketch.triangle((rectWidth/2 + x) - 8, 15, rectWidth/2 + x, 0, (rectWidth/2 + x) + 8, 15);

            // Fade the path indicator as it nears the screen
            sketch.fill(250, (Math.abs(y)/500) * 255);

            for (int i = (int)y; i < sketch.height; i += 50) {
                sketch.circle(x + rectWidth/2, i, 4);
            }
        }

        // Frame by Frame animation handler
        nextFrameTick -= 1;
        if (nextFrameTick == 0) {
            nextFrameTick = 6;
            currentFrame = (currentFrame == barrelTexture.length-1) ? 0 : currentFrame + 1;
        }

        sketch.image(barrelTexture[currentFrame], x, y);

        // Draw explosion effect if the barrel has been hit
        if (hit) {
            currExplosionRadius+=15;
            sketch.fill(150, 190);
            sketch.circle(x+rectWidth/2f, y+rectHeight/2f, currExplosionRadius);

            // When the explosion reaches its maximum radius, prepare it for removal
            if (currExplosionRadius >= explosionRadius) {
                doneAnimation = true;
            }
        }
    }
}
