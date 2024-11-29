//package com.myPackage;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.graphics.Texture;
//import com.badlogic.gdx.graphics.g2d.SpriteBatch;
//import com.badlogic.gdx.math.Vector2;
//import com.badlogic.gdx.physics.box2d.*;
//
//
//public class RedBird extends BirdClass{
//    private boolean abilityActivated = false;
//    private float abilityDuration = 1f; // Time in seconds for the ability to remain active
//    private float abilityTimer = 0f;
//
//    public RedBird(World world, float x, float y, float radius) {
//        super(world, x, y, radius, Gdx.files.internal("birds/red.png").path());// Adjust the path as needed
//    }
//
//    public void activateAbility() {
//        if (!abilityActivated) {
//            float newRadius = getRadius() * 2; // Double the radius
//            setRadius(newRadius);
//
//            abilityActivated = true;
//            abilityTimer = abilityDuration; // Start the timer
//            System.out.println("Special ability activated! Bird size doubled.");
//        }
//    }
//
//    public void resetAbility() {
//        if (abilityActivated) {
//            setRadius(getRadius() / 2); // Return to original size
//            abilityActivated = false;
//            System.out.println("Special ability reset! Bird size returned to normal.");
//        }
//    }
//
//    // Update the ability timer
//    public void update(float delta) {
//        if (abilityActivated) {
//            abilityTimer -= delta; // Decrease the timer
//            if (abilityTimer <= 0) {
//                resetAbility(); // Reset size when the timer runs out
//            }
//        }
//    }
//
//}
package com.myPackage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class RedBird extends BirdClass {
    private boolean abilityActivated = false;
    private float abilityDuration = 1f; // Time in seconds for the ability to remain active
    private float abilityTimer = 0f;
    private float originalRadius; // Store the original radius

    public RedBird(World world, float x, float y, float radius) {
        super(world, x, y, radius, Gdx.files.internal("birds/red.png").path());
        this.originalRadius = radius; // Initialize the original radius
    }

    // Special ability: Double the size of the bird
    public void activateAbility() {
        if (!abilityActivated) {
            setRadius(originalRadius * 2); // Double the radius
            abilityActivated = true;
            abilityTimer = abilityDuration; // Start the timer
            System.out.println("Special ability activated! Bird size doubled.");
        }
    }

    // Reset the ability by restoring the original size
    public void resetAbility() {
        if (abilityActivated) {
            setRadius(originalRadius); // Restore the original radius
            abilityActivated = false;
            System.out.println("Special ability reset! Bird size returned to normal.");
        }
    }

    // Update the ability timer
    public void update(float delta) {
        if (abilityActivated) {
            abilityTimer -= delta; // Decrease the timer
            if (abilityTimer <= 0) {
                resetAbility(); // Reset the size when the timer runs out
            }
        }
    }
}
