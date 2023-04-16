package Woodrow.Final;
import java.util.Random;

import processing.core.PApplet;

public class Platform extends GameObject {
    Random rng;
    public Platform(PApplet sketch) {
        super(sketch);
        rng = new Random();
        y = -50;
        x = rng.nextInt(sketch.width - C.platformEdgeOffset*2) + C.platformEdgeOffset;
        yVelocity = C.objectYVelocity;
        rectHeight = C.platformHeight;
        rectWidth = rng.nextInt(C.platformMaxLength - C.platformMinLength) + C.platformMinLength;
    }

}

