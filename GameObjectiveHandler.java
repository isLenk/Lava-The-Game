package Talban.Final;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Random;

public class GameObjectiveHandler {
    // Stores all the objectives that the game contains
    private static ArrayList<GameObjective> objectiveList;

    // Stores the objective of the current round
    private static GameObjective currentObjective;

    // Random used for selecting a random objective
    private static Random rng;

    // Add objectives to the list (Called on setup())
    public static void init(PApplet sketch) {
        rng = new Random();
        objectiveList = new ArrayList<>();

        // Survive for 60 seconds
        GameObjective objective = new GameObjective(sketch);
        objective.setDescription("Survive for 60 seconds!");
        objective.setSurviveTime(60);
        objectiveList.add(objective);

        // Survive for 30 seconds
        objective = new GameObjective(sketch);
        objective.setDescription("Don't die for 30 seconds!");
        objective.setSurviveTime(30);
        objectiveList.add(objective);

        // Survive for 30 seconds
        objective = new GameObjective(sketch);
        objective.setDescription("Stay alive for 20 seconds!");
        objective.setSurviveTime(20);
        objectiveList.add(objective);

        // Survive for 10 seconds
        objective = new GameObjective(sketch);
        objective.setDescription("Survive for 10 seconds!");
        objective.setSurviveTime(10);
        objectiveList.add(objective);
    }

    // Updates the current objective if it is available
    public static void update() {
        if (currentObjective != null) {
            currentObjective.update();
        }
    }

    // Displays the current objective data if it is available
    public static void display() {
        if (currentObjective != null) {
            currentObjective.display();
        }
    }

    // Checks if the objective has been completed, return true if so.
    public static boolean objectiveComplete() {
        if (currentObjective == null) {return false;}
        if (currentObjective.completed()) {
            return true;
        }
        return false;
    }

    // Removes the objective
    public static void endObjective() {
        currentObjective = null;
    }

    // Pick a random objective and assign it to currentObjective. Start it afterwards
    public static void rollNewObjective() {
       currentObjective = objectiveList.get(rng.nextInt(objectiveList.size() -1));
       currentObjective.start();
    }
}
