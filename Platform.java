package Talban.Final;
import java.util.Random;

import processing.core.PApplet;
import processing.core.PImage;

public class Platform extends GameObject {

    // Stores the platforms images
    private static PImage body;
    private static PImage right;
    private static PImage left;

    // Stores the mob that the platform holds. Was supposed to be able to hold different types but only the slime was made.
    private SlimeMob mob;

    // Returns the mob object
    public SlimeMob getMob() {
        return mob;
    }

    public Platform(PApplet sketch) {
        super(sketch);
        Random rng = new Random();
        y = -50;
        // Randomly choose where to place the x position of the graph is
        x = rng.nextInt(sketch.width - C.platformEdgeOffset*2) + C.platformEdgeOffset;
        yVelocity = C.objectYVelocity;
        rectHeight = 20;
        rectWidth = (rng.nextInt(C.platformMaxLength - C.platformMinLength) + C.platformMinLength);
        rectWidth = rectWidth - (rectWidth % 20);

        // Load the platform body images in if they haven't already
        if (body == null) {
            body = JImage.getImage(sketch,"/Talban/Final/Sprites/Platform/Body.png");
            right = JImage.getImage(sketch,"/Talban/Final/Sprites/Platform/Right.png");
            left = JImage.getImage(sketch,"/Talban/Final/Sprites/Platform/Left.png");
        }

        // Add x velocity if this platform randomly is selected ot be a moving platform
        if (rng.nextInt(C.movingPlatformChance[0]) < C.movingPlatformChance[1]) {
            xVelocity = (rng.nextFloat() * C.objectXVelocity) * ((rng.nextInt(2) == 1) ? -1 : 1);
        }

        // Add a platform mob if the platform rolled the right chance
        if (rng.nextInt(C.platformMobSpawnChance[1]) < C.platformMobSpawnChance[0]) {
            mob = new SlimeMob(sketch, this);
        }
    }

    public void update() {
        lastY = y;
        lastX = x;

        y += yVelocity;
        x += xVelocity;

        // Flip directions if the platform hits the edge of the screen
        if (right() >= sketch.width || left() <= 0) {
            xVelocity *= -1;
        }
    }

    public void display() {
        // A for loop that spaces the x coordinate every 20 pixels
        for (float _x = x; _x <= x+rectWidth; _x += 20) {
            // Draw left of the image
            if (_x == x) {
                sketch.image(left, _x, y);
            }
            // Draw the right of the image
            else if (_x >= x+rectWidth-10) {
                sketch.image(right, _x, y);
            }
            // Draw the body image
            else {
                sketch.image(body, _x, y);
            }
        }
    }
}

