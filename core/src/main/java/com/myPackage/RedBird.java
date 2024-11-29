package com.myPackage;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.physics.box2d.World;

public class RedBird extends BirdClass {
    private boolean abilityActivated = false;
    private float abilityDuration = 1f;
    private float abilityTimer = 0f;
    private float originalRadius;

    public RedBird(World world, float x, float y, float radius) {
        super(world, x, y, radius, Gdx.files.internal("birds/red.png").path());
        this.originalRadius = radius;
    }


    public void activateAbility() {
        if (!abilityActivated) {
            setRadius(originalRadius * 2);
            abilityActivated = true;
            abilityTimer = abilityDuration;
            System.out.println("Special ability activated! Bird size doubled.");
        }
    }


    public void resetAbility() {
        if (abilityActivated) {
            setRadius(originalRadius);
            abilityActivated = false;
            System.out.println("Special ability reset! Bird size returned to normal.");
        }
    }


    public void update(float delta) {
        if (abilityActivated) {
            abilityTimer -= delta;
            if (abilityTimer <= 0) {
                resetAbility();
            }
        }
    }
}
