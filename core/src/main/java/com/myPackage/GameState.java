package com.myPackage;

import java.io.Serializable;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private int level;
    private float playerX, playerY; // Example of player position; you can expand this based on your needs.

    public GameState(int level, float playerX, float playerY) {
        this.level = level;
        this.playerX = playerX;
        this.playerY = playerY;
    }

    // Getters and Setters
    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public float getPlayerX() {
        return playerX;
    }

    public void setPlayerX(float playerX) {
        this.playerX = playerX;
    }

    public float getPlayerY() {
        return playerY;
    }

    public void setPlayerY(float playerY) {
        this.playerY = playerY;
    }
}
