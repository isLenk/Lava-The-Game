package Talban.Final;

import processing.core.PApplet;
import processing.core.PFont;


public class GameFont {
    // The main font used in basically everything else
    static PFont mainFont;
    // Used in describing the objective of the round at the start of each game
    static PFont objectiveDescriptionFont;

    // Initialize the fonts that will be used by the game
    public static void init(PApplet sketch) {
        mainFont = sketch.createFont(FilePath.get("/Talban/Final/Fonts/BitFont.ttf"), 60);
        objectiveDescriptionFont = sketch.createFont(FilePath.get("/Talban/Final/Fonts/BitFont.ttf"), 30);
    }

}
