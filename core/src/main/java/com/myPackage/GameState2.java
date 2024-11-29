package com.myPackage;

import java.io.Serializable;

public class GameState2 implements Serializable {
    private static final long serialVersionUID = 1L;

    private int level;
    private int pigsKilled;
    private int pigsRemaining;
    private float timeLeft;  // Time left in seconds

    // Constructor
    public GameState2(int level, int pigsKilled, int pigsRemaining, float timeLeft) {
        this.level = level;
        this.pigsKilled = pigsKilled;
        this.pigsRemaining = pigsRemaining;
        this.timeLeft = timeLeft;
    }

    // Getters and setters
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getPigsKilled() {
        return pigsKilled;
    }

    public void setPigsKilled(int pigsKilled) {
        this.pigsKilled = pigsKilled;
    }

    public int getPigsRemaining() {
        return pigsRemaining;
    }

    public void setPigsRemaining(int pigsRemaining) {
        this.pigsRemaining = pigsRemaining;
    }

    public float getTimeLeft() {
        return timeLeft;
    }

    public void setTimeLeft(float timeLeft) {
        this.timeLeft = timeLeft;
    }
}
