package Talban.Final;

import processing.core.PApplet;
import processing.core.PImage;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public class BackgroundObject extends GameObject{
    private static File backgroundObjectFolder;
    private PImage myImage;
    private boolean frontLayer;

    // Returns whether this background object belongs on the foreground or background layer when drawing
    public boolean isFrontLayer() {
        return frontLayer;
    }

    // Create a random background object using the background image folder
    public BackgroundObject(PApplet sketch) {
        super(sketch);

        // Assign the background object folder variable the path to the background images if it hasn't already
        if (backgroundObjectFolder == null) {
            backgroundObjectFolder = new File(FilePath.get("/Talban/Final/Sprites/Background_Images"));
        }

        // list all the files inside of the background images folder
        File[] files = backgroundObjectFolder.listFiles();

        // Randomly decide if the background object is a front layer or not
        Random rand = new Random();
        frontLayer = rand.nextInt(C.foregroundObjectChance[1]) < C.foregroundObjectChance[0];

        yVelocity = C.backgroundObjectYVelocity;
        x = rand.nextInt(sketch.width);

        // Attempt to load a random background object
        try {
            File chosenFile = files[rand.nextInt(files.length-1)];
            Image image = ImageIO.read(chosenFile);
            // Assign the width and height according to the image dimensions
            rectWidth = image.getWidth(null);
            rectHeight = image.getHeight(null);
            myImage = sketch.loadImage(chosenFile.getAbsolutePath());
            y = 0 - rectHeight;
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void update() {
        y += yVelocity;
    }

    // Draw the background objects image to screen
    public void display() {
        sketch.image(myImage, x, y);
    }
}
