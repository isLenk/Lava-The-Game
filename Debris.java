package Talban.Final;


import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Random;

public class Debris {
    PApplet sketch;

    // Debris list stores all the debris objects that the game runner will then display and update
    private static ArrayList<Debris> debrisList;

    // Stores all particles of each debris.
    private ArrayList<Particle> particle;

    // Stores random object for generating unique explosion effects
    Random rng;

    // Basic properties of the debris
    float x, y;
    int backgroundColour;

    // How long the debris stays alive until it is removed from the loop
    int lifespan;

    // How long each debris particle is
    float length;

    // Either cube or ball shaped particles
    String shape;

    // Initialize the Debris Class. Basically just assigns debris list an array list
    public static void init() {
        debrisList = new ArrayList<>();
    }

    // Returns the debris list (Used for Game Runner to update)
    public static ArrayList<Debris> getDebrisList() {
        return debrisList;
    }

    // Removes the element in debris list that has said index
    public static void remove(int index) {
        debrisList.remove(index);
    }

    // Shortened method that adds debris to the list. Contains default length and shape
    public static void add(PApplet sketch, float x, float y, int color, int particleCount) {
        debrisList.add(new Debris(sketch, x, y, color , particleCount));
    }

    // Lengthened method that adds debris to the list. Customizable length and shape
    public static void add(PApplet sketch, float x, float y, int color, int particleCount,  float length, String shape) {
        debrisList.add(new Debris(sketch, x, y, color, particleCount, length, shape));
    }

    // Constructor for shortened method with default length and shape parameters
    public Debris(PApplet sketch, float x, float y, int color, int particleCount) {
        this.sketch = sketch;
        this.x = x;
        this.y = y;
        this.backgroundColour = color;
        length = 5;
        rng = new Random();
        particle = new ArrayList<>();
        lifespan = 60;
        shape = "ball";
        for (int i = 0; i < particleCount; i++) {
            Particle particleObject = new Particle(x, y);
            particleObject.setVelocities(rng.nextInt(20 ) - 10, rng.nextInt(20 ) - 10);
            particle.add(particleObject);
        }
    }

    // Constructor for lengthened method
    public Debris(PApplet sketch, float x, float y, int color, int particleCount,  float length, String shape) {
        this(sketch, x, y, color, particleCount);
        this.length = length;
        this.shape = shape;
    }

    public void update() {
        lifespan--;

        // Update the particles. If the particle is dead, remove it from the list
        for (int i = particle.size() - 1; i >= 0; i--) {
            Particle particleObject = particle.get(i);
            particleObject.update(sketch.height);
            if (particleObject.dead) {
                particle.remove(i);
            }
        }
    }

    public void display() {
        for (Particle particleObject : particle) {
            // Fade the particles colors as it dies out
            sketch.fill(this.backgroundColour, 255*(lifespan/60f));
            sketch.noStroke();

            // Draw a cube if it is asked to be a cube, or ball if it is not...
            if (shape.equals("cube")) {
                sketch.square(particleObject.x, particleObject.y, length);
            }
            else {
                sketch.circle(particleObject.x, particleObject.y, length);
            }
        }
    }
}
