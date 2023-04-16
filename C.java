package Talban.Final;

public class C {
    // The players X velocity
    public static final float playerXVelocity = 10f;

    // The initial fall velocity as soon as the player begins their descent
    public static final float initialFallVelocity = 2.15f;

    // A constant that decreases the other factors in the players acceleration and deceleration
    public static final float gravityConstant = 0.75f;

    // How much the player will accelerate as they fall or jump
    public static final float gravityAcceleration = 1.05f;

    // A tick method of determining the curvature of the players jump (if you were to create a graph)
    public static final int maxJumpTick = 20;

    // The peak fall tick the player can reach
    public static final int terminalVelocityTick = 60;

    // How many jumps the player is allowed to perform before needing to find ground (including initial jump)
    public static final int maxJumpsAllowed = 2;

    // How many pixels the center of the platform can spawn near the edges of the screen
    public static final int platformEdgeOffset = 100;

    // Maximum width the platform can randomly have
    public static final int platformMaxLength = 300;

    // Minimum width the platform can randomly have
    public static final int platformMinLength = 100;

    // The falling speed of objects (Default: 1.5f)
    public static final float objectYVelocity = 1.5f;

    // The falling speed of background objects
    public static final float backgroundObjectYVelocity = 2f;

    // The maximum x velocity of objects. (Default: 2f | Mainly used for the platform mobs)
    public static final float objectXVelocity = 2f;

    // A tick counter that determines when the next platform can be placed
    public static final int nextPlatformTick = 100;

    // An integer to determine how many ticks need to pass before the day is considered finished
    public static final float endOfDay = 300;

    // How fast time progresses per frame
    public static final float timeSpeed = 0.15f;

    // The chances of a platform that has an X Velocity being spawned (Default: 1 in 5)
    public static final int[] movingPlatformChance = {1, 5};

    // The chances of a coin drop being spawned (Default 5 in 200)
    public static final int[] coinDropChance = {5, 200};

    // The chances of a falling barrel being spawned (Default 2 in 400)
    public static final int[] fallingBarrelChance = {3, 400};

    // The chances of a background object being spawned in (Default 5 in 200)
    public static final int[] backgroundObjectChance = {5, 200};

    // The chances of a platform mob being spawned in (Default 4 in 20)
    public static final int[] platformMobSpawnChance = {4, 20};

    // The chances of a background object being a foreground object
    public static final int[] foregroundObjectChance = {3, 10};

    // Animation tick starter for dead zone
    public static final int deadZoneAnimationTick = 20;

    // How far from the top of the screen until the dead zone needs to reset
    public static final int deadZoneSafeArea = 200;

    // Base x thrust that the players can receive from the barrel
    public static final int fallingBarrelXThrust = 10;

    // Base Y thrust that the players can receive from the barrel
    public static final int fallingBarrelYThrust = 10;


}
